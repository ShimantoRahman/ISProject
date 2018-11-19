package GUI;

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
import java.util.ResourceBundle;

public class ControllerNieuweAanvraag implements Initializable {
    private static Scene instance = null;

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

    public static Scene getInstance() {
        if(instance == null) {
            try {
                Parent root = FXMLLoader.load(ControllerDashboardOuder.class.getResource("nieuweAanvraag.fxml"));
                instance = new Scene(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            return instance;
        } else return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //vul choiceboxes met data van de databank

        //voorlopig choiceboxes handmatig vullen met testdata
        eersteKeuzeBox.getItems().addAll("Voskenslaan", "Don Bosco", "Sint-Bernardus");
        tweedeKeuzeBox.getItems().addAll("Voskenslaan", "Don Bosco", "Sint-Bernardus");
        derdeKeuzeBox.getItems().addAll("Voskenslaan", "Don Bosco", "Sint-Bernardus");
    }

    public void IndienBtnPressed() {
        //test of dat de data correct is ingevoerd
        if(!RRNummerTxt.getText().equals("root")) {
            AlertBox alertBox = new AlertBox("Error","Rijksregisternummer niet gevonden");
            alertBox.show();
        }
        else if(!scholenCorrect()) {
            AlertBox alertBox = new AlertBox("Error","De scholen zijn niet correct gekozen");
            alertBox.show();
        }
        else {
            reset();
            AlertBox alertBox = new AlertBox("Correct","Aanvraag ingediend");
            alertBox.show();
            /** data verwerken
             * 1. een nieuwe toewijzingsaanvraag creeëren
             * 2. afstand van het kind bepalen tot de school
             * ...
             */
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
}
