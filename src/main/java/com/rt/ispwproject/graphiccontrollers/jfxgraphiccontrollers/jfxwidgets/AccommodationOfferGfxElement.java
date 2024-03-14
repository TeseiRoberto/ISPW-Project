package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets;

import com.rt.ispwproject.beans.Accommodation;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;


// Widget that implements a clickable element which displays data for an accommodation
public class AccommodationOfferGfxElement extends GridPane {

    private static final int    COL_NUM = 2;
    private static final int    ROW_NUM = 1;
    private static final float  WIDTH = 500.0f;
    private static final float  HEIGHT = 200.0f;
    private static final float  IMG_WIDTH = 250.0f;
    private static final float  IMG_HEIGHT = HEIGHT - 40.0f;


    public AccommodationOfferGfxElement(Accommodation offer, EventHandler<MouseEvent> onClick)
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
        add(createAccommodationImage(offer), 0, 0);
        add(createAccommodationDetails(offer), 1, 0);
    }


    // Creates an image view that displays an image of the accommodation
    private ImageView createAccommodationImage(Accommodation offer)
    {
        Image img = null;

        // Here we should download an image from the given links but for now all the links to images will be path to files
        // in the file system, so we simply load one of the image from the file system
        List<URL> imageLinks = offer.getImagesLinks();
        URL imgUrl = null;

        if(imageLinks == null || imageLinks.isEmpty())
            imgUrl = getClass().getResource("imageNotAvailable.jpg");
        else
            imgUrl = imageLinks.getFirst();

        try {
            img = new Image(imgUrl.toString());
        } catch(NullPointerException e)
        {
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setContentText("Could not load accommodation image!");
            msg.showAndWait();
        }

        ImageView accommodationImg = new ImageView();
        accommodationImg.setFitWidth(IMG_WIDTH);
        accommodationImg.setFitHeight(IMG_HEIGHT);
        accommodationImg.setCache(true);
        accommodationImg.setImage(img);

        return accommodationImg;
    }


    // Creates text elements placed one on top of the other to display the accommodation details
    private VBox createAccommodationDetails(Accommodation offer)
    {
        Text accommodationType = new Text(offer.getType());
        Text accommodationName = new Text(offer.getName());
        Text accommodationAddress = new Text(offer.getAddress());
        Text accommodationPrice = new Text(offer.getPriceAsStr());
        QualityIndicator accommodationQuality = new QualityIndicator(offer.getQuality());

        Font font = new Font("System", 18);
        accommodationType.setFont(font);
        accommodationName.setFont(font);
        accommodationAddress.setFont(font);
        accommodationPrice.setFont(font);

        // Put the accommodation details in a Vbox
        VBox detailsContainer = new VBox();
        detailsContainer.setSpacing(10);
        detailsContainer.setAlignment(Pos.CENTER);
        detailsContainer.setPrefSize(WIDTH / 2, HEIGHT);
        detailsContainer.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        detailsContainer.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        detailsContainer.getChildren().add(accommodationType);
        detailsContainer.getChildren().add(accommodationName);
        detailsContainer.getChildren().add(accommodationAddress);
        detailsContainer.getChildren().add(accommodationQuality);
        detailsContainer.getChildren().add(accommodationPrice);

        return detailsContainer;
    }
}

