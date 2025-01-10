package ma.enset.face_detection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteConnection {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\HP\\IdeaProjects\\App_face_detection\\database.db";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite established.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return connection;
    }

    public static void createTables() {
        String createUsers = """
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    face_data Byte[],
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createAdmins = """
                CREATE TABLE IF NOT EXISTS Admins (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE
                );
                """;

        String createAccessLogs = """
                CREATE TABLE IF NOT EXISTS AccessLogs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status TEXT NOT NULL,
                    image_snapshot BLOB,
                    FOREIGN KEY (user_id) REFERENCES Users(id)
                );
                """;

        String createStatistics = """
                CREATE TABLE IF NOT EXISTS Statistics (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    total_attempts INTEGER DEFAULT 0,
                    successful_attempts INTEGER DEFAULT 0,
                    failed_attempts INTEGER DEFAULT 0,
                    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createUsers);
            statement.execute(createAdmins);
            statement.execute(createAccessLogs);
            statement.execute(createStatistics);
            System.out.println("Tables created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTables();
    }
}

