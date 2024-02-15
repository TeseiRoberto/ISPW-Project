package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualityIndicator;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualitySelector;
import com.rt.ispwproject.logiccontrollers.ChangesManager;
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
        requestedAccommodationQualityHbox.getChildren().add(requestedAccommodationQuality);
        requestedTransportQualityHbox.getChildren().add(requestedTransportQuality);

        // Insert quality indicators for the offer in their hboxes
        offeredAccommodationQualityHbox.getChildren().add(offeredAccommodationQuality);
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


    // Constructs a ChangesRequest instance and sends it to the travel agency
    public void onRequestChangesClick()
    {
        try {
            ChangesRequest newRequest = new ChangesRequest(currOffer.getId(), currOffer.getBidderUsername());
            newRequest.setChangesDescription(requestedChangesDescriptionTextarea.getText());
            newRequest.setDestination(requestedDestinationTextfield.getText());
            newRequest.setPrice(getPriceChange());
            newRequest.setDuration(getDurationChanges());
            newRequest.setAccommodationChanges(getAccommodationChanges());  // Get requested changes on accommodation if changes are required
            newRequest.setTransportChanges(getTransportChanges());

            // Ask for user confirm
            ButtonType res = displayConfirmDialog("Do you really want to request the specified changes?");
            if(res != ButtonType.OK)
                return;

            ChangesManager changesManager = new ChangesManager();
            changesManager.requestChangesOnOffer(currSession, newRequest, currOffer);
        } catch (DbException | IllegalArgumentException e)
        {
            displayErrorDialog(e.getMessage());
        }
    }

    // ===========[ Utility methods that helps us to create the request of changes instance ]===========

    // Returns the price that the user would like to pay (-1 if no change is required)
    private int getPriceChange() throws IllegalArgumentException
    {
        int newPrice = -1;
        try {
            if(requestedPriceTextfield.getText() != null)
                newPrice = Integer.parseInt(requestedPriceTextfield.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price cannot contain letters");
        }

        return newPrice;
    }


    // Returns the duration that the user would like for his holiday, null if no change is requested
    private Duration getDurationChanges() throws IllegalArgumentException
    {
        Duration durationChange = null;
        if(requestedDepartureDatePicker.getValue() != null && requestedReturnDatePicker.getValue() != null)
            durationChange = new Duration(requestedDepartureDatePicker.getValue(), requestedReturnDatePicker.getValue());

        return durationChange;
    }


    // Returns an Accommodation instance that contains the requested changes, null if no change is requested
    private Accommodation getAccommodationChanges() throws IllegalArgumentException
    {
        Accommodation accommodationChanges = null;
        if(requestedAccommodationChangeCheckBox.isSelected())               // Check if any change is requested
        {
            String newType = requestedAccommodationTypeComboBox.getValue();
            int newQuality = requestedAccommodationQuality.getQualityLevel();
            String newNumOfRoomsAsStr = requestedNumOfRoomsTextfield.getText();
            int newNumOfRooms = 0;

            if(newType == null || newType.isEmpty())                        // No change is requested on the accommodation type
                newType = currOffer.getAccommodationOffer().getType();

            if(newQuality == 0)                                             // No change is requested on the accommodation quality
                newQuality = currOffer.getAccommodationOffer().getQuality();

            if(newNumOfRoomsAsStr == null || newNumOfRoomsAsStr.isEmpty())  // No change is requested on the number of rooms
            {
                newNumOfRooms = currOffer.getAccommodationOffer().getNumOfRooms();
            } else {
                try {
                    newNumOfRooms = Integer.parseInt(newNumOfRoomsAsStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Number of rooms cannot contain letters");
                }
            }

            accommodationChanges = new Accommodation(newType, newQuality, newNumOfRooms);
        }

        return accommodationChanges;
    }


    // Returns a Transport instance that contains the requested changes, null if no change is requested
    private Transport getTransportChanges() throws IllegalArgumentException
    {
        Transport transportChanges = null;
        if(requestedTransportChangeCheckBox.isSelected())                                   // Check if any change is requested
        {
            String newType = requestedTransportTypeComboBox.getValue();
            int newQuality = requestedTransportQuality.getQualityLevel();
            String newDepartureLocation = requestedDepartureLocationTextfield.getText();
            String newNumOfTravelersAsStr = requestedNumOfTravelersTextfield.getText();
            int newNumOfTravelers = 0;

            if (newType == null || newType.isEmpty())                                       // No change is requested on the transport type
                newType = currOffer.getTransportOffer().getType();

            if (newDepartureLocation == null || newDepartureLocation.isEmpty())             // No change is requested on the departure location
                newDepartureLocation = currOffer.getTransportOffer().getDepartureLocation();

            if(newQuality == 0)                                                             // No change is requested on the transport quality
                newQuality = currOffer.getTransportOffer().getQuality();

            if(newNumOfTravelersAsStr == null || newNumOfTravelersAsStr.isEmpty())          // No change is requested on the number of travelers
            {
                newNumOfTravelers = currOffer.getTransportOffer().getNumOfTravelers();
            } else {
                try {
                    newNumOfTravelers = Integer.parseInt(newNumOfTravelersAsStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Number of travelers cannot contain letters");
                }
            }

            transportChanges =  new Transport(newType, newQuality, newDepartureLocation, newNumOfTravelers);
        }

        return transportChanges;
    }

}
