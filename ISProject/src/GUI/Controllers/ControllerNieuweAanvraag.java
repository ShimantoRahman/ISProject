package GUI.Controllers;

import GUI.AlertBox;
import Logic.IAfstandBerekeningFormule;
import SysteemKlasses.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ControllerNieuweAanvraag implements Initializable {

    @FXML
    TextField RRNummerTxt;
    @FXML
    ChoiceBox<School> eersteKeuzeBox;
    @FXML
    ChoiceBox<School> tweedeKeuzeBox;
    @FXML
    ChoiceBox<School> derdeKeuzeBox;
    @FXML
    CheckBox eersteKeuzeCheckbox;
    @FXML
    CheckBox tweedeKeuzeCheckbox;
    @FXML
    CheckBox derdeKeuzeCheckbox;

    private Parent elementen;
    private Ouder ouder;
    private Toewijzingsaanvraag toewijzingsaanvraag;
    private ArrayList<School> scholen;
    private HashMap<String, Student> studenten;
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private IAfstandBerekeningFormule afstandBerekeningFormule;

    public Parent getElementen() {
        return elementen;
    }

    public void setElementen(Parent elementen) {
        this.elementen = elementen;
    }

    public Ouder getOuder() {
        return ouder;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
    }

    public Toewijzingsaanvraag getToewijzingsaanvraag() {
        return toewijzingsaanvraag;
    }

    public void setToewijzingsaanvraag(Toewijzingsaanvraag toewijzingsaanvraag) {
        this.toewijzingsaanvraag = toewijzingsaanvraag;
    }

    public void verwijderToewijzingsaanvraag() {
        this.toewijzingsaanvraag = null;
        Toewijzingsaanvraag.setAantalAanvragen(Toewijzingsaanvraag.getAantalAanvragen() - 1);
    }

    public void setScholen(ArrayList<School> scholen) {
        this.scholen = scholen;
        for (School school: scholen) {
            eersteKeuzeBox.getItems().addAll(school);
            tweedeKeuzeBox.getItems().addAll(school);
            derdeKeuzeBox.getItems().addAll(school);
        }
    }

    public void setStudenten(HashMap<String, Student> studenten) {
        this.studenten = studenten;
    }

    public void setToewijzingsaanvragen(HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen) {
        this.toewijzingsaanvragen = toewijzingsaanvragen;
    }

    public void setAfstandBerekeningFormule(IAfstandBerekeningFormule afstandBerekeningFormule) {
        this.afstandBerekeningFormule = afstandBerekeningFormule;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // de "indienen" button wordt ingedrukt
    // niet "indien een button gedrukt wordt"
    public void IndienBtnPressed() {
        //test of dat de data correct is ingevoerd
        String RRNStudent = RRNummerTxt.getText();
        // er is geen student met dit rijksregisternummer
        if(!RRNStudent.equals("root") && !studenten.containsKey(RRNStudent)) {
            AlertBox alertBox = new AlertBox("Error","Er is geen student met dit rijksregisternummer");
            alertBox.show();
        }
        // er is voor de student al reeds een toewijzingsaanvraag geregistreerd
        else if(heeftStudentAlEenToewijzingsaanvraag(RRNStudent)) {
            AlertBox alertBox = new AlertBox("Error",
                    "Er is voor de student al reeds een toewijzingsaanvraag geregistreerd");
            alertBox.show();
        }
        // de scholen zijn niet correct ingegeven (meerdere keren dezelfde school)
        else if(!scholenCorrect()) {
            AlertBox alertBox = new AlertBox("Error","De scholen zijn niet correct gekozen");
            alertBox.show();
        }
        // nieuwe toewijzingsaanvraag wordt aangemaakt
        else {
            double breedtegraadOuder =  ouder.getAdres().getGemeente().getBreedtegraad();
            double lengtegraadOuder =  ouder.getAdres().getGemeente().getLengtegraad();

            toewijzingsaanvraag.setStudent(studenten.get(RRNStudent));
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);

            Voorkeur[] voorkeuren = new Voorkeur[3];

            voorkeuren[0] = getVoorkeur(eersteKeuzeBox, eersteKeuzeCheckbox, breedtegraadOuder, lengtegraadOuder);
            voorkeuren[1] = getVoorkeur(tweedeKeuzeBox, tweedeKeuzeCheckbox, breedtegraadOuder, lengtegraadOuder);
            voorkeuren[2] = getVoorkeur(derdeKeuzeBox, derdeKeuzeCheckbox, breedtegraadOuder, lengtegraadOuder);

            toewijzingsaanvraag.setVoorkeuren(voorkeuren);

            toewijzingsaanvragen.put(toewijzingsaanvraag.getToewijzingsaanvraagNummer(), toewijzingsaanvraag);

            toewijzingsaanvraag = new Toewijzingsaanvraag(ouder);
            reset();
            AlertBox alertBox = new AlertBox("Succes","Aanvraag ingediend");
            alertBox.show();
        }
    }

    private boolean scholenCorrect() {
        School eersteKeuze = eersteKeuzeBox.getValue();
        School tweedeKeuze = tweedeKeuzeBox.getValue();
        School derdeKeuze = derdeKeuzeBox.getValue();
        if(eersteKeuze != null && tweedeKeuze != null && derdeKeuze != null)
            return !(eersteKeuze.equals(tweedeKeuze) || eersteKeuze.equals(derdeKeuze) || tweedeKeuze.equals(derdeKeuze));
        return false;
    }

    //zet alle velden terug op null of false
    public void reset() {
        RRNummerTxt.setText(null);
        eersteKeuzeBox.setValue(null);
        tweedeKeuzeBox.setValue(null);
        derdeKeuzeBox.setValue(null);
        eersteKeuzeCheckbox.setSelected(false);
        tweedeKeuzeCheckbox.setSelected(false);
        derdeKeuzeCheckbox.setSelected(false);
    }

    private Voorkeur getVoorkeur(ChoiceBox<School> keuzeBox, CheckBox checkBox,
                                 double breedtegraadOuder, double lengtegraadOuder) {
        for (int i = 0; i < scholen.size(); i++) {
            if(keuzeBox.getSelectionModel().isSelected(i)) {
                School school = scholen.get(i);
                double breedtegraadSchool = school.getAdres().getGemeente().getBreedtegraad();
                double lengtegraadSchool = school.getAdres().getGemeente().getLengtegraad();
                afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder,
                        breedtegraadSchool, lengtegraadSchool);
                double afstand = afstandBerekeningFormule.getAfstand();
                return new Voorkeur(school,afstand, checkBox.isSelected());
            }
        } return null;
    }

    private boolean heeftStudentAlEenToewijzingsaanvraag(String rijksregisternummer) {
        for (Toewijzingsaanvraag aanvraag: toewijzingsaanvragen.values()) {
            if(aanvraag.getStudent().getRijksregisterNummer().equals(rijksregisternummer));
                return true;
        } return false;
    }
}
