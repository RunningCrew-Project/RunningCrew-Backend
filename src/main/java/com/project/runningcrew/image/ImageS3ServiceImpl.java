package com.project.runningcrew.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.runningcrew.exception.image.EmptyImageFileException;
import com.project.runningcrew.exception.image.s3.S3DeleteException;
import com.project.runningcrew.exception.image.s3.S3UploadException;
import com.project.runningcrew.exception.notFound.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageS3ServiceImpl implements ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${s3.host.name}")
    private String hostName;

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;


    /**
     * MultipartFile 을 S3 스토리지에 업로드하고 url 을 반환한다.
     * @param multipartFile 저장할 MultipartFile
     * @param dirName       이미지를 저장할 directory
     * @return 저장된 s3 스토리지의 url
     */
    @Override
    public String uploadImage(MultipartFile multipartFile, String dirName) {
        if (multipartFile.isEmpty()) {
            throw new EmptyImageFileException();
        }

        String s3FileName = dirName + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata obj = new ObjectMetadata();
        obj.setContentLength(multipartFile.getSize());
        obj.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucketName, s3FileName, multipartFile.getInputStream(), obj);
        } catch (Exception e) {
            //note 이미지 업로드 에러 발생
            log.error("image S3 upload exception : {}", e);
            throw new S3UploadException(bucketName);
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }


    /**
     * s3 스토리지에 저장된 이미지를 삭제한다.
     * @param fileUrl 삭제할 s3 스토리지의 이미지 url
     */
    @Override
    public void deleteImage(String fileUrl) {

        if (!fileUrl.startsWith(hostName)) {
            throw new S3DeleteException(bucketName, fileUrl);
        }

        String decodeURL = decodeURL(fileUrl);
        boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, decodeURL);
        log.info("Delete fileUrl={}", decodeURL);

        if (isObjectExist) {
            amazonS3Client.deleteObject(bucketName, decodeURL);
        } else {
            log.error("it's an S3 image that doesn't exist in S3 bucket");
            throw new S3DeleteException(bucketName, decodeURL);
        }

    }


    /**
     * 이미지 조회하기.
     * @param dirName 조회할 버킷의 디렉토리 이름
     * @param imgName 이미지 이름
     * @return S3 버킷의 조회 이미지
     */
    @Override
    public String getImage(String dirName, String imgName) {

        String filePath = dirName + '/' + imgName;
        boolean isObjectExist = amazonS3.doesObjectExist(bucketName, filePath);

        if(isObjectExist) {
            log.info("Get fileUrl={}", filePath);
            return amazonS3.getUrl(bucketName, filePath).toString();
        } else {
            throw new ImageNotFoundException();
        }

    }


    /**
     * 파일 경로 URL 을 utf-8 디코딩하여 반환한다.
     * @param fileUrl 파일 경로
     * @return 디코딩 URL
     */
    @Override
    public String decodeURL(String fileUrl) {
        return URLDecoder.decode(
                fileUrl.replace(hostName, "").replaceAll("\\p{Z}", ""),
                StandardCharsets.UTF_8
        );
    }




}
