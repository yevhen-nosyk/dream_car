package ua.nure.nosyk.courseproject.dao;
import ua.nure.nosyk.courseproject.entity.Request;
import java.sql.SQLException; import java.util.List;
public interface RequestDAO {
    Request findById(long id) throws SQLException;
    List<Request> findBySender(long userId) throws SQLException;
    List<Request> findByReceiver(long userId) throws SQLException;
    void create(Request request) throws SQLException;
    void updateStatus(long id, String status) throws SQLException;
    void deleteByUser(long userId) throws SQLException;
    void cancelPendingByUser(long userId) throws SQLException;
}
