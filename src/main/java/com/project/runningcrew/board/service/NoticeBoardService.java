package com.project.runningcrew.board.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.fcm.FirebaseMessagingService;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.board.repository.NoticeBoardRepository;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import com.project.runningcrew.image.ImageService;
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
    private final FirebaseMessagingService firebaseMessagingService;

    private final ImageService imageService;
    private final String imageDirName = "board";


    /**
     * NoticeBoard 를 생성하고 해당 크루의 푸쉬 알림을 보낸다.
     * @param noticeBoard 입력받은 NoticeBoard
     * @param multipartFiles 추가 이미지 MultipartFile
     * @return 저장된 NoticeBoard 의 id 값
     */
    public Long saveNoticeBoard(NoticeBoard noticeBoard, List<MultipartFile> multipartFiles) {
        Board savedBoard = noticeBoardRepository.save(noticeBoard);

        if(multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
                boardImageRepository.save(new BoardImage(imageUrl, savedBoard));
            }
        }

        firebaseMessagingService.sendNoticeBoardMessages(noticeBoard.getMember().getCrew(), noticeBoard);
        // NoticeBoard 생성 시 FirebaseMessagingService 사용하여 알림 신청 추가

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
