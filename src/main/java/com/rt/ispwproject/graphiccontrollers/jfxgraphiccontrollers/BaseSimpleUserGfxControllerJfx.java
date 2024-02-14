package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import javafx.stage.Stage;


// Implements callbacks that are common to all the graphic controllers classes related to the "SIMPLE_USER" user type
public class BaseSimpleUserGfxControllerJfx extends BaseGfxControllerJfx {


    public BaseSimpleUserGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    // Invoked when the "create announcements" button is clicked, switches to the "create announcement" screen
    public void onCreateAnnouncementClick()
    {
        changeScreen(getClass().getResource(CREATE_ANNOUNCEMENT_SCREEN_NAME),
                c -> new CreateAnnouncementGfxControllerJfx(currSession, mainStage));
    }


    // Invoked when the "my announcements" button is clicked, switches to the "my announcements" screen
    public void onMyAnnouncementsClick()
    {
        changeScreen(getClass().getResource(MY_ANNOUNCEMENTS_SCREEN_NAME),
                c -> new MyAnnouncementsGfxControllerJfx(currSession, mainStage));
    }

}
