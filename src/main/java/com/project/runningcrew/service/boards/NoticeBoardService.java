package com.project.runningcrew.service.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.repository.boards.NoticeBoardRepository;
import com.project.runningcrew.repository.images.BoardImageRepository;
import com.project.runningcrew.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;
    private final BoardImageRepository boardImageRepository;

    private final ImageService imageService;
    private final String imageDirName = "board";


    /**
     * NoticeBoard 전용 생성 메소드
     * @param noticeBoard 입력받은 NoticeBoard
     * @param multipartFiles 추가 이미지 MultipartFile
     * @return 저장된 NoticeBoard 의 id 값
     */
    public Long saveNoticeBoard(NoticeBoard noticeBoard, List<MultipartFile> multipartFiles) {
        Board savedBoard = noticeBoardRepository.save(noticeBoard);
        for (MultipartFile multipartFile : multipartFiles) {
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            boardImageRepository.save(new BoardImage(imageUrl, savedBoard));
        }
        return savedBoard.getId();
    }



    /**
     * 입력받은 Crew 의 공지게시판 목록을 모두 보여준다.
     * @param crew 입력받은 Crew
     * @param pageable
     * @return 입력받은 Crew 의 공지게시판 목록(페이징 적용)
     */
    public Slice<NoticeBoard> findNoticeBoardByCrew(Crew crew, Pageable pageable) {
        return noticeBoardRepository.findNoticeBoardByCrew(crew, pageable);
    }


}
