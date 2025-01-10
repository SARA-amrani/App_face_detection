package ma.enset.face_detection.dao;

import ma.enset.face_detection.SQLiteConnection;
import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcessLogsDaoImp implements AccessLogsDao{
    Connection connection = SQLiteConnection.connect();

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
            throw new RuntimeException("Erreur lors de la récupération de toutes les utilisateurs.", e);
        }

        return acessLogs;
    }

    @Override
    public AccessLogs finById(Integer id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM AccessLogs WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
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
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'un access logs avec ID " + id, e);
        }
        return null;
    }

    @Override
    public void save(AccessLogs accessLogs) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO AccessLogs (timestamp, status, image_snapshot, user_id) VALUES (?, ?, ?, ?)")) {
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
            throw new RuntimeException("Erreur lors de l'enregistrement de cet access logs.", e);
        }
    }


    @Override
    public void delete(AccessLogs accessLogs) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM AccessLogs WHERE id = ?");
            pstmt.setLong(1, accessLogs.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de ce access logs " + e);
        }
    }

    @Override
    public void update(AccessLogs accessLogs) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE AccessLogs SET timestamp = ?, status = ?, image_snapshot = ?, user_id = ? WHERE id = ?")) {
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
            throw new RuntimeException("Erreur lors de la mise à jour de l'access logs avec ID " + accessLogs.getId(), e);
        }
    }


    private Users getUserById(long userId) {
        Users users = null;
        try {
            String query = "SELECT * FROM client WHERE id_client = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                users = new Users();
                users.setId(rs.getInt("id"));
                users.setUsername(rs.getString("username"));
                users.setEmail(rs.getString("email"));
                users.setFace_data(rs.getBytes("face_data"));
                users.setCreated_at(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur.", e);
        }
        return users;
    }


    @Override
    public List<AccessLogs> findByQuery(String query) {
        List<AccessLogs> accessLogsList = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM AccessLogs WHERE status LIKE ? ");
            pstmt.setString(1, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                AccessLogs accessLogs = new AccessLogs();
                accessLogs.setId(rs.getInt("id"));
                accessLogs.setTimestamp(rs.getTimestamp("timestamp"));
                accessLogs.setStatus(rs.getString("status"));
                accessLogs.setImage_snapshot(rs.getBytes("image_snapshot"));
                accessLogsList.add(accessLogs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche " + e);
        }
        return accessLogsList;
    }

    public void associateUser(int accessLogId, Users user) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE AccessLogs SET user_id = ? WHERE id = ?")) {
            if (user != null) {
                pstmt.setInt(1, user.getId());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setInt(2, accessLogId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'association de l'utilisateur avec AccessLogs ID " + accessLogId, e);
        }
    }

}
