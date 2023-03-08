package com.project.runningcrew.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ImageS3ServiceImplTest {

    @Autowired ImageService imageService;

    @DisplayName("S3 이미지 업로드 테스트")
    @Test
     void uploadImageTest() throws Exception {
        //given

        //when

        //then

    }

    @DisplayName("S3 이미지 삭제 테스트")
    @Test
    void deleteImageTest() throws Exception {
        //given
    }


}