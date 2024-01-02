package com.rt.ispwproject.graphicControllers.jfxGraphicControllers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.logicControllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        LoginController ctrl = new LoginController();
        Session currSession;

        try {
            currSession = ctrl.login(usernameTextfield.getText(), passwordTextfield.getText());
        } catch(RuntimeException e)
        {
            Alert errorMsg = new Alert(Alert.AlertType.ERROR);
            errorMsg.setContentText(e.getMessage());
            errorMsg.showAndWait();
        }
    }
}
