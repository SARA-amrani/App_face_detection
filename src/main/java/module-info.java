module ma.enset.face_detection {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens ma.enset.face_detection to javafx.fxml;
    exports ma.enset.face_detection;
}