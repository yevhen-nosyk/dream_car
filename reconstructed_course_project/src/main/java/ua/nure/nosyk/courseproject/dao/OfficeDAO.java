package ua.nure.nosyk.courseproject.dao;
import ua.nure.nosyk.courseproject.entity.Office;
import java.sql.SQLException; import java.util.List;
public interface OfficeDAO {
    List<Office> findAll() throws SQLException;
}
