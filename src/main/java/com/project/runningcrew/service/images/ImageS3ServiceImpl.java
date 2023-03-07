package com.project.runningcrew.service.images;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ImageS3ServiceImpl implements ImageService{


    @Value("${spring.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;


    @Override
    public String uploadImage(MultipartFile multipartFile, String dirName) {

        return null;
    }

    @Override
    public void deleteImage(String fileUrl) {

    }
}
