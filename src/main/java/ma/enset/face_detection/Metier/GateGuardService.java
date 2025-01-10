package ma.enset.face_detection.Metier;
import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;
import java.util.List;

public interface GateGuardService {
    //Users
    List<Users> getAllUsers();
    void addUser(Users users);
    void deleteUser(Users users);
    void updateUser(Users users);
    List<Users> findUserByQuery(String query);

    //Access logs
    List<AccessLogs> getAllAccessLogs();
    void addAccessLogs(AccessLogs accessLogs);
    void deleteAcessLogs(AccessLogs accessLogs);
    void updateAccessLogs(AccessLogs accessLogs);
    List<AccessLogs> findAcessLogsByQuery(String query);

}
