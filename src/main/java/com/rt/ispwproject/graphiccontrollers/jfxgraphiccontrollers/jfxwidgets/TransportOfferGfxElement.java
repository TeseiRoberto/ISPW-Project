package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets;

import com.rt.ispwproject.beans.Transport;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


// Widget that implements a clickable element that displays data for a transport
public class TransportOfferGfxElement extends GridPane {

    private static final int    COL_NUM = 4;
    private static final int    ROW_NUM = 1;
    private static final float  WIDTH = 500.0f;
    private static final float  HEIGHT = 100.0f;


    public TransportOfferGfxElement(Transport offer, EventHandler<MouseEvent> onClick)
    {
        super();
        setStyle("-fx-background-color: #EBEBEB;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-color: #2B2B2B;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-insets: 1px");

        setOnMouseClicked(onClick);

        // Set widget layout
        setPrefSize(WIDTH, HEIGHT);
        setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 10, 10, 10));

        // Set number of rows
        for(int i = 0; i < ROW_NUM; ++i)
        {
            RowConstraints constraints = new RowConstraints();
            constraints.setPercentHeight(HEIGHT / ROW_NUM);
            getRowConstraints().add(constraints);
        }

        // Set number of columns
        for(int i = 0; i < COL_NUM; ++i)
        {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(WIDTH / COL_NUM);
            getColumnConstraints().add(constraints);
        }

        // Creates the elements of the widget
        createTransportDetails(offer);
    }



    // Creates text elements placed one next to the other to display the transport details
    private void createTransportDetails(Transport offer)
    {
        // Create text elements
        Text transportType = new Text(offer.getType().toString());
        Text transportCompanyName = new Text(offer.getCompanyName());
        Text transportPrice = new Text(offer.getPricePerTravelerAsStr() + " per traveler");
        QualityIndicator transportQuality = new QualityIndicator(offer.getQuality());

        // Set transport price text element alignment
        transportPrice.setWrappingWidth(100);
        transportPrice.setTextAlignment(TextAlignment.CENTER);

        // Set text elements font
        Font font = new Font("System", 18);
        transportType.setFont(font);
        transportCompanyName.setFont(font);
        transportPrice.setFont(font);

        // Insert elements in the grid pane
        add(transportType, 0, 0);
        add(transportCompanyName, 1, 0);
        add(transportQuality, 2, 0);
        add(transportPrice, 3, 0);
    }
}


