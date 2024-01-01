package com.rt.ispwproject.graphicControllers.jfxGraphicControllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginGfxControllerJfx extends Application {

    @FXML TextField usernameTextfield;
    @FXML PasswordField passwordTextfield;


    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginGfxControllerJfx.class.getResource("common/loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        stage.setTitle("My Holiday!");
        stage.setScene(scene);
        stage.show();
    }


    // Called when login button is clicked
    public void onLoginClick()
    {
        // TODO: add implementation
        System.out.println(usernameTextfield.getText() + " - " + passwordTextfield.getText());
    }
}
