package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.AccommodationOfferGfxElement;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualityIndicator;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.TransportOfferGfxElement;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MakeOfferGfxControllerJfx extends BaseGfxControllerJfx {

    private final Session           currSession;
    private final Announcement      currAnnounce;
    private AccommodationOffer      chosenAccommodation = null; // Accommodation offer chosen using the search window
    private TransportOffer          chosenTransport = null;     // Transport offer chosen using the search window
    private Stage                   searchWindow = null;        // Window used to display a list of the available accommodations/transports
    private VBox                    availableElements = null;   // Vbox used in the search window to contain the available accommodations/transports


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
    @FXML private TextField         offeredNumOfRoomsTextfield;
    @FXML private Text              offeredAccommodationPriceText;
    @FXML private Text              offeredTransportTypeText;
    @FXML private Text              offeredTransportCompanyNameText;
    @FXML private HBox              offeredTransportQualityHbox;
    private final QualityIndicator  offeredTransportQuality = new QualityIndicator(0);
    @FXML private TextField         offeredDepartureLocationTextfield;
    @FXML private Text              offeredTransportPriceText;
    @FXML private TextField         offeredNumOfTravelersTextfield;
    @FXML private Text              offeredPriceText;

    /* This variable is used in the onDepartureDateChange and onReturnDateChange callbacks, is needed to avoid getting stuck in
     * a loop indeed such callbacks can change the value of the property they are related to and when that happens the callback is invoked again
     * To avoid this we set and check the value of this variable every time a change happens*/
    private boolean                 dataUpdatedOnCallback = false;


    public MakeOfferGfxControllerJfx(Session session, Announcement announce)
    {
        this.currSession = session;
        this.currAnnounce = announce;
    }


    @FXML void initialize()
    {
        setRequestFields();

        // Insert quality indicators for the offer in their hboxes
        offeredAccommodationQualityHbox.getChildren().add(offeredAccommodationQuality);
        offeredTransportQualityHbox.getChildren().add(offeredTransportQuality);

        // Set callbacks for the offer fields
        offeredDestinationTextfield.textProperty().addListener((ov, oldVal, newVal) -> onDestinationChanged(oldVal));
        offeredNumOfRoomsTextfield.textProperty().addListener((ov, oldVal, newVal) -> onNumOfRoomsChanged(oldVal));
        offeredNumOfTravelersTextfield.textProperty().addListener((ov, oldVal, newVal) -> onNumOfTravelersChanged(oldVal));
        offeredDepartureLocationTextfield.textProperty().addListener((ov, oldVal, newVal) -> onDepartureLocationChanged(oldVal));
        offeredDepartureDatePicker.valueProperty().addListener((ov, oldVal, newVal) -> onDepartureDateChanged(oldVal));
        offeredReturnDatePicker.valueProperty().addListener((ov, oldVal, newVal) -> onReturnDateChanged(oldVal));
        updateOfferPrice();
    }


    // Fills in the fields of the user request with the announcement data
    public void setRequestFields()
    {
        announcementOwnerText.setText(currAnnounce.getOwner());
        announcementDescriptionTextarea.setText(currAnnounce.getHolidayDescription());
        requestedDestinationText.setText(currAnnounce.getDestination());
        requestedDepartureDateText.setText(currAnnounce.getHolidayDuration().getDepartureDate().toString());
        requestedReturnDateText.setText(currAnnounce.getHolidayDuration().getReturnDate().toString());
        requestedAccommodationTypeText.setText(currAnnounce.getAccommodationType().toString());
        requestedNumOfRoomsText.setText( Integer.toString(currAnnounce.getNumOfRoomsRequired()) );
        requestedTransportTypeText.setText(currAnnounce.getTransportType().toString());
        requestedDepartureLocationText.setText(currAnnounce.getDepartureLocation());
        availableBudgetText.setText(currAnnounce.getAvailableBudgetAsStr());
        requestedNumOfTravelersText.setText( Integer.toString(currAnnounce.getNumOfTravelers()) );

        requestedAccommodationQuality.setQualityLevel(currAnnounce.getAccommodationQuality());
        requestedAccommodationQualityHbox.getChildren().add(this.requestedAccommodationQuality);
        requestedTransportQuality.setQualityLevel(currAnnounce.getTransportQuality());
        requestedTransportQualityHbox.getChildren().add(this.requestedTransportQuality);
    }


    // If the offered destination changes then the selected accommodation/transport may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen accommodation/transport
    private void onDestinationChanged(String oldVal)
    {
        if(oldVal == null || dataUpdatedOnCallback)
        {
            dataUpdatedOnCallback = false;
            return;
        }

        if(chosenAccommodation != null || chosenTransport != null)
        {
            if(displayConfirmDialog("If you change the destination then the selected accommodation/transport will be lost. "
                    + "Do you want to proceed?") == ButtonType.OK)
            {
                setChosenAccommodation(null);
                setChosenTransport(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredDestinationTextfield.setText(oldVal);
            }
        }
    }


    // If the offered num of rooms changes then the selected accommodation may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen accommodation
    private void onNumOfRoomsChanged(String oldVal)
    {
        if(oldVal == null || dataUpdatedOnCallback)
        {
            dataUpdatedOnCallback = false;
            return;
        }

        if(chosenAccommodation != null)
        {
            if(displayConfirmDialog("If you change the number of rooms then the selected accommodation will be lost. "
                    + "Do you want to proceed?") == ButtonType.OK)
            {
                setChosenAccommodation(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredNumOfRoomsTextfield.setText(oldVal);
            }
        }
    }


    // If the offered num of travelers changes then the selected transport may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen transport
    private void onNumOfTravelersChanged(String oldVal)
    {
        if(oldVal == null || dataUpdatedOnCallback)
        {
            dataUpdatedOnCallback = false;
            return;
        }

        if(chosenTransport != null)
        {
            if(displayConfirmDialog("If you change the number of rooms then the selected transport will be lost. "
                    + "Do you want to proceed?") == ButtonType.OK)
            {
                setChosenTransport(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredNumOfTravelersTextfield.setText(oldVal);
            }
        }
    }


    // If the offered num of travelers changes then the selected transport may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen transport
    private void onDepartureLocationChanged(String oldVal)
    {
        if(oldVal == null || dataUpdatedOnCallback)
        {
            dataUpdatedOnCallback = false;
            return;
        }

        if(chosenTransport != null)
        {
            if(displayConfirmDialog("If you change the departure location then the selected transport will be lost. "
                    + "Do you want to proceed?") == ButtonType.OK)
            {
                setChosenTransport(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredDepartureLocationTextfield.setText(oldVal);
            }
        }
    }


    // If the offered departure date changes then the selected accommodation/transport may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen accommodation/transport
    private void onDepartureDateChanged(LocalDate oldVal)
    {
        if(dataUpdatedOnCallback)                           // Check state variable to avoid infinite loop of invocations
        {
            dataUpdatedOnCallback = false;
            return;
        }

        if(chosenAccommodation != null || chosenTransport != null)
        {
            if(displayConfirmDialog("If you change the departure date then the selected accommodation/transport will be lost. "
                    + "Do you want to proceed?") == ButtonType.OK)
            {
                setChosenAccommodation(null);
                setChosenTransport(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredDepartureDatePicker.valueProperty().set(oldVal);
            }
        }
    }


    // If the offered return date changes then the selected accommodation/transport may become unavailable, ask the user if
    // he wants to proceed with the change and in that case reset the chosen accommodation/transport
    private void onReturnDateChanged(LocalDate oldVal)
    {
        if(dataUpdatedOnCallback)                           // Check state variable to avoid infinite loop of invocations
        {
            dataUpdatedOnCallback = false;
            return;
        }

        if(chosenAccommodation != null || chosenTransport != null)
        {
            if(displayConfirmDialog("If you change the return date then the selected accommodation/transport will be lost. "
                    + "Do you want to proceed?") == ButtonType.OK)
            {
                setChosenAccommodation(null);
                setChosenTransport(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredReturnDatePicker.valueProperty().set(oldVal);
            }
        }
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
        String destination = offeredDestinationTextfield.getText();
        String numOfRoomsAsStr = offeredNumOfRoomsTextfield.getText();
        int numOfRooms = 0;
        Duration checkInOutDates = null;

        try {
            if(destination == null || destination.isEmpty())
                throw new IllegalArgumentException("Please insert a destination to get the available accommodations.");

            if(numOfRoomsAsStr == null || numOfRoomsAsStr.isEmpty())
                throw new IllegalArgumentException("Please insert the number of rooms required to get the available accommodations.");

            numOfRooms = Integer.parseInt(numOfRoomsAsStr);
            checkInOutDates = new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue());
            createSearchWindow("Accommodation selector", "Available accommodations", 550, 480);

        } catch(IllegalArgumentException e)
        {
            displayErrorDialog(e.getMessage());
            return;
        } catch(IllegalStateException e)                    // The search window was already open
        {
            return;
        }

        // Get list of the available accommodations and display it in the search window
        OfferManager offerManager = new OfferManager();
        List<AccommodationOffer> availableAccommodations = offerManager.getAvailableAccommodations(destination, checkInOutDates, numOfRooms);
        if(availableAccommodations == null || availableAccommodations.isEmpty())
        {
            Label infoMsg = new Label("No accommodation has been found.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(new Font("System", 18));
            availableElements.getChildren().add(infoMsg);
        } else {
            for(AccommodationOffer el : availableAccommodations)
            {
                availableElements.getChildren().add(new AccommodationOfferGfxElement(el, e -> {
                            setChosenAccommodation(el);
                            destroySearchWindow();
                        } )
                );
            }
        }

        updateOfferPrice();                                 // Recalculate offer price
    }


    // Sets the given accommodation offer as the chosen one, updates UI and closes the search window
    public void setChosenAccommodation(AccommodationOffer accommodation)
    {
        chosenAccommodation = accommodation;
        if(accommodation == null)
        {
            offeredAccommodationTypeText.setText("Not selected yet...");
            offeredAccommodationNameText.setText("Not selected yet...");
            offeredAccommodationQuality.setQualityLevel(0);
            offeredAccommodationAddressText.setText("Not selected yet...");
            offeredNumOfRoomsTextfield.setPromptText("Num. of rooms");
            offeredAccommodationPriceText.setText("0€");
        } else {
            offeredAccommodationTypeText.setText(accommodation.getType().toString());
            offeredAccommodationNameText.setText(accommodation.getName());
            offeredAccommodationQuality.setQualityLevel(accommodation.getQuality());
            offeredAccommodationAddressText.setText(accommodation.getAddress());
            offeredAccommodationPriceText.setText(accommodation.getPricePerNightAsStr());
        }
        updateOfferPrice();
    }


    // Invoked when the "search transport" button is clicked
    public void onSearchTransportClick()
    {
        String destination = offeredDestinationTextfield.getText();
        String departureLocation = offeredDepartureLocationTextfield.getText();
        String numOfTravelersAsStr = offeredNumOfTravelersTextfield.getText();
        int numOfTravelers = 0;
        Duration departureAndReturnDates = null;

        try {
            if(destination == null || destination.isEmpty())
                throw new IllegalArgumentException("Please insert a destination to get the available transports.");

            if(departureLocation == null || departureLocation.isEmpty())
                throw new IllegalArgumentException("Please insert a departure location to get the available transports.");

            if(numOfTravelersAsStr == null || numOfTravelersAsStr.isEmpty())
                throw new IllegalArgumentException("Please insert the number of travelers to get the available transports.");

            numOfTravelers = Integer.parseInt(numOfTravelersAsStr);
            departureAndReturnDates = new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue());
            createSearchWindow("Transport selector", "Available transports", 550, 480);

        } catch(IllegalArgumentException e)
        {
            displayErrorDialog(e.getMessage());
            return;
        } catch(IllegalStateException e)                    // The search window was already open
        {
            return;
        }

        // Get list of the available transports and display it in the search window
        OfferManager offerManager = new OfferManager();
        List<TransportOffer> availableTransports = offerManager.getAvailableTransports(
                departureLocation, destination, departureAndReturnDates, numOfTravelers);

        if(availableTransports == null || availableTransports.isEmpty())
        {
            Label infoMsg = new Label("No transport has been found.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(new Font("System", 18));
            availableElements.getChildren().add(infoMsg);
        } else {
            for (TransportOffer el : availableTransports) {
                availableElements.getChildren().add(new TransportOfferGfxElement(el, e -> {
                            setChosenTransport(el);
                            destroySearchWindow();
                        })
                );
            }
        }
    }


    // Sets the given transport offer as the chosen one, updates UI and closes the search window
    public void setChosenTransport(TransportOffer transport)
    {
        chosenTransport = transport;
        if(transport == null)
        {
            offeredTransportTypeText.setText("Not selected yet...");
            offeredTransportCompanyNameText.setText("Not selected yet...");
            offeredTransportQuality.setQualityLevel(0);
            offeredDepartureLocationTextfield.setPromptText("Departure from");
            offeredNumOfTravelersTextfield.setPromptText("Num. of travelers");
            offeredTransportPriceText.setText("0€");
        } else {
            offeredTransportTypeText.setText(transport.getType().toString());
            offeredTransportCompanyNameText.setText(transport.getCompanyName());
            offeredTransportQuality.setQualityLevel(transport.getQuality());
            offeredTransportPriceText.setText(transport.getPriceAsStr());
        }
        updateOfferPrice();
    }


    // Creates a generic search window, a search window is composed of a main title and a scroll pane in which
    // graphics elements will be placed, if a search window was already opened then an exception is thrown
    public void createSearchWindow(String windowTitle, String screenTitle, int width, int height) throws IllegalStateException
    {
        if(searchWindow != null)                            // Only one search window can be active at any given time
        {
            searchWindow.requestFocus();
            throw new IllegalStateException("Search window is already opened!");
        }

        Text screenTitleText = new Text(screenTitle);
        screenTitleText.setFont( new Font("System", 18) );

        availableElements = new VBox();
        availableElements.setAlignment(Pos.TOP_CENTER);
        availableElements.setSpacing(20);
        availableElements.setPadding( new Insets(20, 20, 20, 20) );
        availableElements.getChildren().add(screenTitleText);

        // Create main container
        ScrollPane root = new ScrollPane(availableElements);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Create search window
        searchWindow = new Stage();
        searchWindow.setTitle(windowTitle);
        searchWindow.setResizable(false);
        searchWindow.setScene( new Scene(root, width, height) );
        searchWindow.show();
        searchWindow.setOnCloseRequest(event -> destroySearchWindow());
    }


    // Closes the search window if it is open
    public void destroySearchWindow()
    {
        if(searchWindow != null)
        {
            searchWindow.close();
            searchWindow = null;
            availableElements = null;
        }
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
                throw new IllegalArgumentException("No accommodation has been selected yet...");

            if(chosenTransport == null)
                throw new IllegalArgumentException("No transport has been selected yet...");

            OfferManager offerManager = new OfferManager();
            int offerPrice = offerManager.calculateOfferPrice(chosenAccommodation, chosenTransport);

            Offer newOffer = new Offer(0, currSession.getUsername(), offeredDestinationTextfield.getText(),
                    new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue()),
                    offerPrice, chosenAccommodation, chosenTransport
            );

            offerManager.makeOfferToUser(currSession, currAnnounce, newOffer);
        } catch(IllegalArgumentException | DbException e)
        {
            displayErrorDialog(e.getMessage());
        }
    }


    // Calculates the current offer price and use it to updates the offerPriceText
    public void updateOfferPrice()
    {
        int currPrice = 0;

        if(chosenAccommodation != null || chosenTransport != null)
        {
            OfferManager offerManager = new OfferManager();
            try {
                currPrice = offerManager.calculateOfferPrice(chosenAccommodation, chosenTransport);
            } catch (IllegalArgumentException e) {
                displayErrorDialog("Error during update of the offer price:\n" + e.getMessage());
            }
        }

        this.offeredPriceText.setText( Integer.toString(currPrice) + '€' );
    }


    // Invoked when the "search" button is clicked
    public void onSearchClick()
    {
        displayErrorDialog("Announcements search functionality has not been implemented yet...");
    }

}
