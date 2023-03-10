package com.project.runningcrew.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ImageS3ServiceImplTest {


    @Autowired
    ImageService imageService;


    @DisplayName("S3 이미지 업로드 테스트")
    @Test
     void uploadImageTest() throws Exception {
        // 이미지 업로드 테스트 확인 완료


        File file = new File("C:\\Users\\USER\\Desktop\\test.png");

        FileInputStream input = new FileInputStream(file);

        MultipartFile multipartFile = new MockMultipartFile(
                "test",
                file.getName(),
                "image/png",
                input.readAllBytes()
        );

        String url = imageService.uploadImage(multipartFile, "test");

        System.out.println("url = " + url);
    }

    @DisplayName("S3 이미지 삭제 테스트")
    @Test
    void deleteImageTest() throws Exception {
        imageService.deleteImage("6553efb1-ab95-4f61-8c3a-af86ce61e66b-test.png");

    }


}