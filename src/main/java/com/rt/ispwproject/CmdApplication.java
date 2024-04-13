package com.rt.ispwproject;

import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.BaseGfxControllerCmd;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.LoginGfxControllerCmd;


// This class implements the main loop of the application for the cmd gui mode,
public class CmdApplication {

    private static BaseGfxControllerCmd currGfxController = null;


    // Updates the current graphics controller with the given one
    public static void changeScreen(BaseGfxControllerCmd newGfxController)
    {
        currGfxController = newGfxController;
    }


    // Enables the user to log in and then executes the start method of the current graphic controller
    public static void launch()
    {
        LoginGfxControllerCmd loginCtrl = new LoginGfxControllerCmd();
        loginCtrl.start();

        while(currGfxController != null)
            currGfxController.start();
    }
}
