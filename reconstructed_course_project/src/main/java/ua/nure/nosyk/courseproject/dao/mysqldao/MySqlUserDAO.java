package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;
import ua.nure.nosyk.courseproject.dao.*;
import ua.nure.nosyk.courseproject.entity.User;

import java.sql.*;
import java.util.*;

@Repository
public class MySqlUserDAO implements UserDAO {
    private final ConnectionManager cm;

    public MySqlUserDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    public User findByEmail(String e) throws SQLException {
        String q = "select * from user_account where email=?";
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setString(1, e);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public User findById(long id) throws SQLException {
        String q = "select * from user_account where id=?";
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setLong(1, id);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> o = new ArrayList<>();
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("select * from user_account order by id"); ResultSet r = s.executeQuery()) {
            while (r.next()) o.add(map(r));
        }
        return o;
    }

    public void create(User u) throws SQLException {
        String q = "insert into user_account(role,name,email,password,phone,passport,address,rating) values(?,?,?,?,?,?,?,?)";
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            s.setString(++i, u.getRole());
            s.setString(++i, u.getName());
            s.setString(++i, u.getEmail());
            s.setString(++i, u.getPassword());
            s.setString(++i, u.getPhone());
            s.setString(++i, u.getPassport());
            s.setString(++i, u.getAddress());
            s.setFloat(++i, u.getRating());
            s.executeUpdate();
            try (ResultSet k = s.getGeneratedKeys()) {
                if (k.next()) u.setId(k.getLong(1));
            }
        }
    }

    @Override
    public void update(User u) throws SQLException {
        String sql =
                "UPDATE user_account " +
                        "SET role=?, account_status=?, name=?, email=?, password=?, " +
                        "phone=?, passport=?, address=?, rating=? " +
                        "WHERE id=?";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            int i = 0;

            s.setString(++i, u.getRole());
            s.setString(++i, u.getAccountStatus());
            s.setString(++i, u.getName());
            s.setString(++i, u.getEmail());
            s.setString(++i, u.getPassword());
            s.setString(++i, u.getPhone());
            s.setString(++i, u.getPassport());
            s.setString(++i, u.getAddress());
            s.setFloat(++i, u.getRating());
            s.setLong(++i, u.getId());

            s.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("delete from user_account where id=?")) {
            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    private User map(ResultSet r) throws SQLException {
        User u = new User();
        u.setId(r.getLong("id"));
        u.setRole(r.getString("role"));
        u.setAccountStatus(r.getString("account_status"));
        u.setName(r.getString("name"));
        u.setEmail(r.getString("email"));
        u.setPassword(r.getString("password"));
        u.setPhone(r.getString("phone"));
        u.setPassport(r.getString("passport"));
        u.setAddress(r.getString("address"));
        u.setRating(r.getFloat("rating"));
        return u;
    }
}
