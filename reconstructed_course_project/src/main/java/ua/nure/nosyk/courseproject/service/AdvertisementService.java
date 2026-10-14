package ua.nure.nosyk.courseproject.service;

import org.springframework.stereotype.Service;
import ua.nure.nosyk.courseproject.dao.AdvertisementDAO;
import ua.nure.nosyk.courseproject.entity.Advertisement;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertisementService {
    private final AdvertisementDAO dao;

    public AdvertisementService(AdvertisementDAO dao) {
        this.dao = dao;
    }

    public List<Advertisement> active() throws SQLException {
        return dao.findAllActive();
    }

    public List<Advertisement> mine(long id) throws SQLException {
        return dao.findByOwner(id);
    }

    public Advertisement find(long id) throws SQLException {
        return dao.findById(id);
    }

    public void create(Advertisement a) throws SQLException {
        a.setCreationDate(LocalDateTime.now());
        a.setStatus("ACTIVE");
        dao.create(a);
    }

    public void update(Advertisement a) throws SQLException {
        dao.update(a);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
    }

    public void deleteByOwner(long id) throws SQLException {
        dao.deleteByOwner(id);
    }

    public void suspendByOwner(long ownerId) throws SQLException{
        dao.suspendByOwner(ownerId);
    }
    public void resumeByOwner(long ownerId) throws SQLException{
        dao.resumeByOwner(ownerId);
    }
}