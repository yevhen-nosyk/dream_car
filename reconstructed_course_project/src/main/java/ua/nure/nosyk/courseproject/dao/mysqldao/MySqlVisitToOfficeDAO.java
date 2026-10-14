package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;
import ua.nure.nosyk.courseproject.dao.*;

import java.sql.*;

@Repository
public class MySqlVisitToOfficeDAO implements VisitToOfficeDAO {
    private final ConnectionManager cm;

    public MySqlVisitToOfficeDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    public void create(long userId, long officeId) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("insert into visit_to_office(user_id,office_id) values(?,?)")) {
            s.setLong(1, userId);
            s.setLong(2, officeId);
            s.executeUpdate();
        }
    }
}
