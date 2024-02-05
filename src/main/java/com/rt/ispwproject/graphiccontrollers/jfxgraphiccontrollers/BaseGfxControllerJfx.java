package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

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


    // Loads new screen and set it as current
    protected void changeScreen(URL pathToScreen, Stage stage, Callback<Class<?>, Object> gfxControllerFactoryLambda)
    {
        FXMLLoader loader = new FXMLLoader(pathToScreen);

        if(gfxControllerFactoryLambda != null)
            loader.setControllerFactory(gfxControllerFactoryLambda);

        try {
            Scene newScene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(newScene);
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

}
