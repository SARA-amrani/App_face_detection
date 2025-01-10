package ma.enset.face_detection.dao;

import ma.enset.face_detection.SQLiteConnection;
import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcessLogsDaoImp implements AccessLogsDao {

    // Connexion unique par instance
    private Connection connection;

    public AcessLogsDaoImp() {
        this.connection = SQLiteConnection.connect();
        // Passer en mode WAL (Write-Ahead Logging) pour éviter le verrouillage
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la configuration du mode WAL.", e);
        }
    }

    @Override
    public List<AccessLogs> findAll() {
        List<AccessLogs> acessLogs = new ArrayList<>();
        String query = "SELECT * FROM AccessLogs";
        try (PreparedStatement pstm = connection.prepareStatement(query);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                AccessLogs accessLogs = new AccessLogs();
                accessLogs.setId(rs.getInt("id"));
                accessLogs.setTimestamp(rs.getTimestamp("timestamp"));
                accessLogs.setStatus(rs.getString("status"));
                accessLogs.setImage_snapshot(rs.getBytes("image_snapshot"));

                long userId = rs.getInt("user_id");
                Users user = getUserById(userId);
                accessLogs.setUsers(user);

                acessLogs.add(accessLogs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des logs d'accès.", e);
        }
        return acessLogs;
    }

    @Override
    public AccessLogs finById(Integer id) {
        String query = "SELECT * FROM AccessLogs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AccessLogs accessLogs = new AccessLogs();
                    accessLogs.setId(rs.getInt("id"));
                    accessLogs.setTimestamp(rs.getTimestamp("timestamp"));
                    accessLogs.setStatus(rs.getString("status"));
                    accessLogs.setImage_snapshot(rs.getBytes("image_snapshot"));

                    long userId = rs.getInt("user_id");
                    Users user = getUserById(userId);
                    accessLogs.setUsers(user);

                    return accessLogs;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du log d'accès avec ID " + id, e);
        }
        return null;
    }

    @Override
    public void save(AccessLogs accessLogs) {
        String query = "INSERT INTO AccessLogs (timestamp, status, image_snapshot, user_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setTimestamp(1, accessLogs.getTimestamp());
            pstmt.setString(2, accessLogs.getStatus());
            pstmt.setBytes(3, accessLogs.getImage_snapshot());
            if (accessLogs.getUsers() != null) {
                pstmt.setInt(4, accessLogs.getUsers().getId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du log d'accès.", e);
        }
    }

    @Override
    public void delete(AccessLogs accessLogs) {
        String query = "DELETE FROM AccessLogs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, accessLogs.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du log d'accès.", e);
        }
    }

    @Override
    public void update(AccessLogs accessLogs) {
        String query = "UPDATE AccessLogs SET timestamp = ?, status = ?, image_snapshot = ?, user_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setTimestamp(1, accessLogs.getTimestamp());
            pstmt.setString(2, accessLogs.getStatus());
            pstmt.setBytes(3, accessLogs.getImage_snapshot());
            if (accessLogs.getUsers() != null) {
                pstmt.setInt(4, accessLogs.getUsers().getId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.setInt(5, accessLogs.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du log d'accès.", e);
        }
    }

    private Users getUserById(long userId) {
        Users user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Users();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFace_data(rs.getBytes("face_data"));
                    user.setCreated_at(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur.", e);
        }
        return user;
    }

    @Override
    public List<AccessLogs> findByQuery(String query) {
        List<AccessLogs> accessLogsList = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM AccessLogs WHERE status LIKE ? ")) {
            pstmt.setString(1, "%" + query + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AccessLogs accessLogs = new AccessLogs();
                    accessLogs.setId(rs.getInt("id"));
                    accessLogs.setTimestamp(rs.getTimestamp("timestamp"));
                    accessLogs.setStatus(rs.getString("status"));
                    accessLogs.setImage_snapshot(rs.getBytes("image_snapshot"));
                    accessLogsList.add(accessLogs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche.", e);
        }
        return accessLogsList;
    }

    // Méthode pour associer un utilisateur à un AccessLogs
    public void associateUser(int accessLogId, Users user) {
        String query = "UPDATE AccessLogs SET user_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (user != null) {
                pstmt.setInt(1, user.getId());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setInt(2, accessLogId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'association de l'utilisateur avec le log d'accès.", e);
        }
    }
}
