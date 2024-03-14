package com.rt.ispwproject;

import com.rt.ispwproject.config.GuiType;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.LoginGfxControllerCmd;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.LoginGfxControllerJfx;
import javafx.application.Application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static final String CONFIG_FILE_NAME = "myHoliday.cfg";
    private static GuiType typeOfGui;


    public static void main(String[] args)
    {
        loadConfigurationFile(Main.CONFIG_FILE_NAME);

        if(typeOfGui == GuiType.JAVAFX_GUI)
        {
            Application.launch(LoginGfxControllerJfx.class, args);
        } else {
            LoginGfxControllerCmd loginCtrl = new LoginGfxControllerCmd();
            loginCtrl.start();
        }

    }


    // Loads the type of gui that the application should use from given configuration file
    public static void loadConfigurationFile(String fileName)
    {
        try (InputStream configFile = new FileInputStream(fileName))
        {
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