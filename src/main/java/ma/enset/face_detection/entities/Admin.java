package ma.enset.face_detection.entities;

import java.util.List;

public class Admin {
    private int id;
    private String username;
    private List<Users> users;

    //Constructeurs
    public Admin() {
    }

    public Admin(int id, String username, List<Users> users) {
        this.id = id;
        this.username = username;
        this.users = users;
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

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    //ToString

    @Override
    public String toString() {
        return "Admins{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", users=" + users +
                '}';
    }
}

