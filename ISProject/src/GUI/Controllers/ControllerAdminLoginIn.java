package GUI.Controllers;

import GUI.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAdminLoginIn implements Initializable{

    @FXML
    PasswordField passwordField;

    private Stage primaryStage;
    private Scene scene;
    private ControllerInlogScherm inlogScherm;
    private ControllerAdminPanel adminPanel;

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

    public void setInlogScherm(ControllerInlogScherm inlogScherm) {
        this.inlogScherm = inlogScherm;
    }

    public ControllerAdminPanel getAdminPanel() {
        return adminPanel;
    }

    public void logInBtnPressed() {
        if(passwordField.getText().equals("root")) {
            passwordField.setText(null);
            primaryStage.setScene(adminPanel.getScene());
        } else {
            AlertBox alertBox = new AlertBox("Error", "Paswoord incorect");
            alertBox.show();
        }
    }

    public void terugBtnPressed() {
        passwordField.setText(null);
        primaryStage.setScene(inlogScherm.getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
}
