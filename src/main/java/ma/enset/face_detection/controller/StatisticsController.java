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
import javafx.scene.control.Label;
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
            customizePieChartColors();
            customizeBarChartColors();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserStatistics() throws SQLException {
        int totalUsers = statisticsService.getTotalUsers(); // Nombre total d'utilisateurs
        int deniedUsers = statisticsService.getDeniedAccessCount(); // Nombre d'accès refusés

        // Création des données pour le graphique en fonction des résultats réels
        PieChart.Data verifiedData = new PieChart.Data("Verified", totalUsers);
        PieChart.Data deniedData = new PieChart.Data("Denied", deniedUsers);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(verifiedData, deniedData);
        usersPieChart.setData(pieChartData);
    }


    private void loadAccessAttemptsPerDay() throws SQLException {
        Map<LocalDate, Integer> attemptsPerDay = statisticsService.getAccessAttemptsPerDay();

        ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Access Denied Attempts");

        for (Map.Entry<LocalDate, Integer> entry : attemptsPerDay.entrySet()) {
            String date = entry.getKey().toString();
            int attempts = entry.getValue();
            series.getData().add(new XYChart.Data<>(date, attempts));
        }

        data.add(series);
        attemptsBarChart.setData(data);

        // Appliquer une couleur violette à toute la série
        applySeriesColor(series, "#4A148C");

        // Forcer la couleur de la légende
        updateLegendColor("#4A148C");
    }

    private void applySeriesColor(XYChart.Series<String, Number> series, String color) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: " + color + ";");
        }
    }

    private void updateLegendColor(String color) {
        // Forcer JavaFX à appliquer le CSS et générer les légendes
        attemptsBarChart.applyCss();

        // Modifier les couleurs des éléments de la légende
        Node legend = attemptsBarChart.lookup(".chart-legend");
        if (legend != null) {
            for (Node legendItem : legend.lookupAll(".chart-legend-item-symbol")) {
                legendItem.setStyle("-fx-background-color: " + color + ";");
            }
        }
    }


    private void customizePieChartColors() {
        // Appliquer des couleurs violettes aux secteurs du PieChart
        for (int i = 0; i < usersPieChart.getData().size(); i++) {
            String color = (i % 2 == 0) ? "#A68BB3" : "#8C4799"; // Alterner les couleurs
            usersPieChart.getData().get(i).getNode().setStyle("-fx-pie-color: " + color + ";");
        }
    }

    private void customizeBarChartColors() {
        // Appliquer des couleurs violettes aux barres du BarChart
        for (XYChart.Series<String, Number> series : attemptsBarChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                data.getNode().setStyle("-fx-bar-fill: #8C4799;"); // Couleur violette
            }
        }
    }
}
