package ma.enset.face_detection.dao;

import ma.enset.face_detection.SQLiteConnection;

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
                users.setFace_data(rs.getByte("face_data"));
                users.setCreated_at(rs.getTimestamp("created_at"));
                usersList.add(users);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les utilisateurs.", e);
        }

        return usersList;
    }

    @Override
    public Users finById(Long id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Chercheur chercheur = new Chercheur();
                chercheur.setId_chercheur(rs.getLong("id_chercheur"));
                chercheur.setNom(rs.getString("nom"));
                chercheur.setPrenom(rs.getString("prenom"));
                chercheur.setEmail(rs.getString("email"));
                chercheur.setSpecialite(rs.getString("specialite"));
                return chercheur;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de chercheur avec ID " + id, e);
        }
        return null;
    }

    @Override
    public void save(Chercheur chercheur) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO chercheur (nom, prenom, email, specialite) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, chercheur.getNom());
            pstmt.setString(2, chercheur.getPrenom());
            pstmt.setString(3, chercheur.getEmail());
            pstmt.setString(4, chercheur.getSpecialite());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de ce chercheur.", e);
        }
    }

    @Override
    public void delete(Chercheur chercheur) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM chercheur WHERE id_chercheur = ?");
            pstmt.setLong(1, chercheur.getId_chercheur());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de ce chercheur " + e);
        }
    }

    @Override
    public void update(Chercheur chercheur) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE chercheur SET nom = ?, prenom = ?, email = ?, specialite = ? WHERE id_chercheur = ?");
            pstmt.setString(1, chercheur.getNom());
            pstmt.setString(2, chercheur.getPrenom());
            pstmt.setString(3, chercheur.getEmail());
            pstmt.setString(4, chercheur.getSpecialite());
            pstmt.setLong(5, chercheur.getId_chercheur());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de chercheur avec ID " + chercheur.getId_chercheur(), e);
        }
    }


}

