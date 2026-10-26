package ua.nure.nosyk.courseproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ua.nure.nosyk.courseproject.entity.CarPhoto;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.UUID;

@Service
public class LocalPhotoStorageService {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_PIXELS = 12_000_000L;

    private final Path root;

    public LocalPhotoStorageService(@Value("${app.upload.dir:uploads}") String uploadDirectory) {

        this.root = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        System.out.println("PHOTO STORAGE ROOT: " + this.root);
        System.out.println("UPLOAD DIR EXISTS: " + Files.isDirectory(this.root));
        System.out.println("TARGET PHOTO EXISTS: " + Files.isRegularFile(this.root.resolve("1cd48e91-c884-4df2-b7ae-f47db650373b.jpg")));
    }

    public void validate(MultipartFile file) throws IOException {
        validateAndRead(file);
    }

    public CarPhoto store(MultipartFile file) throws IOException {
        ImageData image = validateAndRead(file);

        Files.createDirectories(root);

        String filename = UUID.randomUUID() + image.extension;
        Path destination = resolveStoredFilename(filename);

        Files.write(destination, image.bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

        CarPhoto photo = new CarPhoto();
        photo.setStoredFilename(filename);
        photo.setContentType(image.contentType);
        photo.setSizeBytes(image.bytes.length);

        return photo;
    }

    public Path resolveStoredFilename(String filename) {
        // Only generated UUID filenames with approved image extensions.
        if (filename == null || !filename.matches("[0-9a-fA-F-]{36}\\.(jpg|png)")) {
            throw new IllegalArgumentException("Invalid image filename");
        }

        Path resolved = root.resolve(filename).normalize();

        if (!resolved.startsWith(root)) {
            throw new IllegalArgumentException("Invalid image path");
        }

        return resolved;
    }

    public void delete(String filename) throws IOException {
        Files.deleteIfExists(resolveStoredFilename(filename));
    }

    private ImageData validateAndRead(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The image is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Each image must be no larger than 5 MB");
        }

        byte[] bytes = file.getBytes();

        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes); ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {

            if (imageInput == null) {
                throw new IllegalArgumentException("The uploaded file is not a valid image");
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);

            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Only JPEG and PNG images are supported");
            }

            ImageReader reader = readers.next();

            try {
                reader.setInput(imageInput, true, true);

                String format = reader.getFormatName().toLowerCase();

                String extension;
                String contentType;

                if ("jpeg".equals(format) || "jpg".equals(format)) {
                    extension = ".jpg";
                    contentType = "image/jpeg";
                } else if ("png".equals(format)) {
                    extension = ".png";
                    contentType = "image/png";
                } else {
                    throw new IllegalArgumentException("Only JPEG and PNG images are supported");
                }

                int width = reader.getWidth(0);
                int height = reader.getHeight(0);

                if (width <= 0 || height <= 0 || (long) width * height > MAX_PIXELS) {
                    throw new IllegalArgumentException("The image dimensions are too large");
                }

                return new ImageData(bytes, extension, contentType);

            } finally {
                reader.dispose();
            }
        }
    }

    private static class ImageData {
        private final byte[] bytes;
        private final String extension;
        private final String contentType;

        private ImageData(byte[] bytes, String extension, String contentType) {
            this.bytes = bytes;
            this.extension = extension;
            this.contentType = contentType;
        }
    }
}
