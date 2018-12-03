package GUI.Controllers;

import GUI.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAdminLoginIn implements Initializable{

    // FXML variabelen
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInBtn;
    @FXML
    private Button terugBtn;

    // instantie variabelen
    private Stage primaryStage;
    private Scene scene;
    private ControllerUserLogIn userLogIn;
    private ControllerAdminPanel adminPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                logInBtnPressed();
            if (event.getCode().equals(KeyCode.ESCAPE))
                terugBtnPressed();
        });
        logInBtn.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                logInBtnPressed();
            if (event.getCode().equals(KeyCode.ESCAPE))
                terugBtnPressed();
        });
        terugBtn.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                logInBtnPressed();
            if (event.getCode().equals(KeyCode.ESCAPE))
                terugBtnPressed();
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FXML/AdminPanel.fxml"));
            Scene sceneAdminPanel = new Scene(loader.load());
            adminPanel = loader.getController();
            adminPanel.setScene(sceneAdminPanel);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }


    // getters & setters
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.adminPanel.setPrimaryStage(primaryStage);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setUserLogIn(ControllerUserLogIn userLogIn) {
        this.userLogIn = userLogIn;
    }

    public ControllerAdminPanel getAdminPanel() {
        return adminPanel;
    }

    // event handlers
    public void logInBtnPressed() {
        if(passwordField.getText().equals("root")) {
            passwordField.setText("");
            primaryStage.setScene(adminPanel.getScene());
        } else {
            AlertBox alertBox = new AlertBox("Error", "Paswoord incorrect");
            alertBox.show();
        }
    }

    public void terugBtnPressed() {
        passwordField.setText("");
        primaryStage.setScene(userLogIn.getScene());
    }

}
