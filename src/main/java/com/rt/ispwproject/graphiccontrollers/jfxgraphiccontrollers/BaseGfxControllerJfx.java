package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

// This class defines some common behaviour for the javafx graphic controllers
public class BaseGfxControllerJfx {

    protected final Session currSession;
    protected final Stage   mainStage;


    public BaseGfxControllerJfx(Session session, Stage stage)
    {
        this.currSession = session;
        this.mainStage = stage;
    }


    // Loads new screen and set it as current
    protected void changeScreen(URL pathToScreen, Callback<Class<?>, Object> gfxControllerFactoryLambda)
    {
        FXMLLoader loader = new FXMLLoader(pathToScreen);

        if(gfxControllerFactoryLambda != null)
            loader.setControllerFactory(gfxControllerFactoryLambda);

        try {
            Scene newScene = new Scene(loader.load(), mainStage.getScene().getWidth(), mainStage.getScene().getHeight());
            mainStage.setScene(newScene);
        } catch(IOException e)
        {
            displayErrorDialog("Change of JavaFX screen failed:\n" + e.getMessage());
        }
    }


    // Displays an error dialog box with the given error message
    protected void displayErrorDialog(String errorMsg)
    {
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setContentText(errorMsg);
        msg.showAndWait();
    }


    // Displays a confirmation dialog box and returns the value selected by the user
    protected ButtonType displayConfirmDialog(String msg)
    {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText(msg);
        confirm.showAndWait();
        return confirm.getResult();
    }


    // Displays an info dialog box
    protected void displayInfoDialog(String msg)
    {
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setContentText(msg);
        confirm.showAndWait();
    }

}
