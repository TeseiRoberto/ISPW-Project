package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.beans.Session;


public abstract class BaseGfxControllerCmd {

    public static BaseGfxControllerCmd  currGfxCtrl = null;
    protected final Session             currSession;


    public BaseGfxControllerCmd(Session session)
    {
        this.currSession = session;
    }


    public abstract void start();


    // Updates the current graphics controller with the given one
    public static void changeScreen(BaseGfxControllerCmd newGfxCtrl)
    {
        currGfxCtrl = newGfxCtrl;
    }
}
