package com.project.runningcrew.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ImageService {

    /**
     * MultipartFile 을 dirName 에 저장 후 저장된 경로를 반환한다.
     * @param multipartFile 저장할 MultipartFile
     * @param dirName 이미지를 저장할 directory
     * @return MultipartFile 이 저장된 경로
     */
    public String uploadImage(MultipartFile multipartFile, String dirName);

    /**
     * 저장된 경로가 fileUrl 인 Image 를 삭제한다.
     * @param fileUrl 삭제할 Image 의 경로
     */
    public void deleteImage(String fileUrl);



}
