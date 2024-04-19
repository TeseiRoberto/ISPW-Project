package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.RequestChangesViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.ChangesManager;

import java.util.List;


// Graphic controller used by the "SIMPLE_USER" to create and send a request of changes on an offer received
public class RequestChangesGfxControllerCmd extends BaseGfxControllerCmd {

    private boolean                     runLoop;
    private final RequestChangesViewCmd view;
    private final Announcement          currAnnounce;
    private final Offer                 currOffer;

    private ChangesOnOffer              changes;

    private boolean                     isDurationChangeRequired;
    private Duration                    durationChanges;

    private boolean                     isAccommodationChangeRequired;
    private Accommodation               accommodationChanges;

    private boolean                     isTransportChangeRequired;
    private Transport                   transportChanges;


    public RequestChangesGfxControllerCmd(Session session, Announcement announce, Offer offer)
    {
        super(session);
        currOffer = offer;
        currAnnounce = announce;
        view = new RequestChangesViewCmd();
        onResetRequestedChangesSelected();
    }


    public void start()
    {
        menu();
    }


    private void menu()
    {
        runLoop = true;
        List<String> possibilities = List.of( "Request change on field", "send request", "back to announcement details", "reset requested changes" );

        do {
            view.showScreenTitle();
            view.showOffer(currOffer);
            view.showChangesOnOffer(changes);

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1:     onEditFieldSelected(); break;
                case 2:     onSendRequestSelected(); break;
                case 3:     onBackSelected(); break;
                case 4:     onResetRequestedChangesSelected();
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }
        } while(runLoop);
    }


    // Resets all the fields of the request of changes so that no change is specified
    private void onResetRequestedChangesSelected()
    {
        changes = new ChangesOnOffer(currSession.getUsername(), currOffer.getId(), currOffer.getBidderUsername());

        isDurationChangeRequired = false;
        durationChanges = new Duration(
                currOffer.getHolidayDuration().getDepartureDate(),
                currOffer.getHolidayDuration().getReturnDate()
        );

        isAccommodationChangeRequired = false;
        accommodationChanges = new Accommodation(
                currOffer.getAccommodationOffer().getType(),
                currOffer.getAccommodationOffer().getQuality(),
                currOffer.getAccommodationOffer().getNumOfRooms()
        );

        isTransportChangeRequired = false;
        transportChanges = new Transport(
            currOffer.getTransportOffer().getType(),
            currOffer.getTransportOffer().getQuality(),
            currOffer.getTransportOffer().getDepartureLocation(),
            currOffer.getDestination(),
            currOffer.getTransportOffer().getNumOfTravelers()
        );
    }


    // Invoked when "edit request field" is selected. It converts the int inserted
    // by the user to a string and asks the user to insert a new value for the announcement field associated to that string
    private void onEditFieldSelected()
    {
        view.print("Which field do you want to edit? ==> ");
        int choice = view.getIntFromUser(1, BaseViewCmd.OFFER_FIELDS.size());

        try {
            switch(BaseViewCmd.OFFER_FIELDS.get(choice))
            {
                case "destination":
                    view.print("Insert your destination: ");
                    transportChanges.setArrivalLocation(view.getStringFromUser());
                    isTransportChangeRequired = true;
                    break;

                case "departure date":
                    view.print("Insert the departure date: ");
                    durationChanges.setDepartureDate(view.getDateFromUser());
                    isDurationChangeRequired = true;
                    break;

                case "return date":
                    view.print("Insert the return date: ");
                    durationChanges.setReturnDate(view.getDateFromUser());
                    isDurationChangeRequired = true;
                    break;

                case "price":
                    view.print("Insert the required price: ");
                    changes.setPrice(view.getIntFromUser(1, Integer.MAX_VALUE));
                    break;

                case "accommodation type": {
                    List<String> types = Accommodation.getAvailableTypes();
                    view.printList(types, true);
                    view.print("Choose your accommodation type: ");

                    int chosenType = view.getIntFromUser(1, types.size());
                    accommodationChanges.setType(types.get(chosenType - 1));
                    isAccommodationChangeRequired = true;
                }   break;

                case "accommodation name":
                case "accommodation address":
                    if(view.showConfirmDialog("Do you want to request a change on the accommodation?"))
                        isAccommodationChangeRequired = true;
                    break;

                case "accommodation quality":
                    view.print("Insert the accommodation quality: ");
                    accommodationChanges.setQuality(view.getIntFromUser(1, 5));
                    isAccommodationChangeRequired = true;
                    break;

                case "number of rooms":
                    view.print("Insert the number of rooms required: ");
                    accommodationChanges.setNumOfRooms(view.getIntFromUser(1, Integer.MAX_VALUE));
                    isAccommodationChangeRequired = true;
                    break;

                case "transport type": {
                    List<String> types = Transport.getAvailableTypes();
                    view.printList(types, true);
                    view.print("Choose your transport type: ");

                    int chosenType = view.getIntFromUser(1, types.size());
                    transportChanges.setType(types.get(chosenType - 1));
                    isTransportChangeRequired = true;
                }   break;

                case "transport company name":
                    if(view.showConfirmDialog("Do you want to request a change on the transport company?"))
                        isTransportChangeRequired = true;
                    break;

                case "transport quality":
                    view.print("Insert the transport quality: ");
                    transportChanges.setQuality(view.getIntFromUser(1, 5));
                    isTransportChangeRequired = true;
                    break;

                case "departure location":
                    view.print("Insert the number of travelers: ");
                    transportChanges.setDepartureLocation(view.getStringFromUser());
                    break;

                case "number of travelers":
                    view.print("Insert the number of travelers: ");
                    transportChanges.setNumOfTravelers(view.getIntFromUser(1, Integer.MAX_VALUE));
                    isTransportChangeRequired = true;
                    break;

                default:
                    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG);
                    return;
            }
        } catch (IllegalArgumentException e){
            view.showErrorDialog(e.getMessage());
            return;
        }

        assembleRequestOfChanges();
    }


    // Invoked when "send request" is selected, it invokes the logic controller to send the request of changes to the travel agency
    private void onSendRequestSelected()
    {
        view.print("Add a description of ,or a motivation for, the requested changes: ");
        changes.setChangesDescription(view.getStringFromUser());

        // Ask for user confirm
        if(!view.showConfirmDialog("Do you really want to request the specified changes?"))
            return;

        try {
            ChangesManager changesManager = new ChangesManager();
            changesManager.requestChangesOnOffer(currSession, changes, currOffer);
            view.showInfoDialog("Request of changes sent correctly!");
        } catch (DbException | IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
            return;
        }

        runLoop = false;
        CmdApplication.changeScreen( new AnnouncementDetailsGfxControllerCmd(currSession, currAnnounce) );
    }


    // Invoked when "back to announcement details" is selected, stops the menu loop and switches back to the "announcement details" view
    private void onBackSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen( new AnnouncementDetailsGfxControllerCmd(currSession, currAnnounce) );
    }


    // Updates the attributes of the ChangesOnOffer instance according to the changes inserted by the user
    private void assembleRequestOfChanges()
    {
        if(isDurationChangeRequired)
            changes.setHolidayDuration(durationChanges);

        if(isAccommodationChangeRequired)
            changes.setAccommodationChanges(accommodationChanges);

        if(isTransportChangeRequired)
            changes.setTransportChanges(transportChanges);
    }

}
