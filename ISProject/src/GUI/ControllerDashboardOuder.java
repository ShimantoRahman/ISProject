package GUI;

import SysteemKlasses.Main;
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
    private static Scene scene = null;

    private static Ouder ouder;

    @FXML
    private Parent nieuweAanvraagParent;
    @FXML
    private VBox mainFrame;
    @FXML
    private Label mainFrameLbl;

    public static Scene getScene() {
        if(scene == null) {
            try {
                Parent root = FXMLLoader.load(ControllerDashboardOuder.class.getResource("DashboardOuder.fxml"));
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
        //Parent root = ControllerInlogScherm.getScene();
        //Main.primaryStage.setScene(new Scene(root));
        ControllerDashboardOuder.ouder = null;
        ControllerNieuweAanvraag.setOuder(null);
        Main.getPrimaryStage().setScene(ControllerInlogScherm.getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            nieuweAanvraagParent = FXMLLoader.load(getClass().getResource("nieuweAanvraag.fxml"));
            ControllerNieuweAanvraag.setOuder(ouder);
            mainFrame.getChildren().removeAll();
            mainFrame.getChildren().setAll(nieuweAanvraagParent);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }
}
