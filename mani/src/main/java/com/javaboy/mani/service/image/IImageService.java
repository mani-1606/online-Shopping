package com.javaboy.mani.service.image;

import com.javaboy.mani.dto.Imagedto;
import com.javaboy.mani.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    Image deleteImageById(Long id);
    void updateImage(MultipartFile file, Long id);
    List<Imagedto> saveImages(List<MultipartFile> files, Long productId);

}
