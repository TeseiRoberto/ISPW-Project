package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualityIndicator;
import com.rt.ispwproject.logiccontrollers.ChangesManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Graphic controller that display the details of an offer that was made by the "TRAVEL_AGENCY"
// and the details of the changes requested by the "SIMPLE_USER" (if a request of changes was made)
public class OfferDetailsGfxControllerJfx extends BaseTravelAgencyGfxControllerJfx {

    private final Offer         currOffer;
    private ChangesOnOffer      requestedChanges;
    @FXML private VBox          mainContainerVbox;
    @FXML private Text          announcementOwnerUsernameText;
    @FXML private Text          offerStatusText;

    // Fields for the travel agency offer
    @FXML private Text          offeredDestinationText;
    @FXML private Text          offeredPriceText;
    @FXML private Text          offeredDepartureDateText;
    @FXML private Text          offeredReturnDateText;
    @FXML private Text          offeredAccommodationTypeText;
    @FXML private Text          offeredAccommodationNameText;
    @FXML private HBox          offeredAccommodationQualityHbox;
    @FXML private Text          offeredAccommodationAddressText;
    @FXML private Text          offeredNumOfRoomsText;
    @FXML private Text          offeredTransportTypeText;
    @FXML private Text          offeredTransportCompanyNameText;
    @FXML private HBox          offeredTransportQualityHbox;
    @FXML private Text          offeredDepartureLocationText;
    @FXML private Text          offeredNumOfTravelersText;

    // Note that this vbox gets deleted if no request of change is found during initialization, so the requested* fields become all invalid
    // It is easier to delete the elements when their not needed than to build them from scratch when we need them
    @FXML private VBox          requestedChangesVbox;

    // Fields for the user requested changes
    @FXML private TextArea      requestedChangesDescriptionTextarea;
    @FXML private Text          requestedDestinationText;
    @FXML private Text          requestedPriceText;
    @FXML private Text          requestedDepartureDateText;
    @FXML private Text          requestedReturnDateText;
    @FXML private Text          requestedAccommodationTypeText;
    @FXML private Text          requestedAccommodationChangeText;
    @FXML private HBox          requestedAccommodationQualityHbox;
    @FXML private Text          requestedNumOfRoomsText;
    @FXML private Text          requestedTransportTypeText;
    @FXML private Text          requestedTransportChangeText;
    @FXML private HBox          requestedTransportQualityHbox;
    @FXML private Text          requestedDepartureLocationText;
    @FXML private Text          requestedNumOfTravelersText;


    public OfferDetailsGfxControllerJfx(Session session, Stage stage, Offer offer)
    {
        super(session, stage);
        this.currOffer = offer;
    }


    // Retrieves request of changes for the current offer (if there is one).
    @FXML public void initialize()
    {
        setOfferFields();

        try {
            ChangesManager changesManager = new ChangesManager();
            requestedChanges = changesManager.getRequestedChangesOnOffer(currSession, currOffer);

            if(requestedChanges == null)
                mainContainerVbox.getChildren().remove(requestedChangesVbox);
            else
                setRequestedChangesFields();
        } catch (DbException | IllegalArgumentException e)
        {
            displayErrorDialog(e.getMessage());
        }

        announcementOwnerUsernameText.setText(currOffer.getRelativeAnnouncementOwnerUsername());
        offerStatusText.setText(currOffer.getOfferStatus());
    }


    // Fills in the fields of the travel agency offer with the current offer data
    private void setOfferFields()
    {
        offeredDestinationText.setText(currOffer.getDestination());
        offeredPriceText.setText(currOffer.getPriceAsStr());
        offeredDepartureDateText.setText(currOffer.getDepartureDate().toString());
        offeredReturnDateText.setText(currOffer.getReturnDate().toString());
        offeredAccommodationTypeText.setText(currOffer.getAccommodationOffer().getType());
        offeredAccommodationNameText.setText(currOffer.getAccommodationOffer().getName());
        offeredAccommodationAddressText.setText(currOffer.getAccommodationOffer().getAddress());
        offeredNumOfRoomsText.setText( Integer.toString(currOffer.getAccommodationOffer().getNumOfRooms()) );
        offeredTransportTypeText.setText(currOffer.getTransportOffer().getType());
        offeredTransportCompanyNameText.setText(currOffer.getTransportOffer().getCompanyName());
        offeredDepartureLocationText.setText(currOffer.getTransportOffer().getDepartureLocation());
        offeredNumOfTravelersText.setText( Integer.toString(currOffer.getTransportOffer().getNumOfTravelers()) );

        QualityIndicator offeredAccommodationQuality = new QualityIndicator(currOffer.getAccommodationOffer().getQuality());
        QualityIndicator offeredTransportQuality = new QualityIndicator(currOffer.getTransportOffer().getQuality());

        offeredAccommodationQualityHbox.getChildren().add(offeredAccommodationQuality);
        offeredTransportQualityHbox.getChildren().add(offeredTransportQuality);
    }


    // Fills in the fields of the changes requested by the user
    private void setRequestedChangesFields()
    {
        if(requestedChangesVbox == null || requestedChanges == null)
            return;

        requestedChangesDescriptionTextarea.setText(requestedChanges.getChangesDescription());

        if(requestedChanges.isPriceChangeRequired())
            requestedPriceText.setText(Integer.toString(requestedChanges.getPrice()));
        else
            requestedPriceText.setText("");

        if(requestedChanges.isDurationChangeRequired())
        {
            requestedDepartureDateText.setText(requestedChanges.getHolidayDuration().getDepartureDate().toString());
            requestedReturnDateText.setText(requestedChanges.getHolidayDuration().getReturnDate().toString());
        } else {
            requestedDepartureDateText.setText("");
            requestedReturnDateText.setText("");
        }

        if(requestedChanges.isAccommodationChangeRequired())
        {
            requestedAccommodationTypeText.setText(requestedChanges.getAccommodationChanges().getType());
            requestedAccommodationChangeText.setText("yes");
            requestedNumOfRoomsText.setText(Integer.toString(requestedChanges.getAccommodationChanges().getNumOfRooms()));

            QualityIndicator accommodationQuality = new QualityIndicator(requestedChanges.getAccommodationChanges().getQuality());
            requestedAccommodationQualityHbox.getChildren().add(accommodationQuality);
        } else {
            requestedAccommodationTypeText.setText("");
            requestedAccommodationChangeText.setText("no");
            requestedNumOfRoomsText.setText("");
        }

        if(requestedChanges.isTransportChangeRequired())
        {
            requestedDestinationText.setText(requestedChanges.getTransportChanges().getArrivalLocation());
            requestedTransportTypeText.setText(requestedChanges.getTransportChanges().getType());
            requestedTransportChangeText.setText("yes");
            requestedDepartureLocationText.setText(requestedChanges.getTransportChanges().getDepartureLocation());
            requestedNumOfTravelersText.setText(Integer.toString(requestedChanges.getTransportChanges().getNumOfTravelers()));

            QualityIndicator transportQuality = new QualityIndicator(requestedChanges.getTransportChanges().getQuality());
            requestedTransportQualityHbox.getChildren().add(transportQuality);
        } else {
            requestedDestinationText.setText("");
            requestedTransportTypeText.setText("");
            requestedTransportChangeText.setText("no");
            requestedDepartureLocationText.setText("");
            requestedNumOfTravelersText.setText("");
        }
    }


    // Invoked when the "close offer details" button is clicked, switches to the "my offers" screen
    public void onCloseOfferDetailsClick()
    {
        changeScreen(getClass().getResource(MY_OFFERS_SCREEN_NAME),
                c -> new MyOffersGfxControllerJfx(currSession, mainStage));
    }


    // Invoked when the "reject changes" button is clicked
    public void onRejectChangesClick()
    {
        // TODO: Add implementation
        displayErrorDialog("Reject requested changes functionality is not implemented yet...");
    }


    // Invoked when the "make counteroffer" button is clicked
    public void onMakeCounterOfferClick()
    {
        // TODO: Add implementation
        displayErrorDialog("Make counter offer functionality is not implemented yet...");
    }
}
