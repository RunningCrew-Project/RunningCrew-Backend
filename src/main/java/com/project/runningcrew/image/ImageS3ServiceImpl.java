package com.project.runningcrew.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.runningcrew.exception.ImageFileCreationException;
import com.project.runningcrew.exception.s3.S3DeleteException;
import com.project.runningcrew.exception.s3.S3UploadException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageS3ServiceImpl implements ImageService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;


    /**
     * MultipartFile 을 S3 스토리지에 업로드하고 url 을 반환한다.
     * @param multipartFile 저장할 MultipartFile
     * @param dirName 이미지를 저장할 directory
     * @return 저장된 s3 스토리지의 url
     */
    @Override
    public String uploadImage(MultipartFile multipartFile, String dirName) {
        if (multipartFile.isEmpty()) {
            throw new ImageFileCreationException();
        }

        String s3FileName = dirName + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata obj = new ObjectMetadata();
        obj.setContentLength(multipartFile.getSize());
        obj.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucketName, s3FileName, multipartFile.getInputStream(), obj);
        } catch (Exception e) {
            // 이미지 업로드 에러 발생
            throw new S3UploadException();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }


    /**
     * s3 스토리지에 저장된 이미지를 삭제한다.
     * @param fileUrl 삭제할 s3 스토리지의 이미지 url
     */
    @Override
    public void deleteImage(String fileUrl) {
        boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, fileUrl);
        log.info("fileUrl={}",fileUrl);
        if(isObjectExist) {
            amazonS3Client.deleteObject(bucketName, fileUrl);
        } else {
            throw new S3DeleteException();
        }
    }
}
