package ua.nure.nosyk.courseproject.dao;
import ua.nure.nosyk.courseproject.entity.Car;
import java.sql.SQLException; import java.util.List;
public interface CarDAO {
    Car findById(long id) throws SQLException;
    List<Car> findByOwner(long ownerId) throws SQLException;
    List<Car> findAll() throws SQLException;
    void create(Car car) throws SQLException;
    void update(Car car) throws SQLException;
    void delete(long id) throws SQLException;
    void deleteByOwner(long ownerId) throws SQLException;
    void suspendByOwner(long ownerId) throws SQLException;
    void resumeByOwner(long ownerId) throws SQLException;
}
