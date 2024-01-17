package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
            displayError("Change of JavaFx screen failed: " + e.getMessage());
        }
    }


    // Displays an error dialog box with the given error message
    protected void displayError(String errorMsg)
    {
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setContentText(errorMsg);
        msg.showAndWait();
    }
}
