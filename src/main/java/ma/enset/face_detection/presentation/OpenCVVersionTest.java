package ma.enset.face_detection.presentation;

import org.opencv.core.Core;

public class OpenCVVersionTest {

    public static void main(String[] args) {
        // Charger la bibliothèque native OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Afficher la version d'OpenCV
        String version = Core.VERSION;
        System.out.println("Version d'OpenCV : " + version);
    }
}
