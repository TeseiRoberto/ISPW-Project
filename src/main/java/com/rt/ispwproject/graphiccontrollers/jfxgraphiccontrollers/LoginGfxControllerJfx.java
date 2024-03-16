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

    @FXML private TextField     usernameTextfield;
    @FXML private PasswordField passwordTextfield;
    private Stage               mainStage;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginGfxControllerJfx.class.getResource("common/loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        stage.setTitle("My Holiday");
        stage.setScene(scene);
        stage.show();
    }


    // Called when login button is clicked
    public void onLoginClick()
    {
        try {
            LoginManager loginManager = new LoginManager();
            Session currSession = loginManager.login(usernameTextfield.getText(), passwordTextfield.getText());
            onLoginSuccess(currSession);

        } catch(IllegalArgumentException | DbException e)
        {
            Alert errorMsg = new Alert(Alert.AlertType.ERROR);
            errorMsg.setContentText(e.getMessage());
            errorMsg.showAndWait();
        }
    }


    // Loads new screen according to user role
    private void onLoginSuccess(Session session)
    {
        FXMLLoader loader = new FXMLLoader();
        mainStage = (Stage) usernameTextfield.getScene().getWindow();

        if (session.getUserRole() == UserRole.SIMPLE_USER)
        {
            loader.setControllerFactory(c -> new MyAnnouncementsGfxControllerJfx(session, mainStage));
            loader.setLocation(getClass().getResource("simpleUser/myAnnouncementsScreen.fxml"));
        } else if (session.getUserRole() == UserRole.TRAVEL_AGENCY)
        {
                loader.setControllerFactory(c -> new SearchAnnouncementsGfxControllerJfx(session, mainStage));
                loader.setLocation(getClass().getResource("travelAgency/searchAnnouncementsScreen.fxml"));
        }

        try {
            Scene currScene = usernameTextfield.getScene();
            Stage currStage = (Stage) currScene.getWindow();
            Scene newScene = new Scene(loader.load(), currScene.getWidth(), currScene.getHeight());
            currStage.setScene(newScene);
        } catch(IOException e)
        {
            Alert errorMsg = new Alert(Alert.AlertType.ERROR);
            errorMsg.setContentText("Cannot load JavaFX screen, " + e.getMessage());
            errorMsg.showAndWait();
        }
    }
}
