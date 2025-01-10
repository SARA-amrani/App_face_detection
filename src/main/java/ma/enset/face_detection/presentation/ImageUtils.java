package ma.enset.face_detection.presentation;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.Tensor;

import java.io.IOException;

public class ImageUtils {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Chargez la bibliothèque native OpenCV
    }

    public static Tensor loadAndPreprocessImage(String imagePath) {
        // Charger l'image en utilisant OpenCV
        Mat image = Imgcodecs.imread(imagePath);

        // Redimensionner l'image à 160x160 (taille attendue par le modèle)
        Mat resizedImage = new Mat();
        Imgproc.resize(image, resizedImage, new Size(160, 160));

        // Convertir l'image en Tensor (après normalisation)
        float[] imageData = new float[160 * 160 * 3];
        resizedImage.get(0, 0, imageData);

        // Créer et retourner le tensor à partir de l'image traitée
        return Tensor.create(new float[][]{imageData});
    }
}
