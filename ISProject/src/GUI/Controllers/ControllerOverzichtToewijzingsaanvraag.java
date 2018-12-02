package GUI.Controllers;

import GUI.AlertBox;
import SysteemKlasses.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ControllerOverzichtToewijzingsaanvraag implements Initializable {

    @FXML
    ChoiceBox<Toewijzingsaanvraag> aanvragenChoicebox;
    @FXML
    Label datumLbl;
    @FXML
    ScrollPane ficheAanvraag;
    @FXML
    Label aanvraagNrLbl;
    @FXML
    Label statusLbl;
    @FXML
    Label toegewezenSchoolLbl;
    @FXML
    Label naamKindLbl;
    @FXML
    Label voornaamKindLbl;
    @FXML
    Label RRNKindLbl;
    @FXML
    Label telefoonNrKindLbl;
    @FXML
    Label naamOuderLbl;
    @FXML
    Label voornaamOuderLbl;
    @FXML
    Label RRNOuderLbl;
    @FXML
    Label straatOuderLbl;
    @FXML
    Label huisNrOuderLbl;
    @FXML
    Label postcodeOuderLbl;
    @FXML
    Label gemeenteOuderLbl;
    @FXML
    Label emailOuderLbl;
    @FXML
    Label vk1NaamLbl;
    @FXML
    Label vk1StraatLbl;
    @FXML
    Label vk1HuisNrLbl;
    @FXML
    Label vk1PostcodeLbl;
    @FXML
    Label vk1GemeenteLbl;
    @FXML
    Label vk2NaamLbl;
    @FXML
    Label vk2StraatLbl;
    @FXML
    Label vk2HuisNrLbl;
    @FXML
    Label vk2PostcodeLbl;
    @FXML
    Label vk2GemeenteLbl;
    @FXML
    Label vk3NaamLbl;
    @FXML
    Label vk3StraatLbl;
    @FXML
    Label vk3HuisNrLbl;
    @FXML
    Label vk3PostcodeLbl;
    @FXML
    Label vk3GemeenteLbl;

    private Parent elementen;
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;

    public Parent getElementen() {
        return elementen;
    }

    public void setElementen(Parent elementen) {
        this.elementen = elementen;
    }

    public void setToewijzingsaanvragen(HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen) {
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values())
            aanvragenChoicebox.getItems().add(toewijzingsaanvraag);
    }

    public void toonFicheBtnPressed() {
        Toewijzingsaanvraag toewijzingsaanvraag = aanvragenChoicebox.getSelectionModel().getSelectedItem();
        if(toewijzingsaanvraag != null) {
            vulFiche(toewijzingsaanvraag);
            ficheAanvraag.setVisible(true);
        } else {
            AlertBox alertBox = new AlertBox("Error", "Er is geen toewijzingsaanvraag geselecteerd");
            alertBox.show();
        }
    }

    public void reset() {
        ficheAanvraag.setVisible(false);
        aanvragenChoicebox.getSelectionModel().select(null);
        aanvragenChoicebox.getItems().removeAll(toewijzingsaanvragen.values());
    }

    private void vulFiche(Toewijzingsaanvraag toewijzingsaanvraag) {
        aanvraagNrLbl.setText(""+ toewijzingsaanvraag.getToewijzingsaanvraagNummer());
        statusLbl.setText(toewijzingsaanvraag.getStatusToewijzingsaanvraag().toString());
        if(toewijzingsaanvraag.getStudent().getToegewezenSchool() == null)
            toegewezenSchoolLbl.setText("Nog geen school toegewezen");
        else
            toegewezenSchoolLbl.setText(toewijzingsaanvraag.getStudent().getToegewezenSchool().toString());

        Student student = toewijzingsaanvraag.getStudent();
        Ouder ouder = toewijzingsaanvraag.getOuder();
        Adres adresOuder = ouder.getAdres();
        School voorkeursschool1 = toewijzingsaanvraag.getVoorkeuren()[0].getSchool();
        School voorkeursschool2 = toewijzingsaanvraag.getVoorkeuren()[1].getSchool();
        School voorkeursschool3 = toewijzingsaanvraag.getVoorkeuren()[2].getSchool();
        // kind
        naamKindLbl.setText(student.getNaam());
        voornaamKindLbl.setText(student.getVoornaam());
        RRNKindLbl.setText(student.getRijksregisterNummer());
        telefoonNrKindLbl.setText(student.getPhoneNumber());

        // ouder
        naamOuderLbl.setText(ouder.getNaam());
        voornaamOuderLbl.setText(ouder.getVoornaam());
        RRNOuderLbl.setText(ouder.getRijksregisterNummer());
        straatOuderLbl.setText(adresOuder.getStraat());
        huisNrOuderLbl.setText(adresOuder.getStraatnummer());
        postcodeOuderLbl.setText("" + adresOuder.getGemeente().getPostcode());
        gemeenteOuderLbl.setText(adresOuder.getGemeente().getNaam());
        emailOuderLbl.setText(ouder.getEmailAdres());

        // eerste voorkeur
        vk1NaamLbl.setText(voorkeursschool1.getNaam());
        vk1StraatLbl.setText(voorkeursschool1.getAdres().getStraat());
        vk1HuisNrLbl.setText(voorkeursschool1.getAdres().getStraatnummer());
        vk1PostcodeLbl.setText("" + voorkeursschool1.getAdres().getGemeente().getPostcode());
        vk1GemeenteLbl.setText(voorkeursschool1.getAdres().getGemeente().getNaam());

        // tweede voorkeur
        vk2NaamLbl.setText(voorkeursschool2.getNaam());
        vk2StraatLbl.setText(voorkeursschool2.getAdres().getStraat());
        vk2HuisNrLbl.setText(voorkeursschool2.getAdres().getStraatnummer());
        vk2PostcodeLbl.setText("" + voorkeursschool2.getAdres().getGemeente().getPostcode());
        vk2GemeenteLbl.setText(voorkeursschool2.getAdres().getGemeente().getNaam());

        // derde voorkeur
        vk3NaamLbl.setText(voorkeursschool3.getNaam());
        vk3StraatLbl.setText(voorkeursschool3.getAdres().getStraat());
        vk3HuisNrLbl.setText(voorkeursschool3.getAdres().getStraatnummer());
        vk3PostcodeLbl.setText("" + voorkeursschool3.getAdres().getGemeente().getPostcode());
        vk3GemeenteLbl.setText(voorkeursschool3.getAdres().getGemeente().getNaam());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ficheAanvraag.setVisible(false);
    }

}