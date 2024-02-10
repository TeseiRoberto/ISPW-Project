package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.DetailsBannerGfxElement;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;


public class MyAnnouncementsGfxControllerJfx extends BaseGfxControllerJfx {

    @FXML VBox                      announcementsVbox;
    private final Session           currSession;
    private List<Announcement>      announcements;


    // Loads all the announcements posted by user and displays them
    public MyAnnouncementsGfxControllerJfx(Session session)
    {
        this.currSession = session;
    }


    // Load the announcements posted by the user and creates a DetailsBannerGfxElement for each of them
    @FXML void initialize()
    {
        try {
            AnnouncementManager annManager = new AnnouncementManager();
            announcements = annManager.getMyAnnouncements(currSession);
        } catch(DbException | IllegalCallerException | IllegalArgumentException e)
        {
            displayErrorDialog(e.getMessage());
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
                DetailsBannerGfxElement gfxEl = new DetailsBannerGfxElement(el, e -> onAnnouncementSelected(el), false);
                announcementsVbox.getChildren().add(gfxEl);
            }
        }
    }


    // Invoked when the "create announcements" button is clicked, switches to the "create announcement" screen
    public void onCreateAnnouncementClick()
    {
        changeScreen(getClass().getResource("user/createAnnouncementScreen.fxml"),
                (Stage) announcementsVbox.getScene().getWindow(), c -> new CreateAnnouncementGfxControllerJfx(currSession));
    }


    // Invoked when a DetailsBannerGfxElement is clicked, switches to the "announcement details" screen
    public void onAnnouncementSelected(Announcement announce)
    {
        changeScreen(getClass().getResource("user/announcementDetailsScreen.fxml"),
                (Stage) announcementsVbox.getScene().getWindow(), c -> new AnnouncementDetailsGfxControllerJfx(currSession, announce));
    }

}
