package ua.nure.nosyk.courseproject.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ua.nure.nosyk.courseproject.dao.CarPhotoDAO;
import ua.nure.nosyk.courseproject.entity.CarPhoto;
import ua.nure.nosyk.courseproject.service.LocalPhotoStorageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

@Controller
public class CarPhotoController {

    private final CarPhotoDAO photos;
    private final LocalPhotoStorageService storage;

    public CarPhotoController(CarPhotoDAO photos, LocalPhotoStorageService storage) {
        this.photos = photos;
        this.storage = storage;
    }

    @GetMapping("/car-photos/{filename:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) throws SQLException {

        CarPhoto photo = photos.findPublicByFilename(filename);

        if (photo == null) {
            return ResponseEntity.notFound().build();
        }

        Path path;

        try {
            path = storage.resolveStoredFilename(photo.getStoredFilename());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }

        if (!Files.isRegularFile(path)) {
            return ResponseEntity.notFound().build();
        }

        MediaType contentType = MediaType.parseMediaType(photo.getContentType());

        return ResponseEntity.ok().contentType(contentType).cacheControl(CacheControl.noCache()).header("X-Content-Type-Options", "nosniff").header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getStoredFilename() + "\"").body(new FileSystemResource(path));
    }
}