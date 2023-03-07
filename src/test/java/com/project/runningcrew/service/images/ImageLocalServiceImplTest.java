package com.project.runningcrew.service.images;

import com.project.runningcrew.exception.ImageFileCreationException;
import com.project.runningcrew.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ImageLocalServiceImplTest {

    @Autowired
    private ImageService imageService;

    @DisplayName("Image 로컬에 업로드 성공 테스트")
    @Test
    public void uploadImageTest1() throws Exception {
        //given
        String fileName = "img";
        String contentType = "png";
        String filePath = "C:\\Users\\alsrn\\Desktop\\test\\" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image",
                fileName+"."+contentType, contentType, fileInputStream);

        ///when
        String uploadImageUrl = imageService.uploadImage(mockMultipartFile, "testtest");

        //then
        assertThat(uploadImageUrl).startsWith("C:\\Users\\alsrn\\Desktop\\test\\testtest\\").endsWith(contentType);
    }

    @DisplayName("Image 로컬에 업로드 예외 테스트")
    @Test
    public void uploadImageTest2() throws Exception{
        //given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "".getBytes());

        ///when
        //then
        assertThatThrownBy(() -> imageService.uploadImage(mockMultipartFile, "testtest"))
                .isInstanceOf(ImageFileCreationException.class);
    }

    @DisplayName("Image 로컬에서 삭제 성공 테스트")
    @Test
    public void deleteImageTest1() throws Exception {
        //given
        String fileName = "img";
        String contentType = "png";
        String filePath = "C:\\Users\\alsrn\\Desktop\\test\\" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image",
                fileName+"."+contentType, contentType, fileInputStream);
        String fileUrl = imageService.uploadImage(mockMultipartFile, "testtest");

        ///when
        imageService.deleteImage(fileUrl);

        //then
        File file = new File(fileUrl);
        assertThat(file.exists()).isFalse();
    }

    @DisplayName("Image 로컬에서 삭제 예외 테스트")
    @Test
    public void deleteImageTest2() throws Exception {
        //given
        String fileUrl = "";

        ///when
        //then
        assertThatThrownBy(() -> imageService.deleteImage(fileUrl))
                .isInstanceOf(ImageFileCreationException.class);
    }

}