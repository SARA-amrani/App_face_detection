package ma.enset.face_detection.dao;

import ma.enset.face_detection.SQLiteConnection;
import ma.enset.face_detection.entities.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDaoImp implements UsersDao{
    Connection connection = SQLiteConnection.connect();

    @Override
    public List<Users> findAll() {
        List<Users> usersList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (PreparedStatement pstm = connection.prepareStatement(query);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Users users = new Users();
                users.setId(rs.getInt("id"));
                users.setUsername(rs.getString("username"));
                users.setEmail(rs.getString("email"));
                users.setFace_data(rs.getBytes("face_data"));
                users.setCreated_at(rs.getTimestamp("created_at"));
                usersList.add(users);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les utilisateurs.", e);
        }

        return usersList;
    }

    @Override
    public Users finById(Integer id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Users users = new Users();
                users.setId(rs.getInt("id"));
                users.setUsername(rs.getString("username"));
                users.setEmail(rs.getString("email"));
                users.setFace_data(rs.getBytes("face_data"));
                users.setCreated_at(rs.getTimestamp("created_at"));
                return users;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'un utilisateurs avec ID " + id, e);
        }
        return null;
    }

    @Override
    public void save(Users users) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, email, face_data, created_at) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, users.getUsername());
            pstmt.setString(2, users.getEmail());
            pstmt.setBytes(3, users.getFace_data());
            pstmt.setTimestamp(4, users.getCreated_at());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de ce utilisateur.", e);
        }
    }

    @Override
    public void delete(Users users) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            pstmt.setLong(1, users.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de ce utilisateur " + e);
        }
    }

    @Override
    public void update(Users users) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE chercheur SET username = ?, email = ?, face_data = ?, created_at = ? WHERE id = ?");
            pstmt.setString(1, users.getUsername());
            pstmt.setString(2, users.getEmail());
            pstmt.setBytes(3, users.getFace_data());
            pstmt.setTimestamp(4, users.getCreated_at());
            pstmt.setLong(5, users.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur avec ID " + users.getId(), e);
        }
    }


    @Override
    public List<Users> findByQuery(String query) {
        List<Users> usersList = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username LIKE ? ");
            pstmt.setString(1, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Users users = new Users();
                users.setId(rs.getInt("id"));
                users.setUsername(rs.getString("username"));
                users.setEmail(rs.getString("email"));
                users.setFace_data(rs.getBytes("face_data"));
                users.setCreated_at(rs.getTimestamp("created_at"));
                usersList.add(users);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche " + e);
        }
        return usersList;
    }
}

