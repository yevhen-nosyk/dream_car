package ua.nure.nosyk.courseproject.dao;

import ua.nure.nosyk.courseproject.entity.CarPhoto;

import java.sql.SQLException;
import java.util.List;

public interface CarPhotoDAO {

    void create(CarPhoto photo) throws SQLException;

    List<CarPhoto> findByCarId(long carId) throws SQLException;

    CarPhoto findPublicByFilename(String filename) throws SQLException;

    void deleteByCarId(long carId) throws SQLException;
}