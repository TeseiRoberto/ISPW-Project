package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.BaseGfxControllerJfx;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.OfferDetailsGfxControllerJfx;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MakeCounterofferGfxControllerJfx extends BaseMakeOfferGfxControllerJfx {

    private final Offer             currOffer;
    private final ChangesOnOffer    requestedChanges;


    public MakeCounterofferGfxControllerJfx(Session session, Stage stage, Offer offer, ChangesOnOffer requestedChanges)
    {
        super(session, stage);
        this.currOffer = offer;
        this.requestedChanges = requestedChanges;
    }


    @FXML public void initialize()
    {
        initializeGui();
        setRequestFields();
        setOfferFields();
    }


    // Fills in the fields of the user request with the requested changes data
    private void setRequestFields()
    {
        this.announcementOwnerText.setText(requestedChanges.getOwnerUsername());
        this.announcementDescriptionTextarea.setText(requestedChanges.getChangesDescription());

        if(requestedChanges.isPriceChangeRequired())            // Price field
            this.availableBudgetText.setText(requestedChanges.getPriceAsStr());
        else
            this.availableBudgetText.setText(currOffer.getPriceAsStr());

        if(requestedChanges.isDurationChangeRequired())         // Departure and return date fields
        {
            this.requestedDepartureDateText.setText(requestedChanges.getHolidayDuration().getDepartureDate().toString());
            this.requestedReturnDateText.setText(requestedChanges.getHolidayDuration().getReturnDate().toString());
        } else {
            this.requestedDepartureDateText.setText(currOffer.getHolidayDuration().getDepartureDate().toString());
            this.requestedReturnDateText.setText(currOffer.getHolidayDuration().getReturnDate().toString());
        }

        if(requestedChanges.isAccommodationChangeRequired())    // Accommodation details fields
        {
            this.requestedAccommodationTypeText.setText(requestedChanges.getAccommodationChanges().getType());
            this.requestedAccommodationQuality.setQualityLevel(requestedChanges.getAccommodationChanges().getQuality());
            this.requestedNumOfRoomsText.setText( Integer.toString(requestedChanges.getAccommodationChanges().getNumOfRooms()) );
        } else {
            this.requestedAccommodationTypeText.setText(currOffer.getAccommodationOffer().getType());
            this.requestedAccommodationQuality.setQualityLevel(currOffer.getAccommodationOffer().getQuality());
            this.requestedNumOfRoomsText.setText( Integer.toString(currOffer.getAccommodationOffer().getNumOfRooms()) );
        }

        if(requestedChanges.isTransportChangeRequired())        // Transport details fields
        {
            this.requestedTransportTypeText.setText(requestedChanges.getTransportChanges().getType());
            this.requestedTransportQuality.setQualityLevel(requestedChanges.getTransportChanges().getQuality());
            this.requestedNumOfTravelersText.setText( Integer.toString(requestedChanges.getTransportChanges().getNumOfTravelers()) );
            this.requestedDepartureLocationText.setText(requestedChanges.getTransportChanges().getDepartureLocation());
            this.requestedDestinationText.setText(requestedChanges.getTransportChanges().getArrivalLocation());
        } else {
            this.requestedTransportTypeText.setText(currOffer.getTransportOffer().getType());
            this.requestedTransportQuality.setQualityLevel(currOffer.getTransportOffer().getQuality());
            this.requestedNumOfTravelersText.setText( Integer.toString(currOffer.getTransportOffer().getNumOfTravelers()) );
            this.requestedDepartureLocationText.setText(currOffer.getTransportOffer().getDepartureLocation());
            this.requestedDestinationText.setText(currOffer.getDestination());
        }
    }


    // Fills in the fields of the offer with the offer data
    private void setOfferFields()
    {
        this.offeredDestinationTextfield.setText(currOffer.getDestination());
        this.offeredDepartureDatePicker.setValue(currOffer.getHolidayDuration().getDepartureDate());
        this.offeredReturnDatePicker.setValue(currOffer.getHolidayDuration().getReturnDate());
        this.offeredNumOfRoomsTextfield.setText( Integer.toString(currOffer.getAccommodationOffer().getNumOfRooms()) );
        this.offeredDepartureLocationTextfield.setText(currOffer.getTransportOffer().getDepartureLocation());
        this.offeredNumOfTravelersTextfield.setText( Integer.toString(currOffer.getTransportOffer().getNumOfTravelers()) );
        setChosenAccommodation(currOffer.getAccommodationOffer());
        setChosenTransport(currOffer.getTransportOffer());
    }


    // Invoked when the "make counteroffer" button is clicked, updates the original offer and switches to the "offer details" screen
    public void onMakeCounterofferClick()
    {
        try {
            if(chosenAccommodation == null)
                throw new IllegalArgumentException("No accommodation has been selected yet...");

            if(chosenTransport == null)
                throw new IllegalArgumentException("No transport has been selected yet...");

            OfferManager offerManager = new OfferManager();
            int offerPrice = offerManager.calculateOfferPrice(chosenAccommodation, chosenTransport);

            // Create a new offer
            Offer newOffer = new Offer(
                    currSession.getUsername(), currOffer.getRelativeAnnouncementOwnerUsername(),
                    offeredDestinationTextfield.getText(),
                    new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue()),
                    offerPrice, chosenAccommodation, chosenTransport
            );

            offerManager.makeCounteroffer(currSession, newOffer, requestedChanges);
            showInfoDialog("Counteroffer sent correctly!");
            changeScreen(BaseGfxControllerJfx.class.getResource(OFFER_DETAILS_SCREEN_NAME),
                    c -> new OfferDetailsGfxControllerJfx(currSession, mainStage, newOffer));

        } catch(IllegalCallerException | IllegalArgumentException | DbException e)
        {
            showErrorDialog(e.getMessage());
        }
    }


    // Invoked when the "close make counteroffer" button is clicked, switches back to the "offer details" screen
    public void onCloseMakeCounterofferClick()
    {
        changeScreen(BaseGfxControllerJfx.class.getResource(OFFER_DETAILS_SCREEN_NAME),
                c -> new OfferDetailsGfxControllerJfx(currSession, mainStage, currOffer));
    }
}
