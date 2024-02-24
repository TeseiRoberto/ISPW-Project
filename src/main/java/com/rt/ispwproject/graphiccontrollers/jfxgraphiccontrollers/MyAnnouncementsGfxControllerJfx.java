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


public class MyAnnouncementsGfxControllerJfx extends BaseSimpleUserGfxControllerJfx {

    @FXML VBox                      announcementsVbox;
    private List<Announcement>      announcements;


    // Loads all the announcements posted by user and displays them
    public MyAnnouncementsGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
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
                DetailsBannerGfxElement gfxEl = new DetailsBannerGfxElement(el, e -> onAnnouncementSelected(el), false, true);
                announcementsVbox.getChildren().add(gfxEl);
            }
        }
    }


    // Invoked when a DetailsBannerGfxElement is clicked, switches to the "announcement details" screen
    public void onAnnouncementSelected(Announcement announce)
    {
        changeScreen(getClass().getResource(ANNOUNCEMENT_DETAILS_SCREEN_NAME),
                c -> new AnnouncementDetailsGfxControllerJfx(currSession, mainStage, announce));
    }

}
