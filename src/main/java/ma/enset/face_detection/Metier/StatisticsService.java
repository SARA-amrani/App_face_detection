package ma.enset.face_detection.Metier;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import ma.enset.face_detection.SQLiteConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StatisticsService{

    private Connection connection;

    public StatisticsService() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\HP\\IdeaProjects\\App_face_detection\\database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM Users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public Map<LocalDate, Integer> getAccessAttemptsPerDay() throws SQLException {
        Map<LocalDate, Integer> attemptsPerDay = new HashMap<>();

        String sql = """
        SELECT date(timestamp / 1000, 'unixepoch') AS access_date, COUNT(*) AS attempt_count
        FROM AccessLogs
        WHERE status = 'denied'
        GROUP BY access_date;
    """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String dateString = resultSet.getString("access_date");
                int count = resultSet.getInt("attempt_count");

                // Convertir la chaîne de date en LocalDate
                LocalDate date = LocalDate.parse(dateString);
                attemptsPerDay.put(date, count);
            }
        }

        return attemptsPerDay;
    }

    public int getDeniedAccessCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM AccessLogs WHERE status = 'denied'";
        try (var connection = SQLiteConnection.connect();
             var statement = connection.prepareStatement(query);
             var resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1); // Récupère le nombre d'accès refusés
            }
        }
        return 0; // Retourne 0 si aucun accès refusé n'est trouvé
    }



}
