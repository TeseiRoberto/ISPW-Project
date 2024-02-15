package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualityIndicator;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class AnnouncementDetailsGfxControllerJfx extends BaseSimpleUserGfxControllerJfx {

    private final Announcement      currAnnounce;
    List<Offer>                     offers;                     // Offers received for the currAnnounce
    private int                     offerIndex;                 // Index in the offers list for the offer that is currently being shown

    @FXML private VBox              mainContainerVbox;
    @FXML private Text              announceDateOfPostText;
    @FXML private Text              announceNumOfViewsText;
    @FXML private TextArea          announceDescriptionTextarea;

    // Fields for the user request
    @FXML private Text              requestedDestinationText;
    @FXML private Text              availableBudgetText;
    @FXML private Text              requestedDepartureDateText;
    @FXML private Text              requestedReturnDateText;
    @FXML private Text              requestedAccommodationTypeText;
    @FXML private HBox              requestedAccommodationQualityHbox;
    @FXML private Text              requestedNumOfRoomsText;
    @FXML private Text              requestedTransportTypeText;
    @FXML private HBox              requestedTransportQualityHbox;
    @FXML private Text              requestedDepartureLocationText;
    @FXML private Text              requestedNumOfTravelersText;

    // Note that this vbox gets deleted if no offer is found during initialization, so the offered* fields become all invalid.
    // It is easier to delete the elements when their not needed than to build them from scratch when we need them
    @FXML private VBox              offerContainerVbox;

    // Fields for the travel agency offer

    @FXML private Text              bidderAgencyNameText;
    @FXML private Text              offerStatusText;

    @FXML private Text              offeredDestinationText;
    @FXML private Text              offeredPriceText;
    @FXML private Text              offeredDepartureDateText;
    @FXML private Text              offeredReturnDateText;
    @FXML private Text              offeredAccommodationTypeText;
    @FXML private Text              offeredAccommodationNameText;
    @FXML private HBox              offeredAccommodationQualityHbox;
    private final QualityIndicator  offeredAccommodationQuality = new QualityIndicator(0);
    @FXML private Text              offeredNumOfRoomsText;
    @FXML private Text              offeredAccommodationAddressText;
    @FXML private ImageView         accommodationImageView;
    private int                     accommodationImageIndex = 0; // Index in the accommodation images list (of the current offer) for the image that is currently being shown
    @FXML private Text              offeredTransportTypeText;
    @FXML private Text              offeredTransportCompanyNameText;
    @FXML private HBox              offeredTransportQualityHbox;
    private final QualityIndicator  offeredTransportQuality = new QualityIndicator(0);
    @FXML private Text              offeredDepartureLocationText;
    @FXML private Text              offeredNumOfTravelersText;


    public AnnouncementDetailsGfxControllerJfx(Session session, Stage stage, Announcement announce)
    {
        super(session, stage);
        this.offerIndex = 0;
        this.currAnnounce = announce;
    }


    // Loads the offer received for the current announcement and sets the request and offer fields
    @FXML public void initialize()
    {
        setRequestFields();

        // Insert quality indicators for the offer in their hboxes
        offeredAccommodationQualityHbox.getChildren().add(offeredAccommodationQuality);
        offeredTransportQualityHbox.getChildren().add(offeredTransportQuality);

        try {
            OfferManager offerManager = new OfferManager();
            offers = offerManager.getOffersForAnnouncement(currSession, currAnnounce);

            if(offers == null || offers.isEmpty())
                insertNoOfferReceivedMessage();
            else
                setOfferFields(offers.get(offerIndex));
        } catch (DbException | IllegalArgumentException e)
        {
            offers.clear();
            displayErrorDialog(e.getMessage());
        }
    }


    // Fills in the fields of the user request with the current announcement data
    private void setRequestFields()
    {
        announceDateOfPostText.setText(currAnnounce.getDateOfPost().toString());
        announceNumOfViewsText.setText( Integer.toString(currAnnounce.getNumOfViews()) );
        announceDescriptionTextarea.setText(currAnnounce.getHolidayDescription());

        requestedDestinationText.setText(currAnnounce.getDestination());
        availableBudgetText.setText(currAnnounce.getAvailableBudgetAsStr());
        requestedDepartureDateText.setText(currAnnounce.getHolidayDuration().getDepartureDate().toString());
        requestedReturnDateText.setText(currAnnounce.getHolidayDuration().getReturnDate().toString());
        requestedAccommodationTypeText.setText(currAnnounce.getAccommodationRequirements().getType());
        requestedNumOfRoomsText.setText( Integer.toString(currAnnounce.getAccommodationRequirements().getNumOfRooms()) );
        requestedTransportTypeText.setText(currAnnounce.getTransportRequirements().getType());
        requestedDepartureLocationText.setText(currAnnounce.getTransportRequirements().getDepartureLocation());
        requestedNumOfTravelersText.setText( Integer.toString(currAnnounce.getTransportRequirements().getNumOfTravelers()) );

        QualityIndicator requestedAccommodationQuality = new QualityIndicator(currAnnounce.getAccommodationRequirements().getQuality());
        QualityIndicator requestedTransportQuality = new QualityIndicator(currAnnounce.getTransportRequirements().getQuality());
        requestedAccommodationQualityHbox.getChildren().add(requestedAccommodationQuality);
        requestedTransportQualityHbox.getChildren().add(requestedTransportQuality);
    }


    // Fills in the fields of the travel agency offer with the current offer data
    private void setOfferFields(Offer offer)
    {
        if(offer == null || offerContainerVbox == null)
            return;

        bidderAgencyNameText.setText(offer.getBidderUsername());
        offerStatusText.setText(offer.getOfferStatus());
        offeredDestinationText.setText(offer.getDestination());
        offeredPriceText.setText(offer.getPriceAsStr());
        offeredDepartureDateText.setText(offer.getDepartureDate().toString());
        offeredReturnDateText.setText(offer.getReturnDate().toString());
        offeredAccommodationTypeText.setText(offer.getAccommodationOffer().getType());
        offeredAccommodationNameText.setText(offer.getAccommodationOffer().getName());
        offeredNumOfRoomsText.setText( Integer.toString(offer.getAccommodationOffer().getNumOfRooms()) );
        offeredAccommodationAddressText.setText(offer.getAccommodationOffer().getAddress());
        offeredTransportTypeText.setText(offer.getTransportOffer().getType());
        offeredTransportCompanyNameText.setText(offer.getTransportOffer().getCompanyName());
        offeredDepartureLocationText.setText(offer.getTransportOffer().getDepartureLocation());
        offeredNumOfTravelersText.setText( Integer.toString(offer.getTransportOffer().getNumOfTravelers()) );

        offeredAccommodationQuality.setQualityLevel(offer.getAccommodationOffer().getQuality());
        offeredTransportQuality.setQualityLevel(offer.getTransportOffer().getQuality());

        // Set accommodation image
        accommodationImageIndex = 0;
        List<URL> images = offer.getAccommodationOffer().getImagesLinks();
        if(images == null || images.isEmpty())
            setAccommodationImage(null);
        else
            setAccommodationImage(images.get(accommodationImageIndex));
    }


    // Deletes the offerContainerVbox and inserts in the main container a message saying that no offer has been received
    private void insertNoOfferReceivedMessage()
    {
        if(offerContainerVbox == null)
            return;

        Label infoMsg = new Label("No offer has been received yet.");
        infoMsg.setTextAlignment(TextAlignment.CENTER);
        infoMsg.setFont(new Font("System", 18));

        mainContainerVbox.getChildren().remove(offerContainerVbox);
        mainContainerVbox.getChildren().add(infoMsg);
        offerContainerVbox = null;
    }


    // Invoked when the "edit announcement" button is clicked
    public void onEditAnnouncementClick()
    {
        displayErrorDialog("Edit announcement functionality is not available yet...");
    }


    // Invoked when the "delete announcement" button is clicked
    public void onDeleteAnnouncementClick()
    {
        displayErrorDialog("Delete announcement functionality is not available yet...");
    }


    // Invoked when the "close announcement" button is clicked, switches back to the "my announcements" screen
    public void onCloseAnnouncementClick()
    {
        changeScreen(getClass().getResource(MY_ANNOUNCEMENTS_SCREEN_NAME),
                c -> new MyAnnouncementsGfxControllerJfx(currSession, mainStage));
    }


    // Invoked when the "previous offer" button is clicked, decrements the offerIndex and updates the offer fields
    public void onPrevOfferClick()
    {
        if(offers == null || offers.isEmpty())
            return;

        if(offerIndex <= 0)                                     // Wrap around the index
            offerIndex = offers.size();

        offerIndex--;
        setOfferFields(offers.get(offerIndex));
    }


    // Invoked when the "next offer" button is clicked, increments the offerIndex and updates the offer fields
    public void onNextOfferClick()
    {
        if(offers == null || offers.isEmpty())
            return;

        if(offerIndex + 1 >= offers.size())                        // Wrap around the index
            offerIndex = -1;

        offerIndex++;
        setOfferFields(offers.get(offerIndex));
    }


    // Invoked when the "previous accommodation image" button is clicked, updates the accommodation image shown
    public void onPrevAccommodationImageClick()
    {
        if(offers == null || offers.isEmpty())
            return;

        List<URL> images = offers.get(offerIndex).getAccommodationOffer().getImagesLinks();
        if(images == null || images.isEmpty())
            return;

        if(accommodationImageIndex <= 0)                        // Wrap around the index
            accommodationImageIndex = images.size();

        accommodationImageIndex--;
        setAccommodationImage(images.get(accommodationImageIndex));
    }


    // Invoked when the "next accommodation image" button is clicked, updates the accommodation image shown
    public void onNextAccommodationImageClick()
    {
        if(offers == null || offers.isEmpty())
            return;

        List<URL> images = offers.get(offerIndex).getAccommodationOffer().getImagesLinks();
         if(images == null || images.isEmpty())
            return;

         if(accommodationImageIndex + 1 >= images.size())       // Wrap around the index
             accommodationImageIndex = -1;

        accommodationImageIndex++;
        setAccommodationImage(images.get(accommodationImageIndex));
    }


    // Updates the accommodation image currently displayed in the accommodation image view using the
    // offerIndex and the accommodationImageIndex to retrieve the image from the offers list
    public void setAccommodationImage(URL imgUrl)
    {
        if(imgUrl == null)
            imgUrl = getClass().getResource("jfxwidgets/imageNotAvailable.jpg");

        try {
            accommodationImageView.setImage(new Image(imgUrl.toString()));
        } catch(NullPointerException e)
        {
            displayErrorDialog("Could not load accommodation image!");
        }
    }


    // Invoked when the "reject offer" button is clicked, informs travel agency that offer was rejected
    public void onRejectOfferClick()
    {
        if(offers == null || offers.isEmpty())
            return;

        ButtonType res = displayConfirmDialog("Do you really want to reject this offer?");
        if(res != ButtonType.OK)
            return;

        try {
            Offer currOffer = offers.get(offerIndex);

            OfferManager offerManager = new OfferManager();
            offerManager.rejectOffer(currSession, currOffer);

            offers.remove(currOffer);                       // Delete current offer from the offers list
            if(offers.isEmpty())
            {
                offerIndex = 0;
                insertNoOfferReceivedMessage();
            } else {
                offerIndex = (offerIndex + 1) % offers.size();
                setOfferFields(offers.get(offerIndex));
            }

        } catch(IndexOutOfBoundsException e)
        {
            displayErrorDialog("No offer has been selected");
        }
        catch (DbException | IllegalCallerException | IllegalArgumentException e) {
            displayErrorDialog(e.getMessage());
        }
    }


    // Invoked when the "request changes" button is clicked
    public void onRequestChangesClick()
    {
        if(offers == null || offers.isEmpty())
            return;

        Offer currOffer = offers.get(offerIndex);
        changeScreen(getClass().getResource(REQUEST_CHANGES_SCREEN_NAME),
                c -> new RequestChangesGfxControllerJfx(currSession, mainStage, currAnnounce, currOffer));
    }


    // Invoked when the "accept offer" button is clicked
    public void onAcceptOfferClick()
    {
        // TODO: Add implementation...
        System.out.println("ACCEPT OFFER CLICKED");
    }

}
