package ma.enset.face_detection.dao;

import ma.enset.face_detection.entities.AccessLogs;
import ma.enset.face_detection.entities.Users;

import java.util.List;

public interface AccessLogsDao extends Dao<AccessLogs,Integer> {
    List<AccessLogs> findByQuery(String query);

}
