package ua.nure.nosyk.courseproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ua.nure.nosyk.courseproject.dao.CarPhotoDAO;
import ua.nure.nosyk.courseproject.entity.CarPhoto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class CarPhotoService {

    private static final int MAX_PHOTOS_PER_CAR = 6;

    private final CarPhotoDAO dao;
    private final LocalPhotoStorageService storage;

    public CarPhotoService(CarPhotoDAO dao, LocalPhotoStorageService storage) {
        this.dao = dao;
        this.storage = storage;
    }

    public List<CarPhoto> findByCarId(long carId) throws SQLException {
        return dao.findByCarId(carId);
    }

    public void validateUploads(MultipartFile[] uploads) throws IOException {

        if (uploads == null) {
            return;
        }

        int count = 0;

        for (MultipartFile file : uploads) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            count++;

            if (count > MAX_PHOTOS_PER_CAR) {
                throw new IllegalArgumentException("You can upload at most 6 photos per car");
            }

            storage.validate(file);
        }
    }

    public void addPhotos(long carId, MultipartFile[] uploads) throws SQLException, IOException {

        if (uploads == null) {
            return;
        }

        List<CarPhoto> existing = dao.findByCarId(carId);

        int newCount = 0;

        for (MultipartFile file : uploads) {
            if (file != null && !file.isEmpty()) {
                newCount++;
            }
        }

        if (existing.size() + newCount > MAX_PHOTOS_PER_CAR) {
            throw new IllegalArgumentException("You can upload at most 6 photos per car");
        }

        int order = existing.size();

        for (MultipartFile file : uploads) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            CarPhoto photo = storage.store(file);
            photo.setCarId(carId);
            photo.setSortOrder(order++);

            try {
                dao.create(photo);
            } catch (SQLException | RuntimeException ex) {
                try {
                    storage.delete(photo.getStoredFilename());
                } catch (IOException cleanupError) {
                    ex.addSuppressed(cleanupError);
                }

                throw ex;
            }
        }
    }

    public void deleteByCarId(long carId) throws SQLException, IOException {

        List<CarPhoto> photos = dao.findByCarId(carId);

        // Remove database metadata first so removed photos aren't served.
        dao.deleteByCarId(carId);

        for (CarPhoto photo : photos) {
            storage.delete(photo.getStoredFilename());
        }
    }
}