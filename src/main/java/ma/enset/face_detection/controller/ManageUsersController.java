package ma.enset.face_detection.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ma.enset.face_detection.ImageUtils;
import ma.enset.face_detection.Metier.GateGuardService;
import ma.enset.face_detection.Metier.GateGuardServiceImp;
import ma.enset.face_detection.dao.AcessLogsDaoImp;
import ma.enset.face_detection.dao.UsersDaoImp;
import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageUsersController implements Initializable {

    @FXML private TextField textFieldNom;
    @FXML private TextField textFieldEmail;
    @FXML private ImageView imageView;


    @FXML private TextField textFieldRecherche;
    @FXML private TableView<Users> tableUtilisateurs;
    @FXML private TableColumn<Users, Long> columnId;
    @FXML private TableColumn<Users, String> columnNom;
    @FXML private TableColumn<Users, String> columnEmail;
    @FXML private TableColumn<Users, byte[]> columnImage;




    private ObservableList<Users> users = FXCollections.observableArrayList();
    private ObservableList<AccessLogs> accessLogs = FXCollections.observableArrayList();
    private GateGuardService service;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service =new GateGuardServiceImp(new UsersDaoImp(),new AcessLogsDaoImp());
        columnImage.setCellFactory(tc -> new TableCell<Users, byte[]>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(50);  // Largeur de l'image
                imageView.setFitHeight(50); // Hauteur de l'image
            }

            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image(new ByteArrayInputStream(item));
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });


        users.addAll(service.getAllUsers());
        accessLogs.addAll(service.getAllAccessLogs());
        textFieldRecherche.textProperty().addListener((observableValue, oldValue, newValue) -> {
            users.setAll(service.findUserByQuery(newValue));
        });


        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnImage.setCellValueFactory(new PropertyValueFactory<>("face_data"));



        tableUtilisateurs.setItems(users);

    }

    //Menu

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


    public void  ajouter(){
        Users users = new Users();
        users.setUsername(textFieldNom.getText());
        users.setEmail(textFieldEmail.getText());
        users.setFace_data(ImageUtils.imageToBytes(imageView.getImage()));
        service.addUser(users);
        load();

    }
    public void ajouterImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");

        // Filtrer pour ne permettre que les fichiers d'image
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }

    }

    public void supprimer(){
        Users users = tableUtilisateurs.getSelectionModel().getSelectedItem();
        service.deleteUser(users);
        load();


    }

    public void modifier(){
        Users users = tableUtilisateurs.getSelectionModel().getSelectedItem();
        users.setUsername(textFieldNom.getText());
        users.setEmail(textFieldEmail.getText());
        users.setFace_data(ImageUtils.imageToBytes(imageView.getImage()));
        service.updateUser(users);
        load();

    }
    public void vider(){
    }

    private void load(){
        users.clear();
        users.addAll(service.getAllUsers());
        accessLogs.addAll(service.getAllAccessLogs());

    }

}
