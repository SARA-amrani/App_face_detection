package ma.enset.face_detection.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import ma.enset.face_detection.Metier.GateGuardService;
import ma.enset.face_detection.Metier.GateGuardServiceImp;
import ma.enset.face_detection.Utils;
import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.sql.rowset.serial.SerialBlob;
import java.net.URL;
import java.sql.Timestamp;
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

    public void verify() {
        Mat frame = new Mat();
        if (videoCapture.read(frame)) {
            String filename = "temp_image.png";
            Imgcodecs.imwrite(filename, frame);

            boolean matchFound = checkForMatchingImage(frame);
            saveAccessLog(filename, matchFound ? "granted" : "denied");

            if (matchFound) {
                showConfirmationAlert("Access Granted", "Identity verified successfully!");
            } else {
                showErrorAlert("Access Denied", "No matching identity found.");
            }
        } else {
            showErrorAlert("Capture Error", "Failed to capture image from the camera.");
        }
    }

    private boolean checkForMatchingImage(Mat capturedImage) {
        for (Users user : gateGuardService.getAllUsers()) {
            byte[] imageBytes = user.getFace_data(); // Maintenant, on s'assure que c'est bien un byte[]
            if (imageBytes != null) {
                Mat dbImage = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

                double comparisonScore = compareImages(capturedImage, dbImage);
                if (comparisonScore > 0.9) {
                    return true;
                }
            }
        }
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
