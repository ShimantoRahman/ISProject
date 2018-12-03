package GUI.Controllers;

import Logic.IAfstandBerekeningFormule;
import SysteemKlasses.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ControllerDashboard implements Initializable {

    // FXML variabelen
    @FXML
    private VBox mainFrame;
    @FXML
    private Label mainFrameLbl;

    // instantie variabelen
    private Stage primaryStage;
    private Scene scene;
    private ControllerUserLogIn userLogIn;
    private ControllerHomepage homepage;
    private ControllerNieuweAanvraag nieuweAanvraag;
    private ControllerFicheSchool ficheSchool;
    private ControllerOverzichtToewijzingsaanvraag overzichtToewijzingsaanvraag;
    private HashMap<String, Student> studenten;
    private HashMap<String, Ouder> ouders;
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private ArrayList<School> scholen;
    private Ouder ouder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Parent elementen;
            FXMLLoader loader;

            loader = new FXMLLoader(getClass().getResource("/GUI/FXML/Homepage.fxml"));
            elementen = loader.load();
            homepage = loader.getController();
            homepage.setElementen(elementen);

            loader = new FXMLLoader(getClass().getResource("/GUI/FXML/NieuweAanvraag.fxml"));
            elementen = loader.load();
            nieuweAanvraag = loader.getController();
            nieuweAanvraag.setElementen(elementen);

            loader = new FXMLLoader(getClass().getResource("/GUI/FXML/FicheSchool.fxml"));
            elementen = loader.load();
            ficheSchool = loader.getController();
            ficheSchool.setElementen(elementen);

            loader = new FXMLLoader(getClass().getResource("/GUI/FXML/OverzichtToewijzingsaanvraag.fxml"));
            elementen = loader.load();
            overzichtToewijzingsaanvraag = loader.getController();
            overzichtToewijzingsaanvraag.setElementen(elementen);

            updateFrame(homepage.getElementen(), "Homepage");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    // getters & setters
    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setUserLogIn(ControllerUserLogIn userLogIn) {
        this.userLogIn = userLogIn;
    }

    public HashMap<String, Student> getStudenten() {
        return studenten;
    }

    public HashMap<String, Ouder> getOuders() {
        return ouders;
    }

    public HashMap<Integer, Toewijzingsaanvraag> getToewijzingsaanvragen() {
        return toewijzingsaanvragen;
    }

    public ArrayList<School> getScholen() {
        return scholen;
    }

    public Ouder getOuder() {
        return ouder;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
        this.homepage.setOuder(ouder);
        this.nieuweAanvraag.setOuder(ouder);
        this.overzichtToewijzingsaanvraag.setToewijzingsaanvragen(getToewijzingsaanvragenVanOuder());
    }

    public void setWaarden(Stage primaryStage, HashMap<String, Student> studenten, HashMap<String, Ouder> ouders,
                           HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen, ArrayList<School> scholen,
                           IAfstandBerekeningFormule afstandBerekeningFormule) {
        this.primaryStage = primaryStage;
        this.studenten = studenten;
        this.ouders = ouders;
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        this.scholen = scholen;

        this.nieuweAanvraag.setScholen(scholen);
        this.nieuweAanvraag.setStudenten(studenten);
        this.nieuweAanvraag.setToewijzingsaanvragen(toewijzingsaanvragen);
        this.nieuweAanvraag.setAfstandBerekeningFormule(afstandBerekeningFormule);
        this.ficheSchool.setScholen(scholen);
    }

    // event handlers

    // wanneer er op nieuwe aanvraag wordt geclicked dan maken we een nieuwe aanvraag aan
    // wanneer er weg wordt geclicked van nieuwe aanvraag, wordt de aanvraag verwijderd
    public void nieuweAanvraagButtonClicked() {
        Toewijzingsaanvraag toewijzingsaanvraag = new Toewijzingsaanvraag(ouder);
        nieuweAanvraag.setToewijzingsaanvraag(toewijzingsaanvraag);
        updateFrame(nieuweAanvraag.getElementen(), "Nieuwe aanvraag");
    }

    public void OverzichtSchoolfichesButtonClicked() {
        nieuweAanvraag.verwijderToewijzingsaanvraag();
        updateFrame(ficheSchool.getElementen(), "Overzicht Schoolfiches");
    }

    public void OverzichtAanvragenButtonClicked() {
        nieuweAanvraag.verwijderToewijzingsaanvraag();
        updateFrame(overzichtToewijzingsaanvraag.getElementen(), "Overzicht Aanvragen");
    }

    private void updateFrame(Parent elementen, String titel) {
        mainFrame.getChildren().removeAll();
        mainFrame.getChildren().setAll(elementen);
        mainFrameLbl.setText(titel);
    }

    public void logOutBtnPressed() {
        reset();
        primaryStage.setScene(userLogIn.getScene());
    }

    // private hulpmethodes

    private HashMap<Integer, Toewijzingsaanvraag> getToewijzingsaanvragenVanOuder() {
        HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragenVanOuder = new HashMap<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            if (toewijzingsaanvraag.getOuder().equals(ouder))
                toewijzingsaanvragenVanOuder
                        .put(toewijzingsaanvraag.getToewijzingsaanvraagNummer(), toewijzingsaanvraag);
        } return toewijzingsaanvragenVanOuder;
    }

    private void reset() {
        ouder = null;
        updateFrame(homepage.getElementen(), "Homepage");
        nieuweAanvraag.reset();
        nieuweAanvraag.setOuder(null);
        ficheSchool.reset();
        overzichtToewijzingsaanvraag.reset();
    }


}
