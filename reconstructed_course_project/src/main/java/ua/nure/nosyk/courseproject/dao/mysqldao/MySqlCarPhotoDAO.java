package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;

import ua.nure.nosyk.courseproject.dao.CarPhotoDAO;
import ua.nure.nosyk.courseproject.dao.ConnectionManager;
import ua.nure.nosyk.courseproject.entity.CarPhoto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MySqlCarPhotoDAO implements CarPhotoDAO {

    private final ConnectionManager cm;

    public MySqlCarPhotoDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public void create(CarPhoto photo) throws SQLException {
        String sql =
                "INSERT INTO car_photo " +
                        "(car_id, stored_filename, content_type, size_bytes, sort_order) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            s.setLong(1, photo.getCarId());
            s.setString(2, photo.getStoredFilename());
            s.setString(3, photo.getContentType());
            s.setLong(4, photo.getSizeBytes());
            s.setInt(5, photo.getSortOrder());

            s.executeUpdate();

            try (ResultSet keys = s.getGeneratedKeys()) {
                if (keys.next()) {
                    photo.setId(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public List<CarPhoto> findByCarId(long carId) throws SQLException {
        String sql =
                "SELECT id, car_id, stored_filename, content_type, " +
                        "size_bytes, sort_order " +
                        "FROM car_photo WHERE car_id = ? " +
                        "ORDER BY sort_order, id";

        List<CarPhoto> photos = new ArrayList<>();

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setLong(1, carId);

            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    photos.add(map(r));
                }
            }
        }

        return photos;
    }

    @Override
    public CarPhoto findPublicByFilename(String filename)
            throws SQLException {

        String sql =
                "SELECT p.id, p.car_id, p.stored_filename, p.content_type, " +
                        "p.size_bytes, p.sort_order " +
                        "FROM car_photo p " +
                        "JOIN car c ON c.id = p.car_id " +
                        "JOIN user_account u ON u.id = c.owner_id " +
                        "WHERE p.stored_filename = ? " +
                        "AND c.suspended_by_ban = FALSE " +
                        "AND u.account_status = 'ACTIVE'";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, filename);

            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    @Override
    public void deleteByCarId(long carId) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "DELETE FROM car_photo WHERE car_id = ?")) {

            s.setLong(1, carId);
            s.executeUpdate();
        }
    }

    private CarPhoto map(ResultSet r) throws SQLException {
        CarPhoto photo = new CarPhoto();

        photo.setId(r.getLong("id"));
        photo.setCarId(r.getLong("car_id"));
        photo.setStoredFilename(r.getString("stored_filename"));
        photo.setContentType(r.getString("content_type"));
        photo.setSizeBytes(r.getLong("size_bytes"));
        photo.setSortOrder(r.getInt("sort_order"));

        return photo;
    }
}