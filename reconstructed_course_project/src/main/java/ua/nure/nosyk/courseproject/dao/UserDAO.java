package ua.nure.nosyk.courseproject.dao;
import ua.nure.nosyk.courseproject.entity.User;
import java.sql.SQLException; import java.util.List;
public interface UserDAO {
    User findByEmail(String email) throws SQLException;
    User findById(long id) throws SQLException;
    List<User> findAll() throws SQLException;
    void create(User user) throws SQLException;
    void update(User user) throws SQLException;
    void delete(long id) throws SQLException;
}
