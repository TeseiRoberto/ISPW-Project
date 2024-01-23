package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualityIndicator;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import com.rt.ispwproject.model.AccommodationType;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MakeOfferGfxControllerJfx extends BaseGfxControllerJfx {

    private final Session           currSession;
    private final Announcement      currAnnounce;
    private AccommodationOffer      chosenAccommodation = null;
    private TransportOffer          chosenTransport = null;

    // Fields for the user request
    @FXML private Text              announcementOwnerText;
    @FXML private TextArea          announcementDescriptionTextarea;
    @FXML private Text              requestedDestinationText;
    @FXML private Text              requestedDepartureDateText;
    @FXML private Text              requestedReturnDateText;
    @FXML private Text              requestedAccommodationTypeText;
    @FXML private HBox              requestedAccommodationQualityHbox;
    private final QualityIndicator  requestedAccommodationQuality = new QualityIndicator(0);
    @FXML private Text              requestedNumOfRoomsText;
    @FXML private Text              requestedTransportTypeText;
    @FXML private HBox              requestedTransportQualityHbox;
    private final QualityIndicator  requestedTransportQuality = new QualityIndicator(0);
    @FXML private Text              requestedNumOfTravelersText;
    @FXML private Text              requestedDepartureLocationText;
    @FXML private Text              availableBudgetText;

    // Fields for the travel agency offer
    @FXML private TextField         offeredDestinationTextfield;
    @FXML private DatePicker        offeredDepartureDatePicker;
    @FXML private DatePicker        offeredReturnDatePicker;
    @FXML private Text              offeredAccommodationTypeText;
    @FXML private Text              offeredAccommodationNameText;
    @FXML private HBox              offeredAccommodationQualityHbox;
    private final QualityIndicator  offeredAccommodationQuality = new QualityIndicator(0);
    @FXML private Text              offeredAccommodationAddressText;
    @FXML private Text              offeredNumOfRoomsText;
    @FXML private Text              offeredAccommodationPriceText;
    @FXML private Text              offeredTransportTypeText;
    @FXML private Text              offeredTransportCompanyText;
    @FXML private HBox              offeredTransportQualityHbox;
    private final QualityIndicator  offeredTransportQuality = new QualityIndicator(0);
    @FXML private Text              offeredDepartureLocationText;
    @FXML private Text              offeredTransportPriceText;
    @FXML private Text              offeredNumOfTravelersText;
    @FXML private Text              offeredPriceText;


    public MakeOfferGfxControllerJfx(Session session, Announcement announce)
    {
        this.currSession = session;
        this.currAnnounce = announce;
    }


    // Set fields for the user request with the current announcement data
    @FXML void initialize()
    {
        this.announcementOwnerText.setText(currAnnounce.getOwner());
        this.announcementDescriptionTextarea.setText(currAnnounce.getHolidayDescription());
        this.requestedDestinationText.setText(currAnnounce.getDestination());
        this.requestedDepartureDateText.setText(currAnnounce.getHolidayDuration().getStartDate().toString());
        this.requestedReturnDateText.setText(currAnnounce.getHolidayDuration().getEndDate().toString());
        this.requestedAccommodationTypeText.setText(currAnnounce.getAccommodationType().toString());
        this.requestedNumOfRoomsText.setText( Integer.toString(currAnnounce.getNumOfRoomsRequired()) );
        this.requestedTransportTypeText.setText(currAnnounce.getTransportType().toString());
        this.requestedDepartureLocationText.setText(currAnnounce.getDepartureLocation());
        this.availableBudgetText.setText( Integer.toString(currAnnounce.getAvailableBudget()) );
        this.requestedNumOfTravelersText.setText( Integer.toString(currAnnounce.getNumOfTravelers()) );

        this.requestedAccommodationQuality.setQualityLevel(currAnnounce.getAccommodationQuality());
        this.requestedAccommodationQualityHbox.getChildren().add(this.requestedAccommodationQuality);
        this.requestedTransportQuality.setQualityLevel(currAnnounce.getTransportQuality());
        this.requestedTransportQualityHbox.getChildren().add(this.requestedTransportQuality);

        this.offeredAccommodationQualityHbox.getChildren().add(this.offeredAccommodationQuality);
        this.offeredTransportQualityHbox.getChildren().add(this.offeredTransportQuality);
    }


    // Invoked when the "close announcement" button is clicked, switches to the "search announcements" screen
    public void onCloseAnnouncementClick()
    {
        changeScreen(getClass().getResource("travelAgency/searchAnnouncementsScreen.fxml"),
                (Stage) announcementDescriptionTextarea.getScene().getWindow(), c -> new SearchAnnouncementsGfxControllerJfx(currSession));
    }


    // Invoked when the "search accommodation" button is clicked
    public void onSearchAccommodationClick()
    {
        // TODO: Need to implement a way to get a list of the available accommodations and display it,
        //  the travel agency will then select one as the chosen accommodation offer
        //  For now we build a dummy offer that matches the request and set it as selected

        chosenAccommodation = new AccommodationOffer(AccommodationType.HOTEL, "Dummy accommodation",
                "dummy road 124", currAnnounce.getAccommodationQuality(), currAnnounce.getNumOfRoomsRequired(),
                100, 1000);

        offeredAccommodationTypeText.setText(chosenAccommodation.getType().toString());
        offeredAccommodationNameText.setText(chosenAccommodation.getName());
        offeredAccommodationQuality.setQualityLevel(chosenAccommodation.getQuality());
        offeredAccommodationAddressText.setText(chosenAccommodation.getAddress());
        offeredNumOfRoomsText.setText(Integer.toString(chosenAccommodation.getNumOfRooms()));
        offeredAccommodationPriceText.setText(Integer.toString(chosenAccommodation.getPricePerNight()));
    }


    // Invoked when the "search transport" button is clicked
    public void onSearchTransportClick()
    {
        // TODO: Need to implement a way to get a list of the available transports and display it,
        //  the travel agency will then select one as the chosen transport offer.
        //  For now we build a dummy offer that matches the request and set it as selected

        chosenTransport = new TransportOffer(currAnnounce.getTransportType(), "Dummy transports",
                currAnnounce.getTransportQuality(), currAnnounce.getDepartureLocation(), currAnnounce.getNumOfTravelers(),
                100, 1000);

        offeredTransportTypeText.setText(chosenTransport.getType().toString());
        offeredTransportCompanyText.setText(chosenTransport.getCompanyName());
        offeredTransportQuality.setQualityLevel(chosenTransport.getQuality());
        offeredDepartureLocationText.setText(chosenTransport.getDepartureLocation());
        offeredTransportPriceText.setText(Integer.toString(chosenTransport.getPricePerTraveler()));
        offeredNumOfTravelersText.setText(Integer.toString(chosenTransport.getNumOfTravelers()));
    }


    // Invoked when the "my offers" button is clicked, switches to the "my offers" screen
    public void onMyOffersClick()
    {
        // TODO: Need to switch to my offers screen
        System.out.println("CLICKED ON MY OFFERS BUTTON!");
    }


    // Invoked when the "make offer" button is clicked
    public void onMakeOfferClick()
    {
        try {
            if(chosenAccommodation == null)
                throw new IllegalArgumentException("No Accommodation has been selected");

            if(chosenTransport == null)
                throw new IllegalArgumentException("No Transport has been selected");

            int offerPrice = chosenAccommodation.getPrice() + chosenTransport.getPrice();
            Offer newOffer = new Offer(0, currSession.getUsername(), offerPrice, offeredDestinationTextfield.getText(),
                    new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue()),
                    chosenAccommodation, chosenTransport
            );

            OfferManager offerManager = new OfferManager();
            offerManager.makeOfferToUser(currAnnounce, newOffer);
        } catch(IllegalArgumentException | DbException e)
        {
            displayError(e.getMessage());
        }
    }


    // Invoked when the "search" button is clicked
    public void onSearchClick()
    {
        displayError("Announcements search functionality has not been implemented yet...");
    }

}
