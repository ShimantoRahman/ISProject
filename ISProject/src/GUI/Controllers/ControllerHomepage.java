package GUI.Controllers;

import SysteemKlasses.Ouder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerHomepage implements Initializable {

    // FXML variabelen
    @FXML
    private Label welkomLbl;

    // instantie variabelen
    private Parent elementen;
    private Ouder ouder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // getters & setters
    public Parent getElementen() {
        return elementen;
    }

    public void setElementen(Parent elementen) {
        this.elementen = elementen;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
        welkomLbl.setText(String.format("Welkom, %s %s", ouder.getNaam(), ouder.getVoornaam()));
    }
}
