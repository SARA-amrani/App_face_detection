package ma.enset.face_detection.dao;

import ma.enset.face_detection.entities.Users;

import java.util.List;

public interface UsersDao extends Dao<Users, Integer>{
    List<Users> findByQuery(String query);
}
