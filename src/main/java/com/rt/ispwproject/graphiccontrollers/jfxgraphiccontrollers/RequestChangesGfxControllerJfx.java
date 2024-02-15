package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.ChangesRequest;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualityIndicator;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualitySelector;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RequestChangesGfxControllerJfx extends BaseSimpleUserGfxControllerJfx {

    private final Announcement      currAnnounce;
    private final Offer             currOffer;

    @FXML private Text              bidderAgencyNameText;

    // Fields for the changes requested by the user request
    @FXML private TextArea          requestedChangesDescriptionTextarea;
    @FXML private TextField         requestedDestinationTextfield;
    @FXML private TextField         requestedPriceTextfield;
    @FXML private DatePicker        requestedDepartureDatePicker;
    @FXML private DatePicker        requestedReturnDatePicker;
    @FXML private ComboBox<String>  requestedAccommodationTypeComboBox;
    @FXML private HBox              requestedAccommodationQualityHbox;
    private final QualitySelector   requestedAccommodationQuality = new QualitySelector(0, 5);
    @FXML private TextField         requestedNumOfRoomsTextfield;
    @FXML private CheckBox          requestedAccommodationChangeCheckBox;
    @FXML private ComboBox<String>  requestedTransportTypeComboBox;
    @FXML private HBox              requestedTransportQualityHbox;
    private final QualitySelector   requestedTransportQuality = new QualitySelector(0, 5);
    @FXML private TextField         requestedNumOfTravelersTextfield;
    @FXML private TextField         requestedDepartureLocationTextfield;
    @FXML private CheckBox          requestedTransportChangeCheckBox;

    // Fields for the offer made by the travel agency
    @FXML private Text              offeredDestinationText;
    @FXML private Text              offeredPriceText;
    @FXML private Text              offeredDepartureDateText;
    @FXML private Text              offeredReturnDateText;
    @FXML private Text              offeredAccommodationTypeText;
    @FXML private HBox              offeredAccommodationQualityHbox;
    private final QualityIndicator  offeredAccommodationQuality = new QualityIndicator(0);
    @FXML private Text              offeredNumOfRoomsText;
    @FXML private Text              offeredAccommodationNameText;
    @FXML private Text              offeredAccommodationAddressText;
    @FXML private Text              offeredTransportTypeText;
    @FXML private HBox              offeredTransportQualityHbox;
    private final QualityIndicator  offeredTransportQuality = new QualityIndicator(0);
    @FXML private Text              offeredNumOfTravelersText;
    @FXML private Text              offeredDepartureLocationText;
    @FXML private Text              offeredTransportCompanyNameText;


    public RequestChangesGfxControllerJfx(Session session, Stage stage, Announcement announce, Offer offer)
    {
        super(session, stage);
        this.currAnnounce = announce;
        this.currOffer = offer;
    }


    @FXML public void initialize()
    {
        // Insert quality selectors for the requested changes in their hboxes
        requestedAccommodationQualityHbox.getChildren().add(requestedAccommodationQualityHbox);
        requestedTransportQualityHbox.getChildren().add(requestedTransportQuality);

        // Insert quality indicators for the offer in their hboxes
        offeredAccommodationQualityHbox.getChildren().add(offeredAccommodationQualityHbox);
        offeredTransportQualityHbox.getChildren().add(offeredTransportQuality);

        setOfferFields();
        bidderAgencyNameText.setText(currOffer.getBidderUsername());
    }


    // Fills in the fields of the travel agency offer with the current offer data
    public void setOfferFields()
    {
        offeredDestinationText.setText(currOffer.getDestination());
        offeredPriceText.setText(currOffer.getPriceAsStr());
        offeredDepartureDateText.setText(currOffer.getDepartureDate().toString());
        offeredReturnDateText.setText(currOffer.getReturnDate().toString());
        offeredAccommodationTypeText.setText(currOffer.getAccommodationOffer().getType());
        offeredNumOfRoomsText.setText( Integer.toString(currOffer.getAccommodationOffer().getNumOfRooms()) );
        offeredAccommodationNameText.setText(currOffer.getAccommodationOffer().getName());
        offeredAccommodationAddressText.setText(currOffer.getAccommodationOffer().getAddress());
        offeredTransportTypeText.setText(currOffer.getTransportOffer().getType());
        offeredNumOfTravelersText.setText( Integer.toString(currOffer.getTransportOffer().getNumOfTravelers()) );
        offeredDepartureLocationText.setText(currOffer.getTransportOffer().getDepartureLocation());
        offeredTransportCompanyNameText.setText(currOffer.getTransportOffer().getCompanyName());

        offeredAccommodationQuality.setQualityLevel(currOffer.getAccommodationOffer().getQuality());
        offeredTransportQuality.setQualityLevel(currOffer.getTransportOffer().getQuality());
    }


    // Invoked when the "close request of changes" button is clicked, switches back to the announcement details screen
    public void onCloseRequestOfChangesClick()
    {
        changeScreen(getClass().getResource(ANNOUNCEMENT_DETAILS_SCREEN_NAME),
                c -> new AnnouncementDetailsGfxControllerJfx(currSession, mainStage, currAnnounce));
    }


    public void onRequestChangesClick()
    {
        ChangesRequest newRequest = new ChangesRequest(currOffer.getId(), currOffer.getBidderUsername());
        // TODO: Add implementation...
    }
}
