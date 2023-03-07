package com.project.runningcrew.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.runningcrew.exception.ImageFileCreationException;
import com.project.runningcrew.exception.S3UploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageS3ServiceImpl implements ImageService{

    @Value("${spring.s3.bucket}")
    private String bucketName;

    private String DOWNLOAD_PATH = "C:\\Users\\alsrn\\Desktop\\test";
    private String SEPARATOR = File.separator;

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;


    @Override
    public String uploadImage(MultipartFile multipartFile, String dirName) {
        if (multipartFile.isEmpty()) {
            throw new ImageFileCreationException();
        }

        String fileName = UUID.randomUUID().toString();
        String extension = multipartFile.getOriginalFilename().split("\\.")[1];
        File uploadFile = new File(DOWNLOAD_PATH + SEPARATOR + dirName + SEPARATOR
                + fileName + "." + extension);


        ObjectMetadata obj = new ObjectMetadata();
        obj.setContentLength(multipartFile.getSize());
        obj.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucketName, fileName, multipartFile.getInputStream(), obj);
        } catch (Exception e) {
            // 이미지 업로드 에러 발생
            throw new S3UploadException();
        }

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    @Override
    public void deleteImage(String fileUrl) {
        boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, fileUrl);
        if(isObjectExist) {
            amazonS3Client.deleteObject(bucketName, fileUrl);
        }
    }
}
