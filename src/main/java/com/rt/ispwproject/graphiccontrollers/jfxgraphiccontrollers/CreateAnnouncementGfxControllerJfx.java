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

import java.time.LocalDate;
import java.util.List;

public class CreateAnnouncementGfxControllerJfx extends BaseGfxControllerJfx {

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
    private final Session                       currSession;

    // Available values for accommodation and transport types combo boxes
    private List<String>    availableAccommodationTypes = List.of("Not specified", "Hotel");
    private List<String>    availabletransportTypes = List.of("Not specified", "Airplane", "Train", "Ferry", "Bus");

    public CreateAnnouncementGfxControllerJfx(Session session)
    {
        this.currSession = session;
    }


    @FXML void initialize()
    {
        // Create QualitySelector widgets for the accommodation and the transport quality fields
        accommodationQualityHbox.getChildren().add(accommodationQualitySelector);
        transportQualityHbox.getChildren().add(transportQualitySelector);

        // Set available values for accommodation and transport type combo boxes
        accommodationTypeCombobox.getItems().addAll(availableAccommodationTypes);
        transportTypeCombobox.getItems().addAll(availabletransportTypes);
    }


    // Invoked when the "my announcements" button is clicked, switches to the "my announcements" screen
    public void onMyAnnouncementsClick()
    {
        changeScreen(getClass().getResource("user/myAnnouncementsScreen.fxml"),
                (Stage) destinationTextfield.getScene().getWindow(), c -> new MyAnnouncementsGfxControllerJfx(currSession));
    }


    // Invoked when the "post announcement" button is clicked, this method posts a new announcement and returns to the "my announcements" screen
    public void onPostAnnouncementClick()
    {
        try {
            Accommodation accommodationReq = new Accommodation(
                    accommodationTypeCombobox.getValue(),
                    accommodationQualitySelector.getQualityLevel(),
                    Integer.parseInt(numOfRoomsTextfield.getText())
            );

            Transport transportReq = new Transport(
                    transportTypeCombobox.getValue(),
                    transportQualitySelector.getQualityLevel(),
                    departureFromTextfield.getText(),
                    Integer.parseInt(numOfTravelersTextfield.getText())
            );

            Announcement announce = new Announcement();
            announce.setOwner(currSession.getUsername());
            announce.setDestination(destinationTextfield.getText());
            announce.setHolidayDescription(descriptionTextarea.getText());
            announce.setAvailableBudget( Integer.parseInt(availableBudgetTextfield.getText()) );
            announce.setDateOfPost(LocalDate.now());
            announce.setHolidayDuration( new Duration(departureDatePicker.getValue(), returnDatePicker.getValue()) );
            announce.setAccommodationRequirements(accommodationReq);
            announce.setTransportRequirements(transportReq);

            AnnouncementManager annManager = new AnnouncementManager();
            annManager.postAnnouncement(currSession, announce);

            changeScreen(getClass().getResource("user/myAnnouncementsScreen.fxml"),
                    (Stage) destinationTextfield.getScene().getWindow(), c -> new MyAnnouncementsGfxControllerJfx(currSession));
        } catch(NumberFormatException e)
        {
            displayErrorDialog("Available budget, number of rooms and number of travelers are empty or contains letters!");
        } catch(IllegalArgumentException | DbException e)
        {
            displayErrorDialog(e.getMessage());
        }
    }

}
