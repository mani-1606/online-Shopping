package com.javaboy.mani.service.image;

import com.javaboy.mani.dto.Imagedto;
import com.javaboy.mani.model.Image;
import com.javaboy.mani.model.Product;
import com.javaboy.mani.repository.ImageRepository;
import com.javaboy.mani.service.product.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class imageService implements IImageService {
    private final ImageRepository imagrepo;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imagrepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + id));
    }

    @Override
    public Image deleteImageById(Long id) {
        return imagrepo.findById(id)
                .map(image -> {
                    imagrepo.delete(image);
                    return image;
                })
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + id));

    }

    @Override
    public void updateImage(MultipartFile file, Long id) {
        Image img = getImageById(id);
        try {
            img.setFilename(file.getOriginalFilename());
            img.setFiletype(file.getContentType());
            img.setImage(new SerialBlob(file.getBytes()));
            Image.setFileName(file.getOriginalFilename());
            imagrepo.save(img);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error updating image with id: " + id, e);
        }

    }

    @Override
    public List<Imagedto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);

        List<Imagedto> saveImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);


                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);

                Image savedImage = imagrepo.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imagrepo.save(savedImage);

                Imagedto imagedto = new Imagedto();
                imagedto.setId(savedImage.getId());
                imagedto.setFilename(savedImage.getFilename());
                imagedto.setDownloadurl(savedImage.getDownloadUrl());
                saveImages.add(imagedto);
            } catch ( IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return saveImages;
    }
}
