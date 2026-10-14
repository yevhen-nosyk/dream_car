package ua.nure.nosyk.courseproject.service;

import org.springframework.stereotype.Service;
import ua.nure.nosyk.courseproject.dao.CarDAO;
import ua.nure.nosyk.courseproject.entity.Car;

import java.sql.SQLException;
import java.util.List;

@Service
public class CarService {
    private final CarDAO dao;

    public CarService(CarDAO dao) {
        this.dao = dao;
    }

    public void create(Car c) throws SQLException {
        dao.create(c);
    }

    public List<Car> mine(long id) throws SQLException {
        return dao.findByOwner(id);
    }

    public List<Car> all() throws SQLException {
        return dao.findAll();
    }

    public Car find(long id) throws SQLException {
        return dao.findById(id);
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