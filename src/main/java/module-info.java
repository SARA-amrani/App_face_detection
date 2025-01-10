module ma.enset.face_detection {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires opencv;
    requires javafx.swing;
    requires java.sql.rowset;
    requires tensorflow;


    opens ma.enset.face_detection.controller to javafx.fxml;
    opens ma.enset.face_detection.entities to javafx.base;
    exports ma.enset.face_detection;
}