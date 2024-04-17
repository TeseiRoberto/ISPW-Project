package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.beans.Session;


public abstract class BaseGfxControllerCmd {

    protected final Session             currSession;


    protected BaseGfxControllerCmd(Session session)
    {
        this.currSession = session;
    }


    public abstract void start();

}
