package GUI;

import SysteemKlasses.Ouder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerDashboardOuder implements Initializable{
    private static Scene instance = null;

    private static Ouder ouder;

    @FXML
    private Parent nieuweAanvraagParent;
    @FXML
    private VBox mainFrame;
    @FXML
    private Label mainFrameLbl;

    public static Scene getInstance() {
        if(instance == null) {
            try {
                Parent root = FXMLLoader.load(ControllerDashboardOuder.class.getResource("DashboardOuder.fxml"));
                instance = new Scene(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            return instance;
        } else return instance;
    }

    public static Ouder getOuder() {
        return ouder;
    }

    public static void setOuder(Ouder ouder) {
        ControllerDashboardOuder.ouder = ouder;
    }

    public void nieuweAanvraagButtonClicked() {
        //nieuweAanvraagParent = FXMLLoader.load(getClass().getResource("nieuweAanvraag.fxml"));
        mainFrame.getChildren().removeAll();
        mainFrame.getChildren().setAll(nieuweAanvraagParent);
        mainFrameLbl.setText("Nieuwe aanvraag");
    }

    public void OverzichtSchoolfichesButtonClicked() {
        mainFrameLbl.setText("Overzicht Schoolfiches");
    }

    public void OverzichtAanvragenButtonClicked() {
        mainFrameLbl.setText("Overzicht Aanvragen");
    }

    public void logOutBtnPressed() {
        /*
        try {
            Parent root = FXMLLoader.load(getClass().getResource("InlogScherm.fxml"));
            Main.primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        */
        //Parent root = ControllerInlogScherm.getInstance();
        //Main.primaryStage.setScene(new Scene(root));
        Main.primaryStage.setScene(ControllerInlogScherm.getInstance());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            nieuweAanvraagParent = FXMLLoader.load(getClass().getResource("nieuweAanvraag.fxml"));
            mainFrame.getChildren().removeAll();
            mainFrame.getChildren().setAll(nieuweAanvraagParent);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }
}
