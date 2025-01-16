package ma.enset.face_detection.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import ma.enset.face_detection.Metier.StatisticsService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class StatisticsController {

    @FXML
    private PieChart usersPieChart;

    @FXML
    private BarChart<String, Number> attemptsBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private final StatisticsService statisticsService = new StatisticsService();

    public void initialize() {
        try {
            loadUserStatistics();
            loadAccessAttemptsPerDay();
            updateLegendColors();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserStatistics() throws SQLException {
        int totalUsers = statisticsService.getTotalUsers();
        int deniedUsers = statisticsService.getDeniedAccessCount();

        PieChart.Data verifiedData = new PieChart.Data("Verified", totalUsers);
        PieChart.Data deniedData = new PieChart.Data("Denied", deniedUsers);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(verifiedData, deniedData);
        usersPieChart.setData(pieChartData);
    }

    private void loadAccessAttemptsPerDay() throws SQLException {
        Map<LocalDate, Integer> attemptsPerDay = statisticsService.getAccessAttemptsPerDay();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Access Denied Attempts");

        for (Map.Entry<LocalDate, Integer> entry : attemptsPerDay.entrySet()) {
            String date = entry.getKey().toString();
            int attempts = entry.getValue();
            series.getData().add(new XYChart.Data<>(date, attempts));
        }

        attemptsBarChart.getData().add(series);
    }

    private void updateLegendColors() {
        // Forcer JavaFX à appliquer le CSS et générer les légendes
        usersPieChart.applyCss();
        attemptsBarChart.applyCss();

        // Mettre à jour les couleurs des légendes pour le PieChart
        ObservableList<PieChart.Data> pieData = usersPieChart.getData();
        String[] pieColors = {"#FF4500", "#FFA500"}; // Couleurs des légendes pour le PieChart
        for (int i = 0; i < pieData.size(); i++) {
            String legendSelector = ".chart-legend-item-symbol:nth-of-type(" + (i + 1) + ")";
            Node legendSymbol = usersPieChart.lookup(legendSelector);
            if (legendSymbol != null) {
                legendSymbol.setStyle("-fx-background-color: " + pieColors[i] + "; -fx-background-radius: 0;");
            }
        }

        // Mettre à jour les couleurs des légendes pour le BarChart
        String barColor = "#4A148C"; // Couleur violette pour les légendes des barres
        Node barLegendSymbol = attemptsBarChart.lookup(".chart-legend-item-symbol");
        if (barLegendSymbol != null) {
            barLegendSymbol.setStyle("-fx-background-color: " + barColor + "; -fx-background-radius: 0;");
        }
    }
}
