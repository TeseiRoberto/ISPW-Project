package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Offer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


// Widget that implements a clickable element that displays some data for an announcement/offer
public class DetailsBannerGfxElement extends HBox {

    private static final Font   BIG_FONT = new Font("System", 18);      // Font used for main details (destination, departure date, return date and budget/price)
    private static final Font   SMALL_FONT = new Font("System", 12);    // Font used for secondary details (announcement owner)
    private static final int    DETAILS_CONTAINER_WIDTH = 620;
    private static final int    DETAILS_CONTAINER_HEIGHT = 80;
    private static final int    NOTIFICATION_CONTAINER_WIDTH = 55;
    private static final int    NOTIFICATION_CONTAINER_HEIGHT = 55;
    private static final Paint  NOTIFICATION_TEXT_COLOR = Paint.valueOf("#FFFFFF");
    private static final Insets NOTIFICATION_INSETS = new Insets(0, 0, 0, NOTIFICATION_CONTAINER_WIDTH);


    public DetailsBannerGfxElement(Announcement announce, EventHandler<MouseEvent> onClick, boolean showOwnerUsername)
    {
        setElementLayout();

        VBox announcementDetailsVbox = createAnnouncementDetails(announce, showOwnerUsername);
        VBox notificationVbox = createNotificationElement(99);

        announcementDetailsVbox.setOnMouseClicked(onClick);
        notificationVbox.setOnMouseClicked(onClick);

        HBox.setMargin(announcementDetailsVbox, NOTIFICATION_INSETS);
        this.getChildren().add(announcementDetailsVbox);
        this.getChildren().add(notificationVbox);

        // TODO: Need to hide the notification element when the given announcement does not have any new offer
    }


    public DetailsBannerGfxElement(Offer offer, EventHandler<MouseEvent> onClick)
    {
        setElementLayout();

        VBox announcementDetailsVbox = createOfferDetails(offer);

        announcementDetailsVbox.setOnMouseClicked(onClick);
        HBox.setMargin(announcementDetailsVbox, NOTIFICATION_INSETS);
        this.getChildren().add(announcementDetailsVbox);

        // If changes has been requested on the offer then show the notification icon
        if(offer.hasRequestOfChanges())
        {
            VBox notificationVbox = createNotificationElement(1);
            notificationVbox.setOnMouseClicked(onClick);
            this.getChildren().add(notificationVbox);
        }
    }


    // Sets the layout properties of this widget
    private void setElementLayout()
    {
        this.setAlignment(Pos.CENTER);
        this.setPrefSize(DETAILS_CONTAINER_WIDTH + NOTIFICATION_CONTAINER_WIDTH, DETAILS_CONTAINER_HEIGHT + NOTIFICATION_CONTAINER_HEIGHT);
        this.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }


    // Creates a vbox that contains the details of the given announcement
    private VBox createAnnouncementDetails(Announcement announce, boolean showOwnerUsername)
    {
        VBox container = createContainer(DETAILS_CONTAINER_WIDTH, DETAILS_CONTAINER_HEIGHT);
        container.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));

        // Set vbox style and layout
        container.setStyle("-fx-background-color: #EBEBEB;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-color: #2B2B2B;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-insets: 1px");

        // Create text elements
        Text destinationText = createText(announce.getDestination(), BIG_FONT);
        Text departureAndReturnText = createText(announce.getHolidayDuration().getDepartureDate().toString() + " - " + announce.getHolidayDuration().getReturnDate().toString(), BIG_FONT);
        Text budgetText = createText(announce.getAvailableBudgetAsStr(), BIG_FONT) ;
        Text dateOfPostText = createText("published on " + announce.getDateOfPost().toString(), SMALL_FONT);
        Text numOfViewsText = createText(announce.getNumOfViews() + " views", SMALL_FONT);

        // Create hboxes and insert text into them
        HBox row1 = new HBox();
        HBox row2 = new HBox();

        row1.setAlignment(Pos.BOTTOM_CENTER);
        row1.setSpacing(80);
        row2.setAlignment(Pos.BOTTOM_RIGHT);
        row2.setSpacing(10);

        row1.getChildren().add(destinationText);
        row1.getChildren().add(departureAndReturnText);
        row1.getChildren().add(budgetText);

        row2.getChildren().add(dateOfPostText);

        if(showOwnerUsername)
        {
            Text ownerText = createText(announce.getOwnerUsername(), SMALL_FONT);
            row2.getChildren().add(ownerText);
        }

        row2.getChildren().add(numOfViewsText);

        // Add hboxes to the vbox
        container.getChildren().add(row1);
        container.getChildren().add(row2);

        return container;
    }


    // Creates a vbox that contains the details of the given offer
    private VBox createOfferDetails(Offer offer)
    {
        VBox container = createContainer(DETAILS_CONTAINER_WIDTH, DETAILS_CONTAINER_HEIGHT);
        container.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));

        // Set vbox style and layout
        container.setStyle("-fx-background-color: #EBEBEB;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-color: #2B2B2B;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-insets: 1px");

        // Create text elements
        Text destinationText = createText(offer.getDestination(), BIG_FONT);
        Text departureAndReturnText = createText(offer.getDepartureDate().toString() + " - " + offer.getReturnDate().toString(), BIG_FONT);
        Text budgetText = createText(offer.getPriceAsStr(), BIG_FONT);

        // Create hboxes and insert text into them
        HBox row = new HBox();

        row.setAlignment(Pos.BOTTOM_CENTER);
        row.setSpacing(80);

        row.getChildren().add(destinationText);
        row.getChildren().add(departureAndReturnText);
        row.getChildren().add(budgetText);

        // Add hbox to the vbox
        container.getChildren().add(row);
        return container;
    }


    // Creates a vbox which indicates that the announcement related to this widget has received offersNum offers
    private VBox createNotificationElement(int notificationNum)
    {
        VBox container = createContainer(NOTIFICATION_CONTAINER_WIDTH, NOTIFICATION_CONTAINER_HEIGHT);

        // Set vbox style and layout
        container.setStyle("-fx-background-color: #E69E0F;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-color: #2B2B2B;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-insets: 1px");

        Label notificationLabel = new Label("+" + notificationNum);
        notificationLabel.setFont(BIG_FONT);
        notificationLabel.setTextFill(NOTIFICATION_TEXT_COLOR);

        container.getChildren().add(notificationLabel);
        return container;
    }


    // Utility method to create vbox
    private VBox createContainer(int width, int height)
    {
        VBox container = new VBox();

        container.setAlignment(Pos.CENTER);
        container.setPrefSize(width, height);
        container.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        container.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        return container;
    }


    // Utility method to create text elements
    private Text createText(String text, Font font)
    {
        Text t = new Text(text);
        t.setFont(font);
        t.setTextAlignment(TextAlignment.CENTER);

        return t;
    }
}