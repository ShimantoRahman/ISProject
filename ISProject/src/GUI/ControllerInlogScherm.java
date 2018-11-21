package GUI;

import SysteemKlasses.Adres;
import SysteemKlasses.Gemeente;
import SysteemKlasses.Main;
import SysteemKlasses.Ouder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerInlogScherm implements Initializable {
    private static Scene scene = null;

    @FXML
    Button logInBtn;
    @FXML
    TextField RRNummerOuder;


    public static Scene getScene() {
        if(scene == null) {
            try {
                Parent root = FXMLLoader.load(ControllerInlogScherm.class.getResource("InlogScherm.fxml"));
                scene = new Scene(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            return scene;
        } else return scene;
    }

    public void logInBtnPressed() {
        String rijksregisternummer = RRNummerOuder.getText();
        if (rijksregisternummer.equals("root")) {
            /*
            try {
                Parent root = FXMLLoader.load(getClass().getResource("DashboardOuder.fxml"));
                Main.primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            */
            // test ouder
            ControllerDashboardOuder.setOuder(new Ouder("rrn","naam", "voornaam",
                    "email", new Adres("straat","huisnummer",
                    new Gemeente("naamGemeente", 9000, 4,51))));
            RRNummerOuder.setText("");
            Main.getPrimaryStage().setScene(ControllerDashboardOuder.getScene());
        }

        else if(isGegevensOuderCorrect(rijksregisternummer)) {
            ControllerDashboardOuder.setOuder(Main.getOuders().get(rijksregisternummer));
            RRNummerOuder.setText("");
            Main.getPrimaryStage().setScene(ControllerDashboardOuder.getScene());
        }

        else {
            AlertBox alertBox = new AlertBox("Fout Rijksregisternummer","Fout Rijksregisternummer");
            alertBox.show();
        }

    }

    private boolean isGegevensOuderCorrect(String input) {
        return Main.getOuders().containsKey(input);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
