package com.rt.ispwproject;

import com.rt.ispwproject.config.GuiType;
import com.rt.ispwproject.graphicControllers.cmdGraphicControllers.LoginGfxControllerCmd;
import com.rt.ispwproject.graphicControllers.jfxGraphicControllers.LoginGfxControllerJfx;
import javafx.application.Application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    final public static String configFileName = "myHoliday.cfg";
    public static GuiType typeOfGui;


    public static void main(String[] args)
    {
        loadConfigurationFile(Main.configFileName);

        if(typeOfGui == GuiType.JAVAFX_GUI)
            Application.launch(LoginGfxControllerJfx.class, args);
        else
            new LoginGfxControllerCmd();
    }


    // Loads the type of gui that the application should use from given configuration file
    public static void loadConfigurationFile(String fileName)
    {
        try {
            InputStream configFile = new FileInputStream(fileName);
            Properties config = new Properties();
            config.load(configFile);

            if(config.getProperty("GUI_TYPE").equals("javafx"))         // Set gui type
                typeOfGui = GuiType.JAVAFX_GUI;
            else
                typeOfGui = GuiType.CMDLINE_GUI;

        } catch(IOException e)
        {
            typeOfGui = GuiType.CMDLINE_GUI;
        }
    }

}