package com.project.runningcrew.image;

import com.project.runningcrew.exception.ImageFileCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

//@Service
@RequiredArgsConstructor
public class ImageLocalServiceImpl implements ImageService {

    private String DOWNLOAD_PATH = "C:\\Users\\alsrn\\Desktop\\test";
    private String SEPARATOR = File.separator;

    /**
     * MultipartFile 을 로컬의 지정된 경로의 dirName 에 저장 후 저장된 경로를 반환한다.
     * @param multipartFile 저장할 MultipartFile
     * @param dirName 이미지를 저장할 directory
     * @return MultipartFile 이 저장된 경로
     * @throws ImageFileCreationException MultipartFile 이 empty 이거나 MultipartFile 을 사용해 File 인스턴스 생성에 실패할 때
     */
    @Override
    public String uploadImage(MultipartFile multipartFile, String dirName) {
        if (multipartFile.isEmpty()) {
            throw new ImageFileCreationException();
        }
        String fileName = UUID.randomUUID().toString();
        String extension = multipartFile.getOriginalFilename().split("\\.")[1];
        File uploadFile = new File(DOWNLOAD_PATH + SEPARATOR + dirName + SEPARATOR
                    + fileName + "." + extension);
        try {
            multipartFile.transferTo(uploadFile);
        } catch (Exception e) {
            throw new ImageFileCreationException();
        }
        return uploadFile.getAbsolutePath();
    }

    /**
     * 로컬에 저장된 경로가 fileUrl 인 Image 를 삭제한다.
     * @param fileUrl 삭제할 Image 의 경로
     * @throws ImageFileCreationException fileUrl 로 File 인스턴스 생성에 실패할 때
     */

    @Override
    public void deleteImage(String fileUrl) {
        File deleteFile = new File(fileUrl);
        if (!deleteFile.exists()) {
            throw new ImageFileCreationException();
        }
        deleteFile.delete();
    }

}
