package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logicControllers.LoginManager;
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
        LoginManager loginMan = new LoginManager();
        Session currSession;

        try {
            currSession = loginMan.login(usernameTextfield.getText(), passwordTextfield.getText());
            onLoginSuccess(currSession);
        } catch(RuntimeException | DbException | IOException e)
        {
            Alert errorMsg = new Alert(Alert.AlertType.ERROR);
            errorMsg.setContentText(e.getMessage());
            errorMsg.showAndWait();
        }
    }


    // Loads new screen according to user role
    private void onLoginSuccess(Session session) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();

        switch (session.getUserRole())
        {
            case UserRole.SIMPLE_USER:
                loader.setControllerFactory(c -> new MyAnnouncementsGfxControllerJfx(session));
                loader.setLocation(getClass().getResource("user/myAnnouncementsScreen.fxml"));
                break;

            case UserRole.TRAVEL_AGENCY:
                // TODO: Create appropriate graphics controller and load appropriate screen!!!
                loader.setControllerFactory(c -> new MyAnnouncementsGfxControllerJfx(session));
                loader.setLocation(getClass().getResource("user/myAnnouncementsScreen.fxml"));
                break;
        }

        Scene newScene = new Scene(loader.load(), 720, 480);
        Stage currStage = (Stage) usernameTextfield.getScene().getWindow();
        currStage.setScene(newScene);
    }
}
