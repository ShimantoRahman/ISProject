package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    private Stage window;
    private Scene scene;
    private VBox layout;
    private Label label;
    private Button button;

    public AlertBox(String title, String message) {
        this.window = new Stage();
        this.layout = new VBox();
        this.setup(title, message);
    }

    public void show() {
        window.showAndWait();
    }

    private void setup(String title, String message) {
        label = new Label(message);
        button = new Button("Ok");
        button.setOnAction(e -> window.close());

        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);

        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        layout.setMargin(label, new Insets(10,10,10,10));
        layout.setMargin(button, new Insets(10,10,10,10));

        scene = new Scene(layout);

        window.setScene(scene);

    }


}
