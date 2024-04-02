package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.BaseGfxControllerJfx;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.SearchAnnouncementsGfxControllerJfx;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MakeOfferGfxControllerJfx extends BaseMakeOfferGfxControllerJfx {

    private final Announcement  currAnnounce;


    public MakeOfferGfxControllerJfx(Session session, Stage stage, Announcement announce)
    {
        super(session, stage);
        this.currAnnounce = announce;
    }


    @FXML public void initialize()
    {
        initializeGui();
        setRequestFields();
    }


    // Fills in the fields of the user request with the announcement data
    public void setRequestFields()
    {
        announcementOwnerText.setText(currAnnounce.getOwnerUsername());
        announcementDescriptionTextarea.setText(currAnnounce.getHolidayDescription());
        requestedDestinationText.setText(currAnnounce.getDestination());
        requestedDepartureDateText.setText(currAnnounce.getHolidayDuration().getDepartureDate().toString());
        requestedReturnDateText.setText(currAnnounce.getHolidayDuration().getReturnDate().toString());
        requestedAccommodationTypeText.setText(currAnnounce.getAccommodationRequirements().getType());
        requestedAccommodationQuality.setQualityLevel(currAnnounce.getAccommodationRequirements().getQuality());
        requestedNumOfRoomsText.setText( Integer.toString(currAnnounce.getAccommodationRequirements().getNumOfRooms()) );
        requestedTransportTypeText.setText(currAnnounce.getTransportRequirements().getType());
        requestedTransportQuality.setQualityLevel(currAnnounce.getTransportRequirements().getQuality());
        requestedDepartureLocationText.setText(currAnnounce.getTransportRequirements().getDepartureLocation());
        availableBudgetText.setText(currAnnounce.getAvailableBudgetAsStr());
        requestedNumOfTravelersText.setText( Integer.toString(currAnnounce.getTransportRequirements().getNumOfTravelers()) );
    }


    // Invoked when the "make offer" button is clicked, sends the offer to the user and switches to the "search announcements" screen
    public void onMakeOfferClick()
    {
        try {
            if(chosenAccommodation == null)
                throw new IllegalArgumentException("No accommodation has been selected yet...");

            if(chosenTransport == null)
                throw new IllegalArgumentException("No transport has been selected yet...");

            OfferManager offerManager = new OfferManager();
            int offerPrice = offerManager.calculateOfferPrice(chosenAccommodation, chosenTransport);

            Offer newOffer = new Offer(
                    currSession.getUsername(), currAnnounce.getOwnerUsername(),
                    offeredDestinationTextfield.getText(),
                    new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue()),
                    offerPrice, chosenAccommodation, chosenTransport
            );

            offerManager.makeOfferToUser(currSession, currAnnounce, newOffer);
        } catch(IllegalCallerException | IllegalArgumentException | DbException e)
        {
            displayErrorDialog(e.getMessage());
            return;
        }

        displayInfoDialog("Offer sent correctly!");
        changeScreen(BaseGfxControllerJfx.class.getResource(SEARCH_ANNOUNCEMENTS_SCREEN_NAME),
                c -> new SearchAnnouncementsGfxControllerJfx(currSession, mainStage));
    }


    // Invoked when the "close announcement" button is clicked, switches back to the "search announcements" screen
    public void onCloseAnnouncementClick()
    {
        changeScreen(BaseGfxControllerJfx.class.getResource(SEARCH_ANNOUNCEMENTS_SCREEN_NAME),
                c -> new SearchAnnouncementsGfxControllerJfx(currSession, mainStage));
    }
}
