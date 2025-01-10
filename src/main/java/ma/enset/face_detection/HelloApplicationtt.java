package ma.enset.face_detection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ma.enset.face_detection.controller.HomeController;

import java.io.IOException;

public class HelloApplicationtt extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/home-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);
        stage.setTitle("Home GateGuard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
