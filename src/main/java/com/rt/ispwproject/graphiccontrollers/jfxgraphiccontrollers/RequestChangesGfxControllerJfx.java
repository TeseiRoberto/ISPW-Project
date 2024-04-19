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

import java.time.LocalDate;

// Graphic controller used by the "SIMPLE_USER" to create and send a request of changes on an offer received
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

        // Set available options for the accommodation type and transport type combo boxes
        requestedAccommodationTypeComboBox.getItems().addAll(Accommodation.getAvailableTypes());
        requestedTransportTypeComboBox.getItems().addAll(Transport.getAvailableTypes());

        setOfferFields();
        bidderAgencyNameText.setText(currOffer.getBidderUsername());

        // Set callbacks for the requested changes fields
        requestedAccommodationTypeComboBox.valueProperty().addListener( (ov, oldVal, newVal) -> onAccommodationTypeChanged(newVal) );
        requestedAccommodationQuality.setOnValueChangeCallback( newVal -> requestedAccommodationChangeCheckBox.setSelected(true) );
        requestedNumOfRoomsTextfield.textProperty().addListener( (ov, oldVal, newVal) -> onNumOfRoomsChanged(newVal) );
        requestedAccommodationChangeCheckBox.selectedProperty().addListener( (ov, oldVal, newVal) -> onChangeAccommodationCheckboxChanged(newVal) );

        requestedDestinationTextfield.textProperty().addListener( (ov, oldVal, newVal) -> onLocationChanged(newVal) );
        requestedTransportTypeComboBox.valueProperty().addListener((ov, oldVal, newVal) -> onTransportTypeChanged(newVal) );
        requestedTransportQuality.setOnValueChangeCallback(  newVal -> requestedTransportChangeCheckBox.setSelected(true) );
        requestedNumOfTravelersTextfield.textProperty().addListener( (ov, oldVal, newVal) -> onNumOfTravelersChanged(newVal) );
        requestedDepartureLocationTextfield.textProperty().addListener( (ov, oldVal, newVal) -> onLocationChanged(newVal) );
        requestedTransportChangeCheckBox.selectedProperty().addListener( (ov, oldVal, newVal) -> onChangeTransportCheckboxChanged(newVal) );
    }


    // Fills in the fields of the travel agency offer with the current offer data
    public void setOfferFields()
    {
        offeredDestinationText.setText(currOffer.getDestination());
        offeredPriceText.setText(currOffer.getPriceAsStr());
        offeredDepartureDateText.setText(currOffer.getHolidayDuration().getDepartureDate().toString());
        offeredReturnDateText.setText(currOffer.getHolidayDuration().getReturnDate().toString());
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


    // Callback invoked when the value of the accommodation type combobox changes, marks as selected the requested accommodation change checkbox
    private void onAccommodationTypeChanged(String newValue)
    {
        if(newValue == null || newValue.isEmpty())
            return;

        requestedAccommodationChangeCheckBox.setSelected(true);
    }


    // Callback invoked when the value of the number of rooms text field changes, marks as selected the requested accommodation change checkbox
    private void onNumOfRoomsChanged(String newValue)
    {
        if(newValue == null || newValue.isEmpty())
            return;

        requestedAccommodationChangeCheckBox.setSelected(true);
    }


    // Callback invoked when the value of the accommodation change checkbox changes, if the checkbox is unselected then
    // all the accommodation changes selected will be lost
    private void onChangeAccommodationCheckboxChanged(boolean newValue)
    {
        if(!newValue)
        {
            requestedAccommodationTypeComboBox.setValue(null);
            requestedAccommodationQuality.setQualityLevel(0);
            requestedNumOfRoomsTextfield.setText(null);
        }
    }


    // Callback invoked when the value of the transport type combobox changes, marks as selected the requested transport change checkbox
    private void onTransportTypeChanged(String newValue)
    {
        if(newValue == null || newValue.isEmpty())
            return;

        requestedTransportChangeCheckBox.setSelected(true);
    }


    // Callback invoked when the value of the number of travelers text field changes, marks as selected the requested transport change checkbox
    private void onNumOfTravelersChanged(String newValue)
    {
        if(newValue == null || newValue.isEmpty())
            return;

        requestedTransportChangeCheckBox.setSelected(true);
    }


    // Callback invoked when the value of the destination/departure location text field changes, marks as selected the requested transport change checkbox
    private void onLocationChanged(String newValue)
    {
        if(newValue == null || newValue.isEmpty())
            return;

        requestedTransportChangeCheckBox.setSelected(true);
    }


    // Callback invoked when the value of the change transport checkbox changes, if the checkbox is unselected then
    // all the transport changes selected will be lost
    private void onChangeTransportCheckboxChanged(boolean newValue)
    {
        if(!newValue)
        {
            requestedDestinationTextfield.setText(null);
            requestedTransportTypeComboBox.setValue(null);
            requestedTransportQuality.setQualityLevel(0);
            requestedDepartureLocationTextfield.setText(null);
            requestedNumOfTravelersTextfield.setText(null);
        }
    }


    // Invoked when the "close request of changes" button is clicked, switches back to the announcement details screen
    public void onCloseRequestOfChangesClick()
    {
        changeScreen(getClass().getResource(ANNOUNCEMENT_DETAILS_SCREEN_NAME),
                c -> new AnnouncementDetailsGfxControllerJfx(currSession, mainStage, currAnnounce));
    }


    // Constructs a ChangesOnOffer instance and sends it to the travel agency, then switch back to the "announcement details" screen
    public void onRequestChangesClick()
    {
        // Ask for user confirm
        ButtonType res = showConfirmDialog("Do you really want to request the specified changes?");
        if(res != ButtonType.OK)
            return;

        try {
            // Build the changes request
            ChangesOnOffer newRequest = new ChangesOnOffer(currSession.getUsername(), currOffer.getId(), currOffer.getBidderUsername());
            newRequest.setChangesDescription(requestedChangesDescriptionTextarea.getText());
            newRequest.setPrice(getPriceChange());
            newRequest.setHolidayDuration(getDurationChanges());
            newRequest.setAccommodationChanges(getAccommodationChanges());
            newRequest.setTransportChanges(getTransportChanges());

            // Send the request
            ChangesManager changesManager = new ChangesManager();
            changesManager.requestChangesOnOffer(currSession, newRequest, currOffer);
            showInfoDialog("Request of changes sent correctly!");
        } catch (DbException | IllegalArgumentException e)
        {
            showErrorDialog(e.getMessage());
            return;
        }

        changeScreen(getClass().getResource(ANNOUNCEMENT_DETAILS_SCREEN_NAME),
                c -> new AnnouncementDetailsGfxControllerJfx(currSession, mainStage, currAnnounce));
    }

    // ===========[ Utility methods that helps us to create the request of changes instance ]===========

    // Returns the price that the user would like to pay (0 if no change is required)
    private int getPriceChange() throws IllegalArgumentException
    {
        int newPrice = 0;
        try {
            String priceAsStr = requestedPriceTextfield.getText();
            if(priceAsStr != null && !priceAsStr.isBlank())
            {
                newPrice = Integer.parseInt(priceAsStr);

                // User cannot explicitly ask for a negative/zero price, the value zero can be used only internally to represent no change
                if(newPrice <= 0)
                    throw new IllegalArgumentException("Price cannot be negative or zero");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price cannot contain letters");
        }

        return newPrice;
    }


    // Returns the duration that the user would like for his holiday, null if no change is requested on the holiday duration
    private Duration getDurationChanges() throws IllegalArgumentException
    {
        LocalDate departureDate = requestedDepartureDatePicker.getValue();
        LocalDate returnDate = requestedReturnDatePicker.getValue();

        if(departureDate == null && returnDate == null)
            return null;

        return new Duration(
                departureDate == null ? currOffer.getHolidayDuration().getDepartureDate() : departureDate,
                returnDate == null ? currOffer.getHolidayDuration().getReturnDate() : returnDate
        );
    }


    // Returns an Accommodation instance that contains the requested changes, returns null if no change is requested on the accommodation
    private Accommodation getAccommodationChanges() throws IllegalArgumentException
    {
        Accommodation accommodationChanges = null;
        if(requestedAccommodationChangeCheckBox.isSelected())               // Check if any change on the accommodation is requested
        {
            String requestedType = currOffer.getAccommodationOffer().getType();
            int requestedQuality = currOffer.getAccommodationOffer().getQuality();
            int requestedNumOfRooms = currOffer.getAccommodationOffer().getNumOfRooms();

            // Check if change is requested on the accommodation type
            if(requestedAccommodationTypeComboBox.getValue() != null)
                requestedType = requestedAccommodationTypeComboBox.getValue();

            // Check if change is requested on the accommodation quality
            if(requestedAccommodationQuality.getQualityLevel() != 0)
                requestedQuality = requestedAccommodationQuality.getQualityLevel();

            // Check if change is requested on the number of rooms
            if(!requestedNumOfRoomsTextfield.getText().isBlank())
            {
                try {
                    requestedNumOfRooms = Integer.parseInt(requestedNumOfRoomsTextfield.getText());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Number of rooms cannot contain letters");
                }
            }

            accommodationChanges = new Accommodation(requestedType, requestedQuality, requestedNumOfRooms);
        }

        return accommodationChanges;
    }


    // Returns a Transport instance that contains the requested changes, returns null if no change is requested on the transport
    private Transport getTransportChanges() throws IllegalArgumentException
    {
        Transport transportChanges = null;
        if(requestedTransportChangeCheckBox.isSelected())       // Check if any change on transport is requested
        {
            String requestedType = currOffer.getTransportOffer().getType();
            int requestedQuality = currOffer.getTransportOffer().getQuality();
            String requestedDepartureLocation = currOffer.getTransportOffer().getDepartureLocation();
            String requestedArrivalLocation = currOffer.getDestination();
            int requestedNumOfTravelers = currOffer.getTransportOffer().getNumOfTravelers();

            // Check if change is requested on the transport type
            if (requestedTransportTypeComboBox.getValue() != null)
                requestedType = requestedTransportTypeComboBox.getValue();

            // Check if change is requested on the departure location
            if (!requestedDepartureLocationTextfield.getText().isBlank())
                requestedDepartureLocation = requestedDepartureLocationTextfield.getText();

            // Check if change is requested on the arrival location (the destination)
            if(!requestedDestinationTextfield.getText().isBlank())
                requestedArrivalLocation = requestedDestinationTextfield.getText();

            // Check if change is requested on the transport quality
            if(requestedTransportQuality.getQualityLevel() != 0)
                requestedQuality = requestedTransportQuality.getQualityLevel();

            // Check if change is requested on the number of travelers
            if(!requestedNumOfTravelersTextfield.getText().isBlank())
            {
                try {
                    requestedNumOfTravelers = Integer.parseInt(requestedNumOfTravelersTextfield.getText());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Number of travelers cannot contain letters");
                }
            }

            transportChanges = new Transport(requestedType, requestedQuality, requestedDepartureLocation, requestedArrivalLocation, requestedNumOfTravelers);
        }

        return transportChanges;
    }

}
