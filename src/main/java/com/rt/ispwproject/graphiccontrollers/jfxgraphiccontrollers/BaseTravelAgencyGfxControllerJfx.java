package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import javafx.stage.Stage;

// Implements callbacks that are common to all the graphic controllers classes related to the "TRAVEL_AGENCY" user type
public class BaseTravelAgencyGfxControllerJfx extends BaseGfxControllerJfx {

    public BaseTravelAgencyGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    // Invoked when the "search announcements" button is clicked, switches to the "search announcements" screen
    public void onSearchAnnouncementsClick()
    {
        changeScreen(getClass().getResource(SEARCH_ANNOUNCEMENTS_SCREEN_NAME),
                e -> new SearchAnnouncementsGfxControllerJfx(currSession, mainStage));
    }


    // Invoked when the "my offers" button is clicked, switches to the "my offers" screen
    public void onMyOffersClick()
    {
        changeScreen(getClass().getResource(MY_OFFERS_SCREEN_NAME),
                c -> new MyOffersGfxControllerJfx(currSession, mainStage));
    }

}