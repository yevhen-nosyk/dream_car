package ua.nure.nosyk.courseproject.service;

import org.springframework.stereotype.Service;
import ua.nure.nosyk.courseproject.dao.UserDAO;
import ua.nure.nosyk.courseproject.entity.User;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private final UserDAO dao;

    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public User authenticate(String email, String password) throws SQLException {
        User u = dao.findByEmail(email);
        if (u == null || !password.equals(u.getPassword())) return null;
        return u;
    }

    public void register(User u) throws SQLException {
        dao.create(u);
    }

    public User findByEmail(String email) throws SQLException {
        return dao.findByEmail(email);
    }

    public User find(long id) throws SQLException {
        return dao.findById(id);
    }

    public List<User> all() throws SQLException {
        return dao.findAll();
    }

    public void update(User u) throws SQLException {
        dao.update(u);
    }
}