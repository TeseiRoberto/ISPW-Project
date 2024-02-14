package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.DetailsBannerGfxElement;
import com.rt.ispwproject.logiccontrollers.SearchAnnouncementsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

public class SearchAnnouncementsGfxControllerJfx extends BaseTravelAgencyGfxControllerJfx {

    @FXML TextField             searchBarTextfield;
    @FXML VBox                  announcementsVbox;
    private List<Announcement>  announcements;
    private static final int    MAX_NUM_OF_ANNOUNCEMENTS_DISPLAYED = 10;


    public SearchAnnouncementsGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    // Loads some of the announcements posted by users and displays them
    @FXML void initialize()
    {
        try {
            SearchAnnouncementsManager searchManager = new SearchAnnouncementsManager();
            announcements = searchManager.loadAnnouncements(currSession, 0, MAX_NUM_OF_ANNOUNCEMENTS_DISPLAYED);
        } catch(DbException | IllegalCallerException | IllegalArgumentException e)
        {
            displayErrorDialog(e.getMessage());
            announcements.clear();
        }

        if(announcements.isEmpty())
        {
            Label infoMsg = new Label("No announcement is currently available.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(new Font("System", 18));
            announcementsVbox.getChildren().add(infoMsg);
        } else {
            for(Announcement el : announcements)
            {
                DetailsBannerGfxElement gfxEl = new DetailsBannerGfxElement(el, e -> onAnnouncementSelected(el) , true);
                announcementsVbox.getChildren().add(gfxEl);
            }
        }
    }


    // Invoked when one of the DetailsBannerGfxElement is clicked, switches to the "make offer" screen
    public void onAnnouncementSelected(Announcement announce)
    {
        changeScreen(getClass().getResource("travelAgency/makeOfferScreen.fxml"),
                c -> new MakeOfferGfxControllerJfx(currSession, mainStage, announce));
    }

}
