package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.makeofferview.BaseMakeOfferViewCmd;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.BaseGfxControllerCmd;
import com.rt.ispwproject.logiccontrollers.OfferManager;

import java.util.List;


// This controller implements some basic functionalities for the make offer screen, this is specialized
// by other graphic controllers to implement the make offer screen and make counteroffer screen.
public abstract class BaseMakeOfferGfxControllerCmd extends BaseGfxControllerCmd {

    protected boolean                       runLoop;
    protected boolean                       accommodationChosen;    // Indicates if accommodation has been chosen for the offer
    protected boolean                       transportChosen;        // Indicates if transport has been chosen for the offer
    protected final BaseMakeOfferViewCmd    view;
    protected Offer                         currOffer;


    protected BaseMakeOfferGfxControllerCmd(Session session, BaseMakeOfferViewCmd view)
    {
        super(session);
        this.view = view;
        this.currOffer = new Offer();
        accommodationChosen = false;
        transportChosen = false;
    }


    protected abstract void menu();


    // Invoked when "edit offer field" is selected.
    // It converts the int inserted by the user to a string and asks the user to insert
    // a new value for the field of the offer associated to the string
    protected void onEditOfferFieldSelected()
    {
        view.print("Which field do you want to edit? ==> ");
        int choice = view.getIntFromUser(1, BaseViewCmd.OFFER_FIELDS.size());

        try {
            switch(BaseViewCmd.OFFER_FIELDS.get(choice))
            {
                case "destination":
                    if(!askConfirmToChangeField())
                        break;

                    view.print("Insert the destination: ");
                    currOffer.setDestination(view.getStringFromUser());
                    break;

                case "departure date":
                    if(!askConfirmToChangeField())
                        break;

                    view.print("Insert the departure date: ");
                    currOffer.getHolidayDuration().setDepartureDate(view.getDateFromUser());
                    break;

                case "return date":
                    if(!askConfirmToChangeField())
                        break;

                    view.print("Insert the return date: ");
                    currOffer.getHolidayDuration().setReturnDate(view.getDateFromUser());
                    break;

                case "price":
                    view.showErrorDialog("You cannot set the price manually");
                    break;

                case "accommodation type", "accommodation name", "accommodation address",
                        "accommodation quality", "number of rooms":
                    onChooseAccommodationSelected();
                    break;

                case "transport type", "transport company name", "transport quality",
                        "departure location", "number of travelers":
                    onChooseTransportSelected();
                    break;

                default:
                    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG);
                    break;
            }
        } catch (IllegalArgumentException e){
            view.showErrorDialog(e.getMessage());
        }

        updateOfferPrice();
    }


    // Gets the criteria for the research of accommodations from the user, loads all the accommodation offers
    // that matches the given criteria and then let the user pick one of them
    protected void onChooseAccommodationSelected()
    {
        if(!view.showConfirmDialog("Do you want to see the available accommodation offers?"))
            return;

        try {
            if(currOffer.getDestination().isBlank())
                throw new IllegalArgumentException("Please insert a destination to get the available accommodations.");

            if(currOffer.getHolidayDuration().getDepartureDate() == null || currOffer.getHolidayDuration().getReturnDate() == null)
                throw new IllegalArgumentException("Please insert the departure and return date to get the available accommodations.");

            int numOfRooms;
            List<Accommodation> availableAccommodations;

            view.print("Insert the number of rooms required: ");
            numOfRooms = view.getIntFromUser(1, Integer.MAX_VALUE);

            // Get list of the available accommodations and display it
            OfferManager offerManager = new OfferManager();
            availableAccommodations = offerManager.getAvailableAccommodations(currOffer.getDestination(), currOffer.getHolidayDuration(), numOfRooms);

            if(availableAccommodations.isEmpty())
            {
                view.print("No accommodation offer is available");
            } else {
                view.showAccommodationOffersList(availableAccommodations);
                view.print("Which offer do you want to select? (enter 0 to select none): ");
                int choice = view.getIntFromUser(0, availableAccommodations.size());

                if(choice != 0)
                {
                    accommodationChosen = true;
                    currOffer.setAccommodationOffer(availableAccommodations.get(choice - 1));
                }
            }
        } catch (ApiException | IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }


    // Loads all the transport available and asks the user to select one of them
    protected void onChooseTransportSelected()
    {
        if(!view.showConfirmDialog("Do you want to see the available transport offers?"))
            return;

        try {
            if(currOffer.getDestination().isBlank())
                throw new IllegalArgumentException("Please insert a destination to get the available transport.");

            if(currOffer.getHolidayDuration().getDepartureDate() == null || currOffer.getHolidayDuration().getReturnDate() == null)
                throw new IllegalArgumentException("Please insert the departure and return date to get the available transport.");

            int numOfTravelers;
            String departureLocation;
            List<Transport> availableTransport;

            view.print("Insert the departure location: ");
            departureLocation = view.getStringFromUser();

            view.print("Insert the number of travelers required: ");
            numOfTravelers = view.getIntFromUser(1, Integer.MAX_VALUE);

            // Get list of the available transport and display it
            OfferManager offerManager = new OfferManager();
            availableTransport = offerManager.getAvailableTransports(departureLocation, currOffer.getDestination(), currOffer.getHolidayDuration(), numOfTravelers);

            if(availableTransport.isEmpty())
            {
                view.print("No transport offer is available");
            } else {
                view.showTransportOffersList(availableTransport);
                view.print("Which offer do you want to select? (enter 0 to select none): ");
                int choice = view.getIntFromUser(0, availableTransport.size());

                if(choice != 0)
                {
                    transportChosen = true;
                    currOffer.setTransportOffer(availableTransport.get(choice - 1));
                }
            }
        } catch (ApiException | IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }


    // Calculates and updates the price of the offer
    private void updateOfferPrice()
    {
        if(accommodationChosen || transportChosen)
        {
            OfferManager offerManager = new OfferManager();
            try {
                currOffer.setPrice(offerManager.calculateOfferPrice(currOffer.getAccommodationOffer(), currOffer.getTransportOffer()));
            } catch (IllegalArgumentException e) {
                view.showErrorDialog("Error during update of the offer price:\n" + e.getMessage());
            }
        }
    }


    // If some fields of the offer changes then the selected accommodation/transport may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen accommodation/transport and returns true
    private boolean askConfirmToChangeField()
    {
        if(accommodationChosen || transportChosen)
        {
            if(view.showConfirmDialog("If you continue then the selected accommodation and transport will be lost.\nDo you want to proceed? "))
            {
                currOffer.setAccommodationOffer(new Accommodation());
                currOffer.setTransportOffer(new Transport());
                accommodationChosen = false;
                transportChosen = false;
            } else {
                return false;
            }
        }
        return true;
    }
}
