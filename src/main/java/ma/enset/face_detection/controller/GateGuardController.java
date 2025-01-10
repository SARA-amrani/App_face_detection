package ma.enset.face_detection.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ma.enset.face_detection.Metier.GateGuardService;
import ma.enset.face_detection.Metier.GateGuardServiceImp;
import ma.enset.face_detection.SQLiteConnection;
import ma.enset.face_detection.Utils;
import ma.enset.face_detection.entities.AccessLogs;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.util.ResourceBundle;

public class GateGuardController implements Initializable {

    @FXML
    private ImageView imageView;

    private VideoCapture videoCapture;
    private final GateGuardService gateGuardService = new GateGuardServiceImp();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        videoCapture = new VideoCapture(0);

        if (!videoCapture.isOpened()) {
            showErrorAlert("Camera Error", "Unable to access the camera.");
            return;
        }

        // Start the video stream in a separate thread
        new Thread(this::updateImageView).start();
    }

    private void updateImageView() {
        Mat frame = new Mat();
        while (videoCapture.isOpened()) {
            if (videoCapture.read(frame)) {
                Core.flip(frame, frame, 1);
                imageView.setImage(Utils.matToImage(frame));
            } else {
                showErrorAlert("Camera Error", "Error reading frame from the camera.");
                break;
            }
        }
    }

    public void verify() throws SQLException {
        Mat frame = new Mat();
        if (videoCapture.read(frame)) {
            String filename = "temp_image.png";
            Imgcodecs.imwrite(filename, frame);

            boolean matchFound = checkForMatchingImage(frame);
            saveAccessLog(filename, matchFound ? "Verified" : "Denied");

            if (matchFound) {
                showConfirmationAlert("Access Granted", "Identity verified successfully!");
                openHomeWindow(); // Ouvrir la fenêtre "Home" après succès
            } else {
                showErrorAlert("Access Denied", "No matching identity found.");
            }
        } else {
            showErrorAlert("Capture Error", "Failed to capture image from the camera.");
        }
    }

    private void openHomeWindow() {
        try {
            // Charger la vue FXML de la fenêtre "Home"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ma/enset/face_detection/fxml/home-view.fxml"));
            Parent homeRoot = loader.load();

            // Créer une nouvelle scène pour la fenêtre "Home"
            Stage stage = new Stage();
            stage.setTitle("Home GateGuard");
            stage.setScene(new Scene(homeRoot));

            // Fermer la fenêtre actuelle (optionnel)
            Stage currentStage = (Stage) imageView.getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Window Error", "Failed to open home window.");
        }
    }


    private void captureAndSaveImage() {
        Mat frame = new Mat();
        if (videoCapture.read(frame)) {
            String filename = "temp_image.png";
            Imgcodecs.imwrite(filename, frame);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\HP\\IdeaProjects\\App_face_detection\\database.db")) {
                boolean matchFound = checkForMatchingImage(frame);

                // Comparer l'image capturée avec la base de données et afficher le score
                if (matchFound) {
                    showConfirmationAlert("Access Granted", "Identity verified successfully!");
                    saveAccessLog(filename, "Verified"); // Enregistrer le log avec le statut "Verified"
                } else {
                    showErrorAlert("Access Denied", "No matching identity found.");
                    saveAccessLog(filename, "Denied"); // Enregistrer le log avec le statut "Denied"
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                showErrorAlert("Database Error", "An error occurred while accessing the database.");
            }
        } else {
            showErrorAlert("Capture Error", "Failed to capture image from the camera.");
        }
    }

    private boolean checkForMatchingImage(Mat capturedImage) throws SQLException {
        Connection connection = SQLiteConnection.connect();
        String sql = "SELECT id, face_data FROM Users";  // Assurez-vous que le champ 'face_data' contient les images
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            byte[] imageBytes = resultSet.getBytes("face_data"); // Récupérer l'image de la table 'Users'
            Mat dbImage = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

            // Calculer le score de comparaison pour l'image capturée
            double comparisonScore = compareImages(capturedImage, dbImage);

            // Afficher le score dans la console pour l'image capturée
            System.out.println("Score de comparaison pour l'image capturée avec l'ID " + resultSet.getInt("id") + ": " + comparisonScore);

            if (comparisonScore > 0.5) {
                System.out.println("Image correspondante trouvée ! Score: " + comparisonScore);
                return true;
            }
        }
        System.out.println("Aucune correspondance trouvée.");
        return false;
    }

    private double compareImages(Mat img1, Mat img2) {
        // Convertir les images en niveaux de gris
        Mat img1Gray = new Mat();
        Mat img2Gray = new Mat();
        Imgproc.cvtColor(img1, img1Gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(img2, img2Gray, Imgproc.COLOR_BGR2GRAY);

        // Calculer les histogrammes pour chaque image
        Mat hist1 = new Mat();
        Mat hist2 = new Mat();
        Imgproc.calcHist(java.util.List.of(img1Gray), new MatOfInt(0), new Mat(), hist1, new MatOfInt(256), new MatOfFloat(0, 256));
        Imgproc.calcHist(java.util.List.of(img2Gray), new MatOfInt(0), new Mat(), hist2, new MatOfInt(256), new MatOfFloat(0, 256));

        // Normaliser les histogrammes
        Core.normalize(hist1, hist1, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(hist2, hist2, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // Comparer les histogrammes
        return Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);
    }

    // Correction ici pour accepter filename et status comme arguments
    private void saveAccessLog(String filename, String status) {
        try {
            AccessLogs accessLog = new AccessLogs();
            accessLog.setTimestamp(Timestamp.from(Instant.now()));
            accessLog.setStatus(status);

            byte[] imageBytes = Utils.readImageBytes(filename);
            accessLog.setImage_snapshot(imageBytes);

            gateGuardService.addAccessLogs(accessLog);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to save access log.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop() {
        if (videoCapture.isOpened()) {
            videoCapture.release();
        }
    }
}
