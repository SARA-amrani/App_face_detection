module ma.enset.face_detection {
    requires javafx.controls;
    requires javafx.fxml;


    opens ma.enset.face_detection to javafx.fxml;
    exports ma.enset.face_detection;
}