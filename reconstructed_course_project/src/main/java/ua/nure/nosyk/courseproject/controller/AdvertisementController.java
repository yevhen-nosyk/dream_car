package ua.nure.nosyk.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.nure.nosyk.courseproject.entity.Advertisement;
import ua.nure.nosyk.courseproject.entity.Car;
import ua.nure.nosyk.courseproject.entity.CarPhoto;
import ua.nure.nosyk.courseproject.entity.User;
import ua.nure.nosyk.courseproject.form.AdvertisementForm;
import ua.nure.nosyk.courseproject.service.AdvertisementService;
import ua.nure.nosyk.courseproject.service.CarPhotoService;
import ua.nure.nosyk.courseproject.service.CarService;
import ua.nure.nosyk.courseproject.service.UserService;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdvertisementController {

    private final AdvertisementService ads;
    private final CarService cars;
    private final CarPhotoService photos;
    private final UserService users;

    public AdvertisementController(AdvertisementService ads, CarService cars, CarPhotoService photos, UserService users) {

        this.ads = ads;
        this.cars = cars;
        this.photos = photos;
        this.users = users;
    }

    private User user(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    @GetMapping({"/advertisements", "/ads"})
    public String all(Model model, HttpSession session) throws Exception {

        List<Advertisement> listings = ads.active();

        model.addAttribute("advertisements", listings);
        model.addAttribute("currentUser", user(session));

        populateDisplayData(listings, model);

        return "advertisements";
    }

    @GetMapping("/myAds")
    public String mine(Model model, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        List<Advertisement> listings = ads.mine(u.getId());

        model.addAttribute("advertisements", listings);
        model.addAttribute("currentUser", u);

        populateDisplayData(listings, model);

        return "myAdvertisements";
    }

    @GetMapping("/addAdvertisement")
    public String add(Model model, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        model.addAttribute("advertisementForm", new AdvertisementForm());

        model.addAttribute("cars", cars.mine(u.getId()));

        return "addAdvertisementPage";
    }

    @PostMapping("/addAdvertisement")
    public String add(@ModelAttribute AdvertisementForm form, Model model, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        model.addAttribute("advertisementForm", form);
        model.addAttribute("cars", cars.mine(u.getId()));

        if (form.getDescription() == null || form.getDescription().trim().isEmpty()) {

            model.addAttribute("errorMessage", "Please provide a description.");

            return "addAdvertisementPage";
        }

        if (form.getDescription().trim().length() > 5000) {
            model.addAttribute("errorMessage", "The description must be no longer than 5000 characters.");

            return "addAdvertisementPage";
        }

        if (form.getCarId() == null) {
            model.addAttribute("errorMessage", "Please select a car for your advertisement.");

            return "addAdvertisementPage";
        }

        Car selectedCar = cars.find(form.getCarId());

        if (selectedCar == null || !java.util.Objects.equals(selectedCar.getOwnerId(), u.getId())) {

            model.addAttribute("errorMessage", "Please select one of your own available cars.");

            return "addAdvertisementPage";
        }

        Advertisement advertisement = new Advertisement();

        advertisement.setOwnerId(u.getId());
        advertisement.setDescription(form.getDescription().trim());
        advertisement.setCarId(selectedCar.getId());

        ads.create(advertisement);

        return "redirect:/myAds";
    }

    @PostMapping("/deleteAdvertisement")
    public String delete(@RequestParam long id, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        Advertisement ad = ads.find(id);

        if (ad != null && ad.getOwnerId().equals(u.getId())) {
            ads.delete(id);
        }

        return "redirect:/myAds";
    }

    private void populateDisplayData(List<Advertisement> listings, Model model) throws Exception {

        Map<Long, Car> carsById = new HashMap<>();
        Map<Long, CarPhoto> coverPhotosByCarId = new HashMap<>();
        Map<Long, String> ownerNamesById = new HashMap<>();

        for (Advertisement ad : listings) {

            if (ad.getCarId() != null) {
                long carId = ad.getCarId();

                Car car = cars.find(carId);

                if (car != null) {
                    carsById.put(carId, car);

                    List<CarPhoto> carPhotos = photos.findByCarId(carId);

                    if (!carPhotos.isEmpty()) {
                        coverPhotosByCarId.put(carId, carPhotos.get(0));
                    }
                }
            }

            if (ad.getOwnerId() != null) {
                long ownerId = ad.getOwnerId();

                if (!ownerNamesById.containsKey(ownerId)) {
                    User owner = users.find(ownerId);

                    if (owner != null) {
                        ownerNamesById.put(ownerId, owner.getName());
                    }
                }
            }
        }

        model.addAttribute("carsById", carsById);
        model.addAttribute("coverPhotosByCarId", coverPhotosByCarId);
        model.addAttribute("ownerNamesById", ownerNamesById);
    }
}