package GUI;

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
    private static Scene scene = null;

    private static Ouder ouder;
    private static ArrayList<School> scholen;
    private static HashMap<String, Student> studenten;
    private static IAfstandBerekeningFormule afstandBerekeningFormule;

    @FXML
    TextField RRNummerTxt;
    @FXML
    ChoiceBox<String> eersteKeuzeBox;
    @FXML
    ChoiceBox<String> tweedeKeuzeBox;
    @FXML
    ChoiceBox<String> derdeKeuzeBox;
    @FXML
    CheckBox eersteKeuzeCheckbox;
    @FXML
    CheckBox tweedeKeuzeCheckbox;
    @FXML
    CheckBox derdeKeuzeCheckbox;

    public static Scene getScene() {
        if(scene == null) {
            try {
                Parent root = FXMLLoader.load(ControllerDashboardOuder.class.getResource("nieuweAanvraag.fxml"));
                scene = new Scene(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            return scene;
        } else return scene;
    }

    public static Ouder getOuder() {
        return ouder;
    }

    public static void setOuder(Ouder ouder) {
        ControllerNieuweAanvraag.ouder = ouder;
    }

    public void setAfstandBerekeningFormule(IAfstandBerekeningFormule afstandBerekeningFormule) {
        this.afstandBerekeningFormule = afstandBerekeningFormule;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //vul choiceboxes met data van de databank
        studenten = Main.getStudenten();
        scholen = Main.getScholen();

        for (School school: scholen) {
            eersteKeuzeBox.getItems().addAll(school.toString());
            tweedeKeuzeBox.getItems().addAll(school.toString());
            derdeKeuzeBox.getItems().addAll(school.toString());
        }

        //voorlopig choiceboxes handmatig vullen met testdata
        eersteKeuzeBox.getItems().addAll("Voskenslaan", "Don Bosco", "Sint-Bernardus");
        tweedeKeuzeBox.getItems().addAll("Voskenslaan", "Don Bosco", "Sint-Bernardus");
        derdeKeuzeBox.getItems().addAll("Voskenslaan", "Don Bosco", "Sint-Bernardus");
    }

    // de "indienen" button wordt ingedrukt
    // niet "indien een button gedrukt wordt"
    public void IndienBtnPressed() {
        //test of dat de data correct is ingevoerd
        String RRNStudent = RRNummerTxt.getText();
        if(!RRNStudent.equals("root") && !studenten.containsKey(RRNStudent)) {
            AlertBox alertBox = new AlertBox("Error","Rijksregisternummer niet gevonden");
            alertBox.show();
        }
        else if(!scholenCorrect()) {
            AlertBox alertBox = new AlertBox("Error","De scholen zijn niet correct gekozen");
            alertBox.show();
        }
        else {
            double breedtegraadOuder =  ouder.getAdres().getGemeente().getBreedtegraad();
            double lengtegraadOuder =  ouder.getAdres().getGemeente().getLengtegraad();

            Toewijzingsaanvraag toewijzingsaanvraag = new Toewijzingsaanvraag(ouder);
            toewijzingsaanvraag.setStudent(studenten.get(RRNStudent));
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);

            Voorkeur[] voorkeuren = new Voorkeur[3];

            voorkeuren[0] = getEersteVoorkeur(breedtegraadOuder, lengtegraadOuder);
            voorkeuren[1] = getTweedeVoorkeur(breedtegraadOuder, lengtegraadOuder);
            voorkeuren[2] = getDerdeVoorkeur(breedtegraadOuder, lengtegraadOuder);

            toewijzingsaanvraag.setVoorkeuren(voorkeuren);

            Main.getToewijzingsaanvragen().put(toewijzingsaanvraag.getToewijzingsaanvraagNummer(), toewijzingsaanvraag);

            reset();
            AlertBox alertBox = new AlertBox("Correct","Aanvraag ingediend");
            alertBox.show();
        }
    }

    private boolean scholenCorrect() {
        String eersteKeuze = eersteKeuzeBox.getValue();
        String tweedeKeuze = tweedeKeuzeBox.getValue();
        String derdeKeuze = derdeKeuzeBox.getValue();
        if(eersteKeuze != null && tweedeKeuze != null && derdeKeuze != null)
            return !(eersteKeuze.equals(tweedeKeuze) || eersteKeuze.equals(derdeKeuze) || tweedeKeuze.equals(derdeKeuze));
        return false;
    }

    //zet alle velden terug op null of false
    private void reset() {
        RRNummerTxt.setText(null);
        eersteKeuzeBox.setValue(null);
        tweedeKeuzeBox.setValue(null);
        derdeKeuzeBox.setValue(null);
        eersteKeuzeCheckbox.setSelected(false);
        tweedeKeuzeCheckbox.setSelected(false);
        derdeKeuzeCheckbox.setSelected(false);
    }

    private Voorkeur getEersteVoorkeur(double breedtegraadOuder, double lengtegraadOuder) {
        for (int i = 0; i < scholen.size(); i++) {
            if(eersteKeuzeBox.getSelectionModel().isSelected(i)) {
                School school = scholen.get(i);
                double breedtegraadSchool = school.getAdres().getGemeente().getBreedtegraad();
                double lengtegraadSchool = school.getAdres().getGemeente().getLengtegraad();
                afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder,
                        breedtegraadSchool, lengtegraadSchool);
                double afstand = afstandBerekeningFormule.getAfstand();
                return new Voorkeur(school,afstand, eersteKeuzeCheckbox.isSelected());
            }
        } return null;
    }

    private Voorkeur getTweedeVoorkeur(double breedtegraadOuder, double lengtegraadOuder) {
        for (int i = 0; i < scholen.size(); i++) {
            if(tweedeKeuzeBox.getSelectionModel().isSelected(i)) {
                School school = scholen.get(i);
                double breedtegraadSchool = school.getAdres().getGemeente().getBreedtegraad();
                double lengtegraadSchool = school.getAdres().getGemeente().getLengtegraad();
                afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder,
                        breedtegraadSchool, lengtegraadSchool);
                double afstand = afstandBerekeningFormule.getAfstand();
                return new Voorkeur(school,afstand, tweedeKeuzeCheckbox.isSelected());
            }
        } return null;
    }

    private Voorkeur getDerdeVoorkeur(double breedtegraadOuder, double lengtegraadOuder) {
        for (int i = 0; i < scholen.size(); i++) {
            if(derdeKeuzeBox.getSelectionModel().isSelected(i)) {
                School school = scholen.get(i);
                double breedtegraadSchool = school.getAdres().getGemeente().getBreedtegraad();
                double lengtegraadSchool = school.getAdres().getGemeente().getLengtegraad();
                afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder,
                        breedtegraadSchool, lengtegraadSchool);
                double afstand = afstandBerekeningFormule.getAfstand();
                return new Voorkeur(school,afstand, derdeKeuzeCheckbox.isSelected());
            }
        } return null;
    }
}
