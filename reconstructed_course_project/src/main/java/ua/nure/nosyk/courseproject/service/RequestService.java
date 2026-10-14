package ua.nure.nosyk.courseproject.service;

import org.springframework.stereotype.Service;
import ua.nure.nosyk.courseproject.dao.RequestDAO;
import ua.nure.nosyk.courseproject.entity.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {
    private final RequestDAO dao;

    public RequestService(RequestDAO dao) {
        this.dao = dao;
    }

    public void create(long sender, long receiver, Long car, int sum, String payment) throws SQLException {
        Request r = new Request();
        r.setSenderId(sender);
        r.setReceiverId(receiver);
        r.setProposedCarId(car);
        r.setSendingDate(LocalDateTime.now());
        r.setStatus("SENT");
        Deposit d = new Deposit();
        d.setSum(sum);
        d.setPaymentMethod(payment);
        d.setStatus(sum > 0 ? "PENDING" : "NONE");
        r.setDeposit(d);
        dao.create(r);
    }

    public List<Request> sent(long id) throws SQLException {
        return dao.findBySender(id);
    }

    public List<Request> received(long id) throws SQLException {
        return dao.findByReceiver(id);
    }

    public ua.nure.nosyk.courseproject.entity.Request find(long id) throws SQLException {
        return dao.findById(id);
    }

    public void status(long id, String status) throws SQLException {
        dao.updateStatus(id, status);
    }

    public void deleteByUser(long id) throws SQLException {
        dao.deleteByUser(id);
    }

    public void cancelPendingByUser(long userId) throws SQLException{
        dao.cancelPendingByUser(userId);
    }
}