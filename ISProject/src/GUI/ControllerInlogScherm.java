package GUI;

import Logic.StudentProposingIToewijzingsAlgoritme;
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
    private static Scene instance = null;

    @FXML
    Button logInBtn;
    @FXML
    TextField RRNummerOuder;


    public static Scene getInstance() {
        if(instance == null) {
            try {
                Parent root = FXMLLoader.load(ControllerInlogScherm.class.getResource("InlogScherm.fxml"));
                instance = new Scene(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            return instance;
        } else return instance;
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
            Main.primaryStage.setScene(ControllerDashboardOuder.getInstance());
        }
        /*
        else if(isGegevensOuderCorrect(rijksregisternummer)) {
            ControllerDashboardOuder.setOuder(Logic.StudentProposingIToewijzingsAlgoritme.getOuders().get(rijksregisternummer));
            Main.primaryStage.setScene(ControllerDashboardOuder.getInstance());
        }
        */
        else {
            AlertBox alertBox = new AlertBox("Fout Rijksregisternummer","Fout Rijksregisternummer");
            alertBox.show();
        }

    }

    private boolean isGegevensOuderCorrect(String input) {
        return StudentProposingIToewijzingsAlgoritme.getOuders().containsKey(input);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
