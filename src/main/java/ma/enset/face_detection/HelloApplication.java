package ma.enset.face_detection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Charger le fichier FXML de GateGuard
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/gateguard-view.fxml"));
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
