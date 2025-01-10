package ma.enset.face_detection.presentation;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.proto.GraphDef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FaceNetExample {

    private static final String MODEL_PATH = "src/resources/facenet_model/model-20180408-102900.pb"; // Chemin vers le modèle .pb

    public static void main(String[] args) {
        // Charger le modèle FaceNet
        try (Graph graph = new Graph()) {
            // Lire le fichier du modèle .pb
            byte[] graphDefBytes = Files.readAllBytes(Paths.get(MODEL_PATH));

            // Importer le GraphDef dans le graphe TensorFlow
            graph.importGraphDef(GraphDef.parseFrom(graphDefBytes)); // Cette méthode peut être utilisée directement avec les bytes du modèle

            // Créer une session pour exécuter le modèle
            try (Session session = new Session(graph)) {
                System.out.println("Modèle chargé avec succès.");

                // Charger une image et la transformer en un tensor (notre entrée)
                Tensor inputTensor = ImageUtils.loadAndPreprocessImage("path_to_your_image.jpg"); // Remplacez par le chemin de votre image

                // Exécuter le modèle avec le tensor d'entrée
                Tensor outputTensor = session.runner()
                        .feed("input_layer_name", inputTensor)  // Remplacez "input_layer_name" par le nom correct de l'entrée
                        .fetch("output_layer_name")  // Remplacez "output_layer_name" par le nom du layer de sortie
                        .run().get(0); // Vous pouvez obtenir l'output ici

                // Affichez les résultats ou effectuez des actions supplémentaires
                System.out.println("Prédiction effectuée !");
                System.out.println(outputTensor);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du modèle : " + e.getMessage());
        }
    }
}
