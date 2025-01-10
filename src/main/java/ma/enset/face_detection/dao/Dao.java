package ma.enset.face_detection.dao;

import java.util.List;

public interface Dao<T,ID> {
    List<T> findAll();
    T finById(ID id);
    void save(T entity);
    void delete(T entity);
    void update(T entity);
}
