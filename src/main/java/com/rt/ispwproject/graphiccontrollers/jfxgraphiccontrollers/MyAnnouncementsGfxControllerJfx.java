package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.AnnouncementGfxElement;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;


public class MyAnnouncementsGfxControllerJfx {

    @FXML VBox announcementsVbox;
    private final Session currSession;
    private List<Announcement> announcements;


    // Loads all the announcements posted by user and displays them
    public MyAnnouncementsGfxControllerJfx(Session session)
    {
        this.currSession = session;
    }


    // Load the announcements posted by the user and creates an AnnouncementGfxElement for each of them
    @FXML void initialize()
    {
        try {
            AnnouncementManager annManager = new AnnouncementManager();
            announcements = annManager.getMyAnnouncements(currSession);
        } catch(DbException e)
        {
            Alert errorMsg = new Alert(Alert.AlertType.ERROR);
            errorMsg.setContentText(e.getMessage());
            errorMsg.showAndWait();
            announcements.clear();
        }

        if(announcements.isEmpty())
        {
            Label infoMsg = new Label("No announcement has been posted yet.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(new Font("System", 18));
            announcementsVbox.getChildren().add(infoMsg);
        } else {
            for(Announcement el : announcements)
            {
                AnnouncementGfxElement gfxEl = new AnnouncementGfxElement(el, e -> onAnnouncementSelected(el) );
                announcementsVbox.getChildren().add(gfxEl);
            }
        }
    }


    // Invoked when the "create announcements" button is clicked
    public void onCreateAnnouncementClick()
    {
        // TODO: pass to "create announcement screen"
        System.out.println("User: " + currSession.getUsername() + " (" +
                currSession.getEmail() + ") has clicked the create announcement button!");
    }


    // Invoked when an AnnouncementGfxElement is clicked
    public void onAnnouncementSelected(Announcement announcement)
    {
        // TODO: Pass to "announcementDetails" screen
        System.out.println("CLICKED ANNOUNCEMENT WITH DESTINATION: " + announcement.getDestination());
    }

}
