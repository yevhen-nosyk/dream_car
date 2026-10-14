package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;
import ua.nure.nosyk.courseproject.dao.*;
import ua.nure.nosyk.courseproject.entity.Advertisement;

import java.sql.*;
import java.util.*;

@Repository
public class MySqlAdvertisementDAO implements AdvertisementDAO {
    private final ConnectionManager cm;

    public MySqlAdvertisementDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    public Advertisement findById(long id) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("select * from advertisement where id=?")) {
            s.setLong(1, id);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }


    @Override
    public List<Advertisement> findAllActive() throws SQLException {
        String sql =
                "SELECT a.* " +
                        "FROM advertisement a " +
                        "JOIN user_account u ON u.id = a.owner_id " +
                        "LEFT JOIN car c ON c.id = a.car_id " +
                        "WHERE a.status = 'ACTIVE' " +
                        "AND a.suspended_by_ban = FALSE " +
                        "AND u.account_status = 'ACTIVE' " +
                        "AND c.suspended_by_ban = FALSE " +
                        "ORDER BY a.creation_date DESC";

        return find(sql);
    }

    @Override
    public List<Advertisement> findByOwner(long id) throws SQLException {
        return find(
                "SELECT * FROM advertisement " +
                        "WHERE owner_id = ? " +
                        "AND suspended_by_ban = FALSE " +
                        "ORDER BY creation_date DESC",
                id
        );
    }

    @Override
    public void suspendByOwner(long ownerId) throws SQLException {
        String sql =
                "UPDATE advertisement " +
                        "SET suspended_by_ban = TRUE " +
                        "WHERE owner_id = ?";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setLong(1, ownerId);
            s.executeUpdate();
        }
    }

    @Override
    public void resumeByOwner(long ownerId) throws SQLException {
        String sql =
                "UPDATE advertisement " +
                        "SET suspended_by_ban = FALSE " +
                        "WHERE owner_id = ? " +
                        "AND suspended_by_ban = TRUE";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setLong(1, ownerId);
            s.executeUpdate();
        }
    }

    private List<Advertisement> find(String q, Object... a) throws SQLException {
        List<Advertisement> o = new ArrayList<>();
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            for (int i = 0; i < a.length; i++) s.setObject(i + 1, a[i]);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) o.add(map(r));
            }
        }
        return o;
    }

    public void create(Advertisement a) throws SQLException {
        String q = "insert into advertisement(description,creation_date,status,owner_id,car_id) values(?,?,?,?,?)";
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            s.setString(++i, a.getDescription());
            s.setTimestamp(++i, Timestamp.valueOf(a.getCreationDate()));
            s.setString(++i, a.getStatus());
            s.setLong(++i, a.getOwnerId());
            if (a.getCarId() == null) s.setNull(++i, Types.BIGINT);
            else s.setLong(++i, a.getCarId());
            s.executeUpdate();
            try (ResultSet k = s.getGeneratedKeys()) {
                if (k.next()) a.setId(k.getLong(1));
            }
        }
    }

    public void update(Advertisement a) throws SQLException {
        String q = "update advertisement set description=?,status=?,car_id=? where id=?";
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            int i = 0;
            s.setString(++i, a.getDescription());
            s.setString(++i, a.getStatus());
            if (a.getCarId() == null) s.setNull(++i, Types.BIGINT);
            else s.setLong(++i, a.getCarId());
            s.setLong(++i, a.getId());
            s.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("delete from advertisement where id=?")) {
            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    public void deleteByOwner(long id) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("delete from advertisement where owner_id=?")) {
            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    private Advertisement map(ResultSet r) throws SQLException {
        Advertisement a = new Advertisement();
        a.setId(r.getLong("id"));
        a.setDescription(r.getString("description"));
        a.setCreationDate(r.getTimestamp("creation_date").toLocalDateTime());
        a.setStatus(r.getString("status"));
        a.setOwnerId(r.getLong("owner_id"));
        long c = r.getLong("car_id");
        a.setCarId(r.wasNull() ? null : c);
        return a;
    }
}
