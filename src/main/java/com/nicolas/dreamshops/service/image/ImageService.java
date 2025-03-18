package com.nicolas.dreamshops.service.image;

import com.nicolas.dreamshops.dto.ImageDto;
import com.nicolas.dreamshops.exceptions.ResourceNotFoundException;
import com.nicolas.dreamshops.model.Image;
import com.nicolas.dreamshops.model.Product;
import com.nicolas.dreamshops.repository.ImageRepository;
import com.nicolas.dreamshops.service.product.IProductService;
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
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No image found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found!");
        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDtos = new ArrayList<>();

        for(MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String downloadUrlPath = "/api/v1/images/image/download";

//              I don't understand why he does this first in the video, so I'll try without it
//              image.setDownloadUrl(downloadUrlPath + image.getId());

                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(downloadUrlPath + savedImage.getId());

                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setImageDownloadUrl(savedImage.getDownloadUrl());

                savedImagesDtos.add(imageDto);
            } catch(IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return savedImagesDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long id) {
        Image image = getImageById(id);

        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
