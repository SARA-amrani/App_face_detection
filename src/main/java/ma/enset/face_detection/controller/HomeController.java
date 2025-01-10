package ma.enset.face_detection.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class HomeController {
    private BorderPane mainPane; // Conteneur principal pour afficher les vues

    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            mainPane.setCenter(root); // Remplace le contenu central du BorderPane par la nouvelle vue
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboard() {
        loadView("/ma/enset/face_detection/fxml/home-view.fxml");
    }

    public void manageUsers() {
        loadView("/ma/enset/face_detection/fxml/home-view.fxml");
    }

    public void statistics() {
        loadView("/ma/enset/face_detection/fxml/home-view.fxml");
    }
}
