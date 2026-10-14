package ua.nure.nosyk.courseproject.service;

import org.springframework.stereotype.Service;
import ua.nure.nosyk.courseproject.entity.User;

import java.sql.SQLException;

@Service
public class AdminService {

    private final UserService users;
    private final AdvertisementService ads;
    private final CarService cars;
    private final RequestService requests;

    public AdminService(
            UserService users,
            AdvertisementService ads,
            CarService cars,
            RequestService requests) {

        this.users = users;
        this.ads = ads;
        this.cars = cars;
        this.requests = requests;
    }
    public void ban(long targetId, long actorId) throws SQLException {
        User target = users.find(targetId);
        if (target == null) {
            return;
        }
        if (targetId == actorId) {
            return;
        }
        if ("ADMIN".equals(target.getRole())) {
            return;
        }
        target.setAccountStatus("BANNED");
        users.update(target);
        ads.suspendByOwner(targetId);
        cars.suspendByOwner(targetId);
        requests.cancelPendingByUser(targetId);
    }

    public void unban(long targetId) throws SQLException {
        User target = users.find(targetId);
        if (target == null) {
            return;
        }

        if (!"BANNED".equals(target.getAccountStatus())) {
            return;
        }

        if ("ADMIN".equals(target.getRole())) {
            return;
        }
        ads.resumeByOwner(targetId);
        cars.resumeByOwner(targetId);

        target.setAccountStatus("ACTIVE");
        users.update(target);
    }
}