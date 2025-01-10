package ma.enset.face_detection;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

    public static Image matToImage(Mat mat) {
        try {
            int width = mat.width(), height = mat.height(), channels = mat.channels();
            byte[] sourcePixels = new byte[width * height * channels];
            mat.get(0, 0, sourcePixels);

            BufferedImage image;
            if (mat.channels() > 1) {
                image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            } else {
                image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            }
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            System.err.println("Erreur de conversion Mat vers Image : " + e.getMessage());
            return null;
        }
    }
    public static byte[] readImageBytes(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
}
