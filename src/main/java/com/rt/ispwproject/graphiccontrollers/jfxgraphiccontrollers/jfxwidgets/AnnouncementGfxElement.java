package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets;

import com.rt.ispwproject.beans.Announcement;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


// Widget that implements a clickable element that displays data for an announcement
public class AnnouncementGfxElement extends VBox {


    public AnnouncementGfxElement(Announcement announcement, EventHandler<MouseEvent> onClick)
    {
        // Set vbox style and layout
        this.setStyle("-fx-background-color: #EBEBEB;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-border-color: #2B2B2B;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-insets: 1px");
        this.setAlignment(Pos.CENTER);
        this.prefWidth(520);
        this.prefHeight(70);
        this.maxWidth(USE_COMPUTED_SIZE);
        this.maxHeight(USE_PREF_SIZE);
        this.minWidth(USE_COMPUTED_SIZE);
        this.minHeight(USE_PREF_SIZE);

        // Create labels
        Label destinationText = new Label(announcement.getDestination());
        Label departureAndReturnText = new Label(announcement.getDepartureDate().toString() + " - " + announcement.getReturnDate().toString());
        Label budgetText = new Label( Integer.toString(announcement.getAvailableBudget()) );
        Label dateOfPostText = new Label("published on " + announcement.getDateOfPost().toString());
        Label numOfViewsText = new Label( announcement.getNumOfViews() + " views");

        Font bigFont = new Font("System", 18);
        destinationText.setFont(bigFont);
        departureAndReturnText.setFont(bigFont);
        budgetText.setFont(bigFont);

        Font smallFont = new Font("System", 12);
        dateOfPostText.setFont(smallFont);
        numOfViewsText.setFont(smallFont);

        // Insert labels into rows
        HBox row1 = new HBox();
        HBox row2 = new HBox();

        row1.setAlignment(Pos.BOTTOM_CENTER);
        row1.setSpacing(100);
        row2.setAlignment(Pos.BOTTOM_RIGHT);
        row2.setSpacing(10);

        row1.getChildren().add(destinationText);
        row1.getChildren().add(departureAndReturnText);
        row1.getChildren().add(budgetText);

        row2.getChildren().add(dateOfPostText);
        row2.getChildren().add(numOfViewsText);

        // Add rows to the vbox
        this.getChildren().add(row1);
        this.getChildren().add(row2);

        // Set on click callback
        this.onMouseClickedProperty().setValue(onClick);
    }
}
