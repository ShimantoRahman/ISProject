package GUI.Controllers;

import GUI.AlertBox;
import SysteemKlasses.Main;
import SysteemKlasses.School;
import SysteemKlasses.Student;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerFicheSchool implements Initializable {

    // FXML variabelen
    @FXML
    private ChoiceBox<School> scholenChoicebox;
    @FXML
    private Label datumLbl;
    @FXML
    private VBox ficheFrame;
    @FXML
    private Label naamLbl;
    @FXML
    private Label straatLbl;
    @FXML
    private Label huisnummerLbl;
    @FXML
    private Label postcodeLbl;
    @FXML
    private Label gemeenteLbl;
    @FXML
    private Label aantalPlaatsenLbl;
    @FXML
    private VBox toegewezenStudentenBox;

    // instantie variabelen
    private Parent elementen;
    private ArrayList<School> scholen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ficheFrame.setVisible(false);
    }

    // getters & setters
    public Parent getElementen() {
        return elementen;
    }

    public void setElementen(Parent elementen) {
        this.elementen = elementen;
    }

    public void setScholen(ArrayList<School> scholen) {
        this.scholen = scholen;
        for (School school: scholen)
            scholenChoicebox.getItems().add(school);
    }

    // event handlers
    public void toonFicheBtnPressed() {
        // pak de geselecteerde school
        School selectedSchool = scholenChoicebox.getSelectionModel().getSelectedItem();

        if(selectedSchool !=  null) {
            // vul labels met data van de school
            vulFiche(selectedSchool);

            // maak fiche zichtbaar
            ficheFrame.setVisible(true);
        } else {
            AlertBox alertBox = new AlertBox("Error", "Er is geen school geselecteerd");
            alertBox.show();
        }
    }

    // andere public methoden
    public void reset() {
        ficheFrame.setVisible(false);
        scholenChoicebox.getSelectionModel().select(null);
    }

    // private hulpmethoden
    private void vulFiche(School school) {
        naamLbl.setText(school.getNaam());
        straatLbl.setText(school.getAdres().getStraat());
        huisnummerLbl.setText(school.getAdres().getStraatnummer());
        postcodeLbl.setText("" + school.getAdres().getGemeente().getPostcode());
        gemeenteLbl.setText(school.getAdres().getGemeente().getNaam());
        aantalPlaatsenLbl.setText("" + school.getAantalPlaatsen());

        vulToegewezenStudenten(school);
    }

    private void vulToegewezenStudenten(School school) {
        if(school.getStudenten().size() == 0) {
            VBox toegewezenStudenten = new VBox();
            toegewezenStudenten.getChildren()
                    .add(new Label("Er zijn nog geen studenten toegewezen tot deze school"));
            toegewezenStudentenBox.getChildren().setAll(toegewezenStudenten);
        }

        else {
            VBox toegewezenStudenten = new VBox();
            int i = 1;
            for (Student student: school.getStudenten().values()) {
                Label label = new Label(String.format("%d. %s %s", i, student.getNaam(), student.getVoornaam()));
                toegewezenStudenten.getChildren().add(label);
                i++;
            } toegewezenStudentenBox.getChildren().setAll(toegewezenStudenten);
        }
    }


}
