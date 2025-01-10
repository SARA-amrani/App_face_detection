package ma.enset.face_detection.entities;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Arrays;

public class AccessLogs {
    private int id;
    private Timestamp timestamp;
    private String status;
    private byte[] image_snapshot;
    private Users users;

    //Constructeurs
    public AccessLogs() {
    }
    public AccessLogs(int id, Timestamp timestamp, String status, byte[] image_snapshot, Users users) {
        this.id = id;
        this.timestamp = timestamp;
        this.status = status;
        this.image_snapshot = image_snapshot;
        this.users = users;
    }

    //getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getImage_snapshot() {
        return image_snapshot;
    }

    public void setImage_snapshot(byte[] image_snapshot) {
        this.image_snapshot = image_snapshot;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    //toString

    @Override
    public String toString() {
        return "AccessLogs{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                ", image_snapshot=" + Arrays.toString(image_snapshot) +
                ", users=" + users +
                '}';
    }
}

