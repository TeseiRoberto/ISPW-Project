package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.LoginManager;
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
        } catch(IllegalArgumentException | DbException e)
        {
            Alert errorMsg = new Alert(Alert.AlertType.ERROR);
            errorMsg.setContentText(e.getMessage());
            errorMsg.showAndWait();
        }
    }


    // Loads new screen according to user role
    private void onLoginSuccess(Session session) throws RuntimeException
    {
        FXMLLoader loader = new FXMLLoader();

        if (session.getUserRole() == UserRole.SIMPLE_USER)
        {
            loader.setControllerFactory(c -> new MyAnnouncementsGfxControllerJfx(session));
            loader.setLocation(getClass().getResource("user/myAnnouncementsScreen.fxml"));
        } else if (session.getUserRole() == UserRole.TRAVEL_AGENCY)
        {
                loader.setControllerFactory(c -> new SearchAnnouncementsGfxControllerJfx(session));
                loader.setLocation(getClass().getResource("travelAgency/searchAnnouncementsScreen.fxml"));
        }

        try {
            Scene currScene = usernameTextfield.getScene();
            Stage currStage = (Stage) currScene.getWindow();
            Scene newScene = new Scene(loader.load(), currScene.getWidth(), currScene.getHeight());
            currStage.setScene(newScene);
        } catch(IOException e)
        {
            throw new RuntimeException("Change of JavaFx screen failed: " + e.getMessage());
        }
    }
}
