package ua.nure.nosyk.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ua.nure.nosyk.courseproject.entity.Car;
import ua.nure.nosyk.courseproject.entity.CarPhoto;
import ua.nure.nosyk.courseproject.entity.User;
import ua.nure.nosyk.courseproject.form.CarForm;
import ua.nure.nosyk.courseproject.service.CarPhotoService;
import ua.nure.nosyk.courseproject.service.CarService;

import javax.servlet.http.HttpSession;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Controller
public class CarController {

    private final CarService cars;
    private final CarPhotoService photos;

    public CarController(CarService cars, CarPhotoService photos) {
        this.cars = cars;
        this.photos = photos;
    }

    private User user(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    @GetMapping("/cars")
    public String cars(Model model, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        List<Car> ownedCars = cars.mine(u.getId());
        Map<Long, List<CarPhoto>> photosByCarId = new HashMap<>();

        for (Car car : ownedCars) {
            photosByCarId.put(car.getId(), photos.findByCarId(car.getId()));
        }

        model.addAttribute("cars", ownedCars);
        model.addAttribute("photosByCarId", photosByCarId);

        return "cars";
    }

    @GetMapping("/addCar")
    public String add(Model model, HttpSession session) {
        if (user(session) == null) {
            return "redirect:/login";
        }

        model.addAttribute("carForm", new CarForm());
        return "addCarPage";
    }

    @PostMapping("/addCar")
    public String add(@ModelAttribute CarForm form, @RequestParam(value = "photos", required = false) MultipartFile[] uploads, Model model, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        int currentYear = Year.now().getValue();

        Set<String> allowedConditions = Set.of("NEW", "USED", "DAMAGED");

        if (form.getBrand() == null || form.getBrand().trim().isEmpty()) {

            return invalidForm(model, form, "Please enter the car brand");
        }

        if (form.getYear() < 1885 || form.getYear() > currentYear + 1) {

            return invalidForm(model, form, "Please enter a valid production year");
        }

        if (form.getMileage() < 0 || form.getMileage() > 2_000_000) {

            return invalidForm(model, form, "Please enter a valid mileage");
        }

        if (form.getVehicleCondition() == null || !allowedConditions.contains(form.getVehicleCondition())) {

            return invalidForm(model, form, "Please select a valid condition");
        }

        try {
            photos.validateUploads(uploads);
        } catch (IllegalArgumentException ex) {
            return invalidForm(model, form, ex.getMessage());
        }

        Car car = new Car();

        car.setOwnerId(u.getId());
        car.setVinCode(form.getVinCode());
        car.setBrand(form.getBrand().trim());
        car.setModel(form.getModel());
        car.setYear(form.getYear());
        car.setMileage(form.getMileage());
        car.setVehicleCondition(form.getVehicleCondition());

        // Create the car first, because photo metadata needs its ID.
        cars.create(car);

        try {
            photos.addPhotos(car.getId(), uploads);
        } catch (Exception ex) {
            // Avoid leaving a half-created car if photo saving fails.
            try {
                photos.deleteByCarId(car.getId());
            } catch (Exception ignored) {
                // The original failure is more useful to diagnose.
            }

            try {
                cars.delete(car.getId());
            } catch (Exception ignored) {
                // Cleanup can be retried if the database is unavailable.
            }

            return invalidForm(model, form, "The car or its photos could not be saved. " + "Please try again.");
        }

        return "redirect:/cars";
    }

    private String invalidForm(Model model, CarForm form, String message) {

        model.addAttribute("carForm", form);
        model.addAttribute("errorMessage", message);

        return "addCarPage";
    }

    @PostMapping("/deleteCar")
    public String delete(@RequestParam long id, HttpSession session) throws Exception {

        User u = user(session);

        if (u == null) {
            return "redirect:/login";
        }

        Car car = cars.find(id);

        if (car != null && Objects.equals(car.getOwnerId(), u.getId())) {

            // Remove photo metadata and files before deleting the car.
            photos.deleteByCarId(id);
            cars.delete(id);
        }

        return "redirect:/cars";
    }
}