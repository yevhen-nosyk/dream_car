package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;
import ua.nure.nosyk.courseproject.dao.*;
import ua.nure.nosyk.courseproject.entity.Office;

import java.sql.*;
import java.util.*;

@Repository
public class MySqlOfficeDAO implements OfficeDAO {
    private final ConnectionManager cm;

    public MySqlOfficeDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    public List<Office> findAll() throws SQLException {
        List<Office> o = new ArrayList<>();
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("select * from office order by id"); ResultSet r = s.executeQuery()) {
            while (r.next()) {
                Office x = new Office();
                x.setId(r.getLong("id"));
                x.setAddress(r.getString("address"));
                x.setWorkHours(r.getString("work_hours"));
                x.setAccessability(r.getString("accessability"));
                o.add(x);
            }
        }
        return o;
    }
}
