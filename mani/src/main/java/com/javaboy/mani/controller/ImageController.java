package com.javaboy.mani.controller;

import com.javaboy.mani.dto.Imagedto;
import com.javaboy.mani.model.Image;
import com.javaboy.mani.response.ApiResponse;
import com.javaboy.mani.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;
    /**
     * Upload images for a product
     * @param file Single file upload (optional)
     * @param files Multiple files upload (optional)
     * @param productId Product ID to associate with images
     * @return ApiResponse with uploaded image details
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImages(
                                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                                   @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                   @RequestParam("productId") Long productId){
        try {
            // Handle both single file and multiple files cases
            if (file != null && (files == null || files.isEmpty())) {
                // Single file upload case
                List<MultipartFile> fileList = List.of(file);
                List<Imagedto> imagedto = imageService.saveImages(fileList, productId);
                return ResponseEntity.ok(new ApiResponse("Image uploaded successfully", imagedto));
            } else if (files != null && !files.isEmpty()) {
                // Multiple files upload case
                List<Imagedto> imagedto = imageService.saveImages(files, productId);
                return ResponseEntity.ok(new ApiResponse("Images uploaded successfully", imagedto));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse("No files were provided", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("Failed to upload images: ", e.getMessage()));
        }
    }
    @GetMapping("/image/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException {
        Image image = imageService.getImageById(id);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        image.getFilename() + "\"").body(resource);
    }
    @DeleteMapping("/image/{id}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id){
        try {
            Image deletedImage = imageService.deleteImageById(id);
            return ResponseEntity.ok(new ApiResponse("Image deleted successfully", deletedImage));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Failed to delete image: ", e.getMessage()));
        }
    }
    @PutMapping("/image/{id}/update")
    public ResponseEntity<ApiResponse> updateImage(@RequestPart("file") MultipartFile file, @PathVariable("id") Long id){
        try {
            imageService.updateImage(file, id);
            return ResponseEntity.ok(new ApiResponse("Image updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Failed to update image: ", e.getMessage()));
        }
    }
}
