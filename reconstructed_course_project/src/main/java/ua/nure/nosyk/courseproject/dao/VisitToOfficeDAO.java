package ua.nure.nosyk.courseproject.dao;
import java.sql.SQLException;
public interface VisitToOfficeDAO {
    void create(long userId, long officeId) throws SQLException;
}
