package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.BaseTravelAgencyGfxControllerJfx;
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


// This controller implements some basic functionalities for the make offer screen, this is specialized
// by other graphic controllers to implement the make offer screen and make counteroffer screen.
public class BaseMakeOfferGfxControllerJfx extends BaseTravelAgencyGfxControllerJfx {

    private static final String         NOT_SELECTED_YET_MSG = "Not selected yet...";
    private static final Font           DEFAULT_FONT = new Font("System", 18);
    protected Accommodation             chosenAccommodation = null; // AccommodationOffer offer chosen using the search window
    protected Transport                 chosenTransport = null;     // TransportOffer offer chosen using the search window
    private Stage                       searchWindow = null;        // Window used to display a list of the available accommodations/transports
    private VBox                        availableElements = null;   // Vbox used in the search window to contain the available accommodations/transports

    // Fields for the user request
    @FXML protected Text                announcementOwnerText;
    @FXML protected TextArea            announcementDescriptionTextarea;
    @FXML protected Text                requestedDestinationText;
    @FXML protected Text                requestedDepartureDateText;
    @FXML protected Text                requestedReturnDateText;
    @FXML protected Text                requestedAccommodationTypeText;
    @FXML protected HBox                requestedAccommodationQualityHbox;
    protected final QualityIndicator    requestedAccommodationQuality = new QualityIndicator(0);
    @FXML protected Text                requestedNumOfRoomsText;
    @FXML protected Text                requestedTransportTypeText;
    @FXML protected HBox                requestedTransportQualityHbox;
    protected final QualityIndicator    requestedTransportQuality = new QualityIndicator(0);
    @FXML protected Text                requestedNumOfTravelersText;
    @FXML protected Text                requestedDepartureLocationText;
    @FXML protected Text                availableBudgetText;

    // Fields for the travel agency offer
    @FXML protected TextField           offeredDestinationTextfield;
    @FXML protected DatePicker          offeredDepartureDatePicker;
    @FXML protected DatePicker          offeredReturnDatePicker;
    @FXML protected Text                offeredAccommodationTypeText;
    @FXML protected Text                offeredAccommodationNameText;
    @FXML protected HBox                offeredAccommodationQualityHbox;
    protected final QualityIndicator    offeredAccommodationQuality = new QualityIndicator(0);
    @FXML protected Text                offeredAccommodationAddressText;
    @FXML protected TextField           offeredNumOfRoomsTextfield;
    @FXML protected Text                offeredAccommodationPriceText;
    @FXML protected Text                offeredTransportTypeText;
    @FXML protected Text                offeredTransportCompanyNameText;
    @FXML protected HBox                offeredTransportQualityHbox;
    protected final QualityIndicator    offeredTransportQuality = new QualityIndicator(0);
    @FXML protected TextField           offeredDepartureLocationTextfield;
    @FXML protected Text                offeredTransportPriceText;
    @FXML protected TextField           offeredNumOfTravelersTextfield;
    @FXML protected Text                offeredPriceText;

    /* This variable is used in the onDepartureDateChange and onReturnDateChange callbacks, is needed to avoid getting stuck in
     * a loop indeed such callbacks can change the value of the property they are related to and when that happens the callback is invoked again.
     * To avoid this we set and check the value of this variable every time a change happens*/
    private boolean                     dataUpdatedOnCallback = false;


    public BaseMakeOfferGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    // Set callbacks for the offer fields and inserts the quality indicators in their relative hboxes,
    // this method must be called by a derived class in the (fxml) initialize method
    protected void initializeGui()
    {
        this.offeredDestinationTextfield.textProperty().addListener((ov, oldVal, newVal) -> onDestinationChanged(oldVal));
        this.offeredNumOfRoomsTextfield.textProperty().addListener((ov, oldVal, newVal) -> onNumOfRoomsChanged(oldVal));
        this.offeredNumOfTravelersTextfield.textProperty().addListener((ov, oldVal, newVal) -> onNumOfTravelersChanged(oldVal));
        this.offeredDepartureLocationTextfield.textProperty().addListener((ov, oldVal, newVal) -> onDepartureLocationChanged(oldVal));
        this.offeredDepartureDatePicker.valueProperty().addListener((ov, oldVal, newVal) -> onDepartureDateChanged(oldVal));
        this.offeredReturnDatePicker.valueProperty().addListener((ov, oldVal, newVal) -> onReturnDateChanged(oldVal));

        this.requestedAccommodationQualityHbox.getChildren().add(this.requestedAccommodationQuality);
        this.requestedTransportQualityHbox.getChildren().add(this.requestedTransportQuality);

        this.offeredAccommodationQualityHbox.getChildren().add(this.offeredAccommodationQuality);
        this.offeredTransportQualityHbox.getChildren().add(this.offeredTransportQuality);
        updateOfferPrice();
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
            if(showConfirmDialog("If you change the destination then the selected accommodation/transport will be lost.") == ButtonType.OK)
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
            if(showConfirmDialog("If you change the number of rooms then the selected accommodation will be lost.") == ButtonType.OK)
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
            if(showConfirmDialog("If you change the number of travelers then the selected transport will be lost.") == ButtonType.OK)
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
            if(showConfirmDialog("If you change the departure location then the selected transport will be lost.") == ButtonType.OK)
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
            if(showConfirmDialog("If you change the departure date then the selected accommodation/transport will be lost.") == ButtonType.OK)
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
            if(showConfirmDialog("If you change the return date then the selected accommodation/transport will be lost.") == ButtonType.OK)
            {
                setChosenAccommodation(null);
                setChosenTransport(null);
            } else {
                dataUpdatedOnCallback = true;
                offeredReturnDatePicker.valueProperty().set(oldVal);
            }
        }
    }


    // Invoked when the "search accommodation" button is clicked, creates a window in which the available accommodations will be displayed
    public void onSearchAccommodationClick()
    {
        int numOfRooms = 0;
        Duration checkInOutDates = null;
        String destination = offeredDestinationTextfield.getText();
        String numOfRoomsAsStr = offeredNumOfRoomsTextfield.getText();
        List<Accommodation> availableAccommodations = null;

        try {
            if(destination == null || destination.isBlank())
                throw new IllegalArgumentException("Please insert a destination to get the available accommodations.");

            if(numOfRoomsAsStr == null || numOfRoomsAsStr.isBlank())
                throw new IllegalArgumentException("Please insert the number of rooms required to get the available accommodations.");

            numOfRooms = Integer.parseInt(numOfRoomsAsStr);
            if(numOfRooms <= 0)
                throw new IllegalArgumentException("The number of rooms required cannot be negative or zero.");

            checkInOutDates = new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue());
            createSearchWindow("Accommodation selector", "Available accommodations", 550, 480);

            // Get list of the available accommodations and display it in the search window
            OfferManager offerManager = new OfferManager();
            availableAccommodations = offerManager.getAvailableAccommodations(destination, checkInOutDates, numOfRooms);

        } catch(IllegalArgumentException e)                     // Some parameter is not set or is invalid
        {
            showErrorDialog(e.getMessage());
            return;
        } catch(IllegalStateException e)                        // The search window is already open
        {
            return;
        } catch(ApiException e)                                 // Errors during the retrieval of the available  accommodations
        {
            showErrorDialog(e.getMessage());
        }

        // Creates graphic elements for the accommodation offers
        if(availableAccommodations == null || availableAccommodations.isEmpty())
        {
            Label infoMsg = new Label("No accommodation has been found.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(DEFAULT_FONT);
            availableElements.getChildren().add(infoMsg);
        } else {
            for(Accommodation el : availableAccommodations)
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
    public void setChosenAccommodation(Accommodation accommodation)
    {
        chosenAccommodation = accommodation;
        if(accommodation == null)
        {
            offeredAccommodationTypeText.setText(NOT_SELECTED_YET_MSG);
            offeredAccommodationNameText.setText(NOT_SELECTED_YET_MSG);
            offeredAccommodationQuality.setQualityLevel(0);
            offeredAccommodationAddressText.setText(NOT_SELECTED_YET_MSG);
            offeredNumOfRoomsTextfield.setPromptText("Num. of rooms");
            offeredAccommodationPriceText.setText("0€");
        } else {
            offeredAccommodationTypeText.setText(accommodation.getType());
            offeredAccommodationNameText.setText(accommodation.getName());
            offeredAccommodationQuality.setQualityLevel(accommodation.getQuality());
            offeredAccommodationAddressText.setText(accommodation.getAddress());
            offeredAccommodationPriceText.setText(accommodation.getPriceAsStr());
        }
        updateOfferPrice();
    }


    // Invoked when the "search transport" button is clicked, creates a window in which the available transports will be displayed
    public void onSearchTransportClick()
    {
        int numOfTravelers = 0;
        Duration departureAndReturnDates = null;
        List<Transport> availableTransports = null;
        String destination = offeredDestinationTextfield.getText();
        String departureLocation = offeredDepartureLocationTextfield.getText();
        String numOfTravelersAsStr = offeredNumOfTravelersTextfield.getText();

        try {
            if(destination == null || destination.isEmpty())
                throw new IllegalArgumentException("Please insert a destination to get the available transports.");

            if(departureLocation == null || departureLocation.isEmpty())
                throw new IllegalArgumentException("Please insert a departure location to get the available transports.");

            if(numOfTravelersAsStr == null || numOfTravelersAsStr.isEmpty())
                throw new IllegalArgumentException("Please insert the number of travelers to get the available transports.");

            numOfTravelers = Integer.parseInt(numOfTravelersAsStr);
            if(numOfTravelers <= 0)
                throw new IllegalArgumentException("The number of travelers cannot be negative or zero.");

            departureAndReturnDates = new Duration(offeredDepartureDatePicker.getValue(), offeredReturnDatePicker.getValue());
            createSearchWindow("Transport selector", "Available transports", 550, 480);

            // Get list of the available transports and display it in the search window
            OfferManager offerManager = new OfferManager();
            availableTransports = offerManager.getAvailableTransports(departureLocation, destination, departureAndReturnDates, numOfTravelers);

        } catch(IllegalArgumentException e)                     // Some parameter is not set or is invalid
        {
            showErrorDialog(e.getMessage());
            return;
        } catch(IllegalStateException e)                        // The search window is already open
        {
            return;
        } catch(ApiException e)                                 // Errors during the retrieval of the available transports
        {
            showErrorDialog(e.getMessage());
        }

        if(availableTransports == null || availableTransports.isEmpty())
        {
            Label infoMsg = new Label("No transport has been found.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(DEFAULT_FONT);
            availableElements.getChildren().add(infoMsg);
        } else {
            for (Transport el : availableTransports) {
                availableElements.getChildren().add(new TransportOfferGfxElement(el, e -> {
                            setChosenTransport(el);
                            destroySearchWindow();
                        })
                );
            }
        }
    }


    // Sets the given transport offer as the chosen one, updates UI and closes the search window
    public void setChosenTransport(Transport transport)
    {
        chosenTransport = transport;
        if(transport == null)
        {
            offeredTransportTypeText.setText(NOT_SELECTED_YET_MSG);
            offeredTransportCompanyNameText.setText(NOT_SELECTED_YET_MSG);
            offeredTransportQuality.setQualityLevel(0);
            offeredDepartureLocationTextfield.setPromptText("Departure from");
            offeredNumOfTravelersTextfield.setPromptText("Num. of travelers");
            offeredTransportPriceText.setText("0€");
        } else {
            offeredTransportTypeText.setText(transport.getType());
            offeredTransportCompanyNameText.setText(transport.getCompanyName());
            offeredTransportQuality.setQualityLevel(transport.getQuality());
            offeredTransportPriceText.setText(transport.getPricePerTravelerAsStr());
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
        screenTitleText.setFont(DEFAULT_FONT);

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
                showErrorDialog("Error during update of the offer price:\n" + e.getMessage());
            }
        }

        this.offeredPriceText.setText( Integer.toString(currPrice) + '€' );
    }

}
