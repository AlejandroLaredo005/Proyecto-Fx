package dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T, K> {
    void save(T entity);
    void update(T entity);
    void delete(K id);
    Optional<T> findById(K id);
    List<T> findAll();
}
