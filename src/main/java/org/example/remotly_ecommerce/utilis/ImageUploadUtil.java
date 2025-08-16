package org.example.remotly_ecommerce.utilis;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageUploadUtil {
    private static final String UPLOAD_DIR = "C:/Users/Mohamed/Music/New folder/";

    /**
     * Save uploaded images to the server folder and return their relative paths.
     */

//    public List<String> saveImages(MultipartFile[] images) {
//        List<String> imagePaths = new ArrayList<>();
//
//        try {
//            for (MultipartFile file : images) {
//                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//                Path filePath = Paths.get(UPLOAD_DIR + filename);
//                Files.createDirectories(filePath.getParent()); // إنشاء المجلد لو مش موجود
//                file.transferTo(filePath);
//                // تخزين المسار النسبي أو الكامل حسب ما تريد
//                imagePaths.add(filePath.toString());
//            }
//        } catch (Exception e) {
//            log.error("Failed to save images", e);
//            throw new RuntimeException("Failed to save images", e);
//        }
//
//        return imagePaths;
//    }

    private final Cloudinary cloudinary;

    /**
     * Save uploaded images to Cloudinary and return their URLs.
     */
    public List<String> saveImages(MultipartFile[] images) {
        List<String> imageUrls = new ArrayList<>();

        try {
            for (MultipartFile file : images) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("folder", "remotly_ecommerce")); // هيتحطوا في فولدر اسمه remotly_ecommerce
                String url = (String) uploadResult.get("secure_url");
                imageUrls.add(url);
            }
        } catch (IOException e) {
            log.error("Failed to upload images to Cloudinary", e);
            throw new RuntimeException("Failed to upload images", e);
        }

        return imageUrls;
    }
    /**
     * Save a single image to Cloudinary and return its URL.
     *
     * @param image MultipartFile representing the image
     * @return URL of the uploaded image
     */
    public String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null; // لو مفيش صورة، نرجع null
        }
        // نعيد أول عنصر من القائمة اللي بترجعها الدالة الحالية
        return saveImages(new MultipartFile[]{image}).get(0);
    }

}
