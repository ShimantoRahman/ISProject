package GUI.Controllers;

import Logic.IToewijzingsAlgoritme;
import SysteemKlasses.Main;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerAdminPanel {

    // instantie variabelen
    private Stage primaryStage;
    private Scene scene;
    private ControllerAdminLoginIn adminLoginIn;
    private IToewijzingsAlgoritme toewijzingsAlgoritme;

    // getters & setters
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setAdminLoginIn(ControllerAdminLoginIn adminLoginIn) {
        this.adminLoginIn = adminLoginIn;
    }

    public void setToewijzingsAlgoritme(IToewijzingsAlgoritme toewijzingsAlgoritme) {
        this.toewijzingsAlgoritme = toewijzingsAlgoritme;
    }

    // event handlers
    public void StartToewijzingsprocedurePressed() {
        toewijzingsAlgoritme.startToewijzingsProcedure();
        //TODO verwijder dit (enkel voor testen)
        Main.printToewijzingsaanvragen();
    }

    public void logUitBtnPressed() {
        primaryStage.setScene(adminLoginIn.getScene());
    }
}
