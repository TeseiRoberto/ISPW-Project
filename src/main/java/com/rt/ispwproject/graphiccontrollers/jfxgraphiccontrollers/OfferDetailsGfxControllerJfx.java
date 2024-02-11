package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class OfferDetailsGfxControllerJfx extends BaseGfxControllerJfx {

    @FXML private Text      announcementOwnerUsernameText;
    @FXML private Text      offerStatusText;

    // Fields for the travel agency offer
    @FXML private Text      offeredDestinationText;
    @FXML private Text      offeredPriceText;
    @FXML private Text      offeredDepartureDateText;
    @FXML private Text      offeredReturnDateText;
    @FXML private Text      offeredAccommodationTypeText;
    @FXML private Text      offeredAccommodationNameText;
    @FXML private HBox      offeredAccommodationQualityHbox;
    @FXML private Text      offeredAccommodationAddressText;
    @FXML private Text      offeredNumOfRoomsText;
    @FXML private Text      offeredTransportTypeText;
    @FXML private Text      offeredTransportCompanyNameText;
    @FXML private HBox      offeredTransportQualityHbox;
    @FXML private Text      offeredDepartureLocationText;
    @FXML private Text      offeredNumOfTravelersText;

    // Note that this vbox gets deleted if no request of change is found during initialization, so the requested* fields become all invalid
    // It is easier to delete the elements when their not needed than to build them from scratch when we need them
    @FXML private VBox      requestedChangesVbox;

    // Fields for the user requested changes
    @FXML private TextArea  requestedChangesDescriptionTextarea;
    @FXML private Text      requestedDestinationText;
    @FXML private Text      requestedPriceText;
    @FXML private Text      requestedDepartureDateText;
    @FXML private Text      requestedReturnDateText;
    @FXML private Text      requestedAccommodationTypeText;
    @FXML private Text      requestedAccommodationChangeText;
    @FXML private HBox      requestedAccommodationQualityHbox;
    @FXML private Text      requestedNumOfRoomsText;
    @FXML private Text      requestedTransportTypeText;
    @FXML private Text      requestedTransportChangeText;
    @FXML private HBox      requestedTransportQualityHbox;
    @FXML private Text      requestedDepartureLocationText;
    @FXML private Text      requestedNumOfTravelersText;


    public OfferDetailsGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    @FXML public void initialize()
    {
        // TODO: Add implementation...
    }


    // Invoked when the "close offer details" button is clicked, switches to the "search announcements" screen
    public void onCloseOfferDetailsClick()
    {
        // TODO: Add implementation...
    }


    // Invoked when the "search announcements" button is clicked, switches to the "search announcements" screen
    public void onSearchAnnouncementsClick()
    {
        changeScreen(getClass().getResource("travelAgency/searchAnnouncementsScreen.fxml"),
                c -> new SearchAnnouncementsGfxControllerJfx(currSession, mainStage));
    }
}
