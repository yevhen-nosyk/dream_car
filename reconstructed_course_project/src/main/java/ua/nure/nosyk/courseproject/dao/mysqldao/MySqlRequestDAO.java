package ua.nure.nosyk.courseproject.dao.mysqldao;

import org.springframework.stereotype.Repository;
import ua.nure.nosyk.courseproject.dao.*;
import ua.nure.nosyk.courseproject.entity.*;

import java.sql.*;
import java.util.*;

@Repository
public class MySqlRequestDAO implements RequestDAO {
    private final ConnectionManager cm;

    public MySqlRequestDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    public Request findById(long id) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("select * from request where id=?")) {
            s.setLong(1, id);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public List<Request> findBySender(long id) throws SQLException {
        return find("select * from request where sender_id=? order by sending_date desc", id);
    }

    public List<Request> findByReceiver(long id) throws SQLException {
        return find("select * from request where receiver_id=? order by sending_date desc", id);
    }

    @Override
    public void cancelPendingByUser(long userId) throws SQLException {
        String sql =
                "UPDATE request " +
                        "SET status = 'CANCELLED' " +
                        "WHERE status = 'SENT' " +
                        "AND (sender_id = ? OR receiver_id = ?)";

        try (Connection c = cm.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setLong(1, userId);
            s.setLong(2, userId);
            s.executeUpdate();
        }
    }

    private List<Request> find(String q, Object... a) throws SQLException {
        List<Request> o = new ArrayList<>();
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            for (int i = 0; i < a.length; i++) s.setObject(i + 1, a[i]);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) o.add(map(r));
            }
        }
        return o;
    }

    public void create(Request x) throws SQLException {
        String q = "insert into request(status,sending_date,proposed_car_id,receiver_id,sender_id,deposit_sum,deposit_status,payment_method) values(?,?,?,?,?,?,?,?)";
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            s.setString(++i, x.getStatus());
            s.setTimestamp(++i, Timestamp.valueOf(x.getSendingDate()));
            if (x.getProposedCarId() == null) s.setNull(++i, Types.BIGINT);
            else s.setLong(++i, x.getProposedCarId());
            s.setLong(++i, x.getReceiverId());
            s.setLong(++i, x.getSenderId());
            Deposit d = x.getDeposit();
            s.setInt(++i, d == null ? 0 : d.getSum());
            s.setString(++i, d == null ? "NONE" : d.getStatus());
            s.setString(++i, d == null ? null : d.getPaymentMethod());
            s.executeUpdate();
            try (ResultSet k = s.getGeneratedKeys()) {
                if (k.next()) x.setId(k.getLong(1));
            }
        }
    }

    public void updateStatus(long id, String status) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("update request set status=? where id=?")) {
            s.setString(1, status);
            s.setLong(2, id);
            s.executeUpdate();
        }
    }

    public void deleteByUser(long id) throws SQLException {
        try (Connection c = cm.getConnection(); PreparedStatement s = c.prepareStatement("delete from request where sender_id=? or receiver_id=?")) {
            s.setLong(1, id);
            s.setLong(2, id);
            s.executeUpdate();
        }
    }

    private Request map(ResultSet r) throws SQLException {
        Request x = new Request();
        x.setId(r.getLong("id"));
        x.setStatus(r.getString("status"));
        x.setSendingDate(r.getTimestamp("sending_date").toLocalDateTime());
        long car = r.getLong("proposed_car_id");
        x.setProposedCarId(r.wasNull() ? null : car);
        x.setReceiverId(r.getLong("receiver_id"));
        x.setSenderId(r.getLong("sender_id"));
        Deposit d = new Deposit();
        d.setSum(r.getInt("deposit_sum"));
        d.setStatus(r.getString("deposit_status"));
        d.setPaymentMethod(r.getString("payment_method"));
        x.setDeposit(d);
        return x;
    }
}
