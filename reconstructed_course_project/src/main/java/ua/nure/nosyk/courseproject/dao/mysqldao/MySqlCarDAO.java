package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;

import ua.nure.nosyk.courseproject.dao.CarDAO;
import ua.nure.nosyk.courseproject.dao.ConnectionManager;
import ua.nure.nosyk.courseproject.entity.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MySqlCarDAO implements CarDAO {

    private final ConnectionManager cm;

    public MySqlCarDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public Car findById(long id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT * FROM car WHERE id = ?")) {

            s.setLong(1, id);

            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    @Override
    public List<Car> findByOwner(long ownerId) throws SQLException {
        return find(
                "SELECT * FROM car " +
                        "WHERE owner_id = ? " +
                        "AND suspended_by_ban = FALSE " +
                        "ORDER BY id",
                ownerId
        );
    }

    @Override
    public List<Car> findAll() throws SQLException {
        return find(
                "SELECT c.* FROM car c " +
                        "JOIN user_account u ON u.id = c.owner_id " +
                        "WHERE c.suspended_by_ban = FALSE " +
                        "AND u.account_status = 'ACTIVE' " +
                        "ORDER BY c.id"
        );
    }

    @Override
    public void create(Car car) throws SQLException {
        String sql =
                "INSERT INTO car " +
                        "(vin_code, brand, model, car_year, mileage, " +
                        "vehicle_condition, owner_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            int i = 0;

            s.setString(++i, car.getVinCode());
            s.setString(++i, car.getBrand());
            s.setString(++i, car.getModel());
            s.setInt(++i, car.getYear());
            s.setInt(++i, car.getMileage());
            s.setString(++i, car.getVehicleCondition());
            s.setLong(++i, car.getOwnerId());

            s.executeUpdate();

            try (ResultSet keys = s.getGeneratedKeys()) {
                if (keys.next()) {
                    car.setId(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Car car) throws SQLException {
        String sql =
                "UPDATE car SET vin_code = ?, brand = ?, model = ?, " +
                        "car_year = ?, mileage = ?, vehicle_condition = ? " +
                        "WHERE id = ? AND owner_id = ? " +
                        "AND suspended_by_ban = FALSE";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, car.getVinCode());
            s.setString(2, car.getBrand());
            s.setString(3, car.getModel());
            s.setInt(4, car.getYear());
            s.setInt(5, car.getMileage());
            s.setString(6, car.getVehicleCondition());
            s.setLong(7, car.getId());
            s.setLong(8, car.getOwnerId());

            s.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "DELETE FROM car WHERE id = ?")) {

            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    @Override
    public void deleteByOwner(long ownerId) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "DELETE FROM car WHERE owner_id = ?")) {

            s.setLong(1, ownerId);
            s.executeUpdate();
        }
    }

    @Override
    public void suspendByOwner(long ownerId) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "UPDATE car SET suspended_by_ban = TRUE " +
                             "WHERE owner_id = ?")) {

            s.setLong(1, ownerId);
            s.executeUpdate();
        }
    }

    @Override
    public void resumeByOwner(long ownerId) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "UPDATE car SET suspended_by_ban = FALSE " +
                             "WHERE owner_id = ? AND suspended_by_ban = TRUE")) {

            s.setLong(1, ownerId);
            s.executeUpdate();
        }
    }

    private List<Car> find(String sql, Object... values)
            throws SQLException {

        List<Car> result = new ArrayList<>();

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                s.setObject(i + 1, values[i]);
            }

            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    result.add(map(r));
                }
            }
        }

        return result;
    }

    private Car map(ResultSet r) throws SQLException {
        Car car = new Car();

        car.setId(r.getLong("id"));
        car.setVinCode(r.getString("vin_code"));
        car.setBrand(r.getString("brand"));
        car.setModel(r.getString("model"));
        car.setYear(r.getInt("car_year"));
        car.setMileage(r.getInt("mileage"));
        car.setVehicleCondition(r.getString("vehicle_condition"));
        car.setOwnerId(r.getLong("owner_id"));

        return car;
    }
}