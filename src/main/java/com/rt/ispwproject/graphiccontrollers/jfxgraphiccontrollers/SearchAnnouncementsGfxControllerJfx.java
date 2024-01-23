package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.AnnouncementGfxElement;
import com.rt.ispwproject.logiccontrollers.SearchAnnouncementsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

public class SearchAnnouncementsGfxControllerJfx extends BaseGfxControllerJfx {

    @FXML TextField             searchBarTextfield;
    @FXML VBox                  announcementsVbox;
    private final Session       currSession;
    private List<Announcement>  announcements;
    private static final int    MAX_NUM_OF_ANNOUNCEMENTS_DISPLAYED = 10;


    public SearchAnnouncementsGfxControllerJfx(Session session)
    {
        this.currSession = session;
    }


    // Loads some of the announcements posted by users and displays them
    @FXML void initialize()
    {
        try {
            SearchAnnouncementsManager searchManager = new SearchAnnouncementsManager();
            announcements = searchManager.loadAnnouncements(0, MAX_NUM_OF_ANNOUNCEMENTS_DISPLAYED);
        } catch(DbException e)
        {
            displayError(e.getMessage());
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
                AnnouncementGfxElement gfxEl = new AnnouncementGfxElement(el, e -> onAnnouncementSelected(el) , true);
                announcementsVbox.getChildren().add(gfxEl);
            }
        }
    }


    // Invoked when one of the AnnouncementGfxElement is clicked, switches to the "make offer" screen
    public void onAnnouncementSelected(Announcement announce)
    {
        changeScreen(getClass().getResource("travelAgency/makeOfferScreen.fxml"),
                (Stage) announcementsVbox.getScene().getWindow(), c -> new MakeOfferGfxControllerJfx(currSession, announce));
    }


    // Invoked when the "my offers" button is clicked
    public void onMyOffersClick()
    {
        // TODO: Need to switch to my offers screen
        System.out.println("CLICKED ON MY OFFERS BUTTON!");
    }


    // Invoked when the "search" button is clicked
    public void onSearchClick()
    {
        displayError("Announcements search functionality has not been implemented yet...");
    }


}
