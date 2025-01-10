package ma.enset.face_detection.Metier;

import ma.enset.face_detection.dao.AcessLogsDaoImp;
import ma.enset.face_detection.dao.UsersDaoImp;
import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;

import java.util.List;

public class GateGuardServiceImp implements GateGuardService{
    private UsersDaoImp usersDaoImp = new UsersDaoImp();
    private AcessLogsDaoImp acessLogsDaoImp = new AcessLogsDaoImp();

    //constructeurs
    public GateGuardServiceImp() {
    }
    public GateGuardServiceImp(UsersDaoImp usersDaoImp, AcessLogsDaoImp acessLogsDaoImp) {
        this.usersDaoImp = usersDaoImp;
        this.acessLogsDaoImp = acessLogsDaoImp;
    }

    //Users ==============================================
    @Override
    public List<Users> getAllUsers() {
        return usersDaoImp.findAll();
    }

    @Override
    public void addUser(Users users) {
        usersDaoImp.save(users);
    }

    @Override
    public void deleteUser(Users users) {
        usersDaoImp.delete(users);
    }

    @Override
    public void updateUser(Users users) {
        usersDaoImp.update(users);
    }

    @Override
    public List<Users> findUserByQuery(String query) {
        return usersDaoImp.findByQuery(query);
    }

    //Acess logs ==============================================

    @Override
    public List<AccessLogs> getAllAccessLogs() {
        return acessLogsDaoImp.findAll();
    }

    @Override
    public void addAccessLogs(AccessLogs accessLogs) {
        acessLogsDaoImp.save(accessLogs);
    }

    @Override
    public void deleteAcessLogs(AccessLogs accessLogs) {
        acessLogsDaoImp.delete(accessLogs);
    }

    @Override
    public void updateAccessLogs(AccessLogs accessLogs) {
        acessLogsDaoImp.update(accessLogs);
    }

    @Override
    public List<AccessLogs> findAcessLogsByQuery(String query) {
        return acessLogsDaoImp.findByQuery(query);
    }
}
