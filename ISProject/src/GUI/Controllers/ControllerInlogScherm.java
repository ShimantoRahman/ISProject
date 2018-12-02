package GUI.Controllers;

import GUI.AlertBox;
import SysteemKlasses.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ControllerInlogScherm implements Initializable {

    @FXML
    private TextField RRNummerOuder;

    private Stage primaryStage;
    private Scene scene;
    private ControllerDashboard dashboard;
    private ControllerAdminLoginIn adminLoginIn;
    private HashMap<String, Student> studenten;
    private HashMap<String, Ouder> ouders;
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private ArrayList<School> scholen;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setDashboard(ControllerDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public void setAdminLoginIn(ControllerAdminLoginIn adminLoginIn) {
        this.adminLoginIn = adminLoginIn;
    }

    public void setWaarden(Stage primaryStage, HashMap<String, Student> studenten, HashMap<String, Ouder> ouders,
                           HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen, ArrayList<School> scholen) {
        this.primaryStage = primaryStage;
        this.studenten = studenten;
        this.ouders = ouders;
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        this.scholen = scholen;
    }

    public void logInBtnPressed() {
        String rijksregisternummer = RRNummerOuder.getText();
        if(ouders.containsKey(rijksregisternummer)) {
            dashboard.setOuder(ouders.get(rijksregisternummer));
            RRNummerOuder.setText("");
            primaryStage.setScene(dashboard.getScene());
        } else {
            AlertBox alertBox = new AlertBox("Fout Rijksregisternummer","Fout Rijksregisternummer");
            alertBox.show();
        }
    }

    public void logInAlsAdminPressed() {
        RRNummerOuder.setText(null);
        primaryStage.setScene(adminLoginIn.getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
