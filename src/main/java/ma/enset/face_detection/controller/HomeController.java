package ma.enset.face_detection.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    public void dashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/home-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Home GateGuard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void manageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/manageUsers-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Users");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void statistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/statistics-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistics");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
