package ua.nure.nosyk.courseproject.dao;
import ua.nure.nosyk.courseproject.entity.Advertisement;
import java.sql.SQLException; import java.util.List;
public interface AdvertisementDAO {
    Advertisement findById(long id) throws SQLException;
    List<Advertisement> findAllActive() throws SQLException;
    List<Advertisement> findByOwner(long ownerId) throws SQLException;
    void create(Advertisement ad) throws SQLException;
    void update(Advertisement ad) throws SQLException;
    void delete(long id) throws SQLException;
    void deleteByOwner(long ownerId) throws SQLException;
    void suspendByOwner(long ownerId) throws SQLException;
    void resumeByOwner(long ownerId) throws SQLException;
}
