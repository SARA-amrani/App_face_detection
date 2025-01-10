package ma.enset.face_detection;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static class PresentationUsers extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            // Charger le fichier FXML de GateGuard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/manageUsers-view.fxml"));
            Parent root = loader.load();

            // Définir la scène avec la vue chargée
            stage.setScene(new Scene(root));
            stage.setTitle("GateGuard");
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
}