package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualitySelector;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// Graphic controller used by the "SIMPLE_USER" to create and post a new announcement
public class CreateAnnouncementGfxControllerJfx extends BaseSimpleUserGfxControllerJfx {

    @FXML private TextArea                      descriptionTextarea;
    @FXML private TextField                     destinationTextfield;
    @FXML private TextField                     availableBudgetTextfield;
    @FXML private DatePicker                    departureDatePicker;
    @FXML private DatePicker                    returnDatePicker;
    @FXML private TextField                     numOfTravelersTextfield;
    @FXML private ComboBox<String>              accommodationTypeCombobox;
    @FXML private HBox                          accommodationQualityHbox;
    private final QualitySelector               accommodationQualitySelector = new QualitySelector(0, 5);
    @FXML private TextField                     numOfRoomsTextfield;
    @FXML private ComboBox<String>              transportTypeCombobox;
    @FXML private HBox                          transportQualityHbox;
    private final QualitySelector               transportQualitySelector = new QualitySelector(0, 5);
    @FXML private TextField                     departureFromTextfield;

    public CreateAnnouncementGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    @FXML void initialize()
    {
        // Create QualitySelector widgets for the accommodation and the transport quality fields
        accommodationQualityHbox.getChildren().add(accommodationQualitySelector);
        transportQualityHbox.getChildren().add(transportQualitySelector);

        // Set available values for accommodation and transport type combo boxes
        accommodationTypeCombobox.getItems().addAll(Accommodation.getAvailableTypes());
        transportTypeCombobox.getItems().addAll(Transport.getAvailableTypes());
    }


    // Invoked when the "post announcement" button is clicked, this method posts a new announcement and returns to the "my announcements" screen
    public void onPostAnnouncementClick()
    {
        int availableBudget = 0;
        int numOfRoomsRequired = 0;
        int numOfTravelers = 0;

        try {
            availableBudget = Integer.parseInt(availableBudgetTextfield.getText());
        } catch (NumberFormatException e)
        {
            showErrorDialog("Available budget is empty or contains letters");
            return;
        }

        try {
            numOfRoomsRequired = Integer.parseInt(numOfRoomsTextfield.getText());
        } catch (NumberFormatException e)
        {
            showErrorDialog("Number of rooms is empty or contains letters");
            return;
        }

        try {
            numOfTravelers = Integer.parseInt(numOfTravelersTextfield.getText());
        } catch (NumberFormatException e)
        {
            showErrorDialog("Number of travelers is empty or contains letters");
            return;
        }

        try {
            Accommodation accommodationReq = new Accommodation(
                    accommodationTypeCombobox.getValue(),
                    accommodationQualitySelector.getQualityLevel(),
                    numOfRoomsRequired
            );

            Transport transportReq = new Transport(
                    transportTypeCombobox.getValue(),
                    transportQualitySelector.getQualityLevel(),
                    departureFromTextfield.getText(),
                    destinationTextfield.getText(),
                    numOfTravelers
            );

            Announcement announce = new Announcement(
                    currSession.getUsername(),
                    descriptionTextarea.getText(),
                    availableBudget,
                    new Duration(departureDatePicker.getValue(), returnDatePicker.getValue()),
                    accommodationReq,
                    transportReq
            );

            AnnouncementManager annManager = new AnnouncementManager();
            annManager.postAnnouncement(currSession, announce);

            changeScreen(getClass().getResource(MY_ANNOUNCEMENTS_SCREEN_NAME),
                    c -> new MyAnnouncementsGfxControllerJfx(currSession, mainStage));
        } catch(IllegalArgumentException | DbException e)
        {
            showErrorDialog(e.getMessage());
        }
    }

}
