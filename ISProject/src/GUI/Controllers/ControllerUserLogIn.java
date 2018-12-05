package GUI.Controllers;

import GUI.AlertBox;
import SysteemKlasses.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ControllerUserLogIn implements Initializable {

    // FXML variabelen
    @FXML
    private TextField RRNummerOuder;
    @FXML
    private Button logInBtn;

    // instantie variabelen
    private Stage primaryStage;
    private Scene scene;
    private ControllerDashboard dashboard;
    private ControllerAdminLoginIn adminLoginIn;
    private HashMap<String, Student> studenten;
    private HashMap<String, Ouder> ouders;
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private ArrayList<School> scholen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RRNummerOuder.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                logInBtnPressed();
        });
        logInBtn.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                logInBtnPressed();
        });
    }

    // getters & setters
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

    // event handlers

    public void logInBtnPressed() {
        String rijksregisternummer = RRNummerOuder.getText();
        if(ouders.containsKey(rijksregisternummer)) {
            dashboard.setOuder(ouders.get(rijksregisternummer));
            reset();
            primaryStage.setScene(dashboard.getScene());
        } else {
            AlertBox alertBox = new AlertBox("Fout Rijksregisternummer","Fout Rijksregisternummer");
            alertBox.show();
        }
    }

    public void logInAlsAdminPressed() {
        reset();
        primaryStage.setScene(adminLoginIn.getScene());
    }

    // andere public methoden
    public void reset() {
        RRNummerOuder.setText("");
    }
}
