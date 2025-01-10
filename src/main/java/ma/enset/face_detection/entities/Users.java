package ma.enset.face_detection.entities;

import java.sql.Timestamp;
import java.util.Arrays;

public class Users {
    private int id;
    private String username;
    private String email;
    private byte[] face_data;
    private Timestamp created_at;

    //Constructeurs
    public Users() {
    }

    public Users(int id, String username, String email, byte[] face_data, Timestamp created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.face_data = face_data;
        this.created_at = created_at;
    }

    //Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getFace_data() {
        return face_data;
    }

    public void setFace_data(byte[] face_data) {
        this.face_data = face_data;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    //ToString
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", face_data=" + Arrays.toString(face_data) +
                ", created_at=" + created_at +
                '}';
    }
}

