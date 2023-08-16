package com.project.runningcrew.board.service;

import com.project.runningcrew.board.entity.InfoBoard;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.exception.notFound.BoardNotFoundException;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import com.project.runningcrew.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final ImageService imageService;

    private final CommentService commentService;
    private final String imageDirName = "board";


    /**
     * 입력받은 boardId 값으로 Board 를 찾아내 반환한다.
     * @param boardId
     * @return boardId 값으로 찾아낸 Board
     */
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
    }


    /**
     * 입력된 Board 를 저장한다.
     * @param board 저장할 Board
     * @param multipartFiles 저장할 모든 MultiPartFile
     * @return 저장된 Board 에 부여된 id
     */
    public Long saveBoard(Board board, List<MultipartFile> multipartFiles) {
        Board savedBoard = boardRepository.save(board);

        if(multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
                boardImageRepository.save(new BoardImage(imageUrl, savedBoard));
            }
        }

        return savedBoard.getId();
    }


    /**
     * 기존의 Board 정보와 갱신정보를 비교하여 바뀐점이 있다면 갱신한다.
     * @param originBoard 기존의 Board 정보
     * @param newBoard 갱신 Board 정보
     * @param addFiles 추가할 이미지의 MultipartFile List
     * @param deleteFiles 삭제할 BoardImage List
     */
    public void updateBoard(Board originBoard, Board newBoard, List<MultipartFile> addFiles, List<BoardImage> deleteFiles) {

        if(!originBoard.getTitle().equals(newBoard.getTitle())) {
            originBoard.updateTitle(newBoard.getTitle());
        }

        if(!originBoard.getDetail().equals(newBoard.getDetail())) {
            originBoard.updateDetail(newBoard.getDetail());
        }

        if(originBoard instanceof ReviewBoard && newBoard instanceof ReviewBoard) {
            //note 리뷰 게시글의 경우 런닝기록 수정이 가능하다.
            ((ReviewBoard) originBoard).updateRunningRecord(((ReviewBoard) newBoard).getRunningRecord());
        }

        if(originBoard instanceof InfoBoard && newBoard instanceof InfoBoard) {
            //note 정보 게시글의 경우 런닝기록 수정이 가능하다.
            ((InfoBoard) originBoard).updateRunningRecord(((InfoBoard) newBoard).getRunningRecord());
        }


        if(!CollectionUtils.isEmpty(addFiles)) {
            for (MultipartFile multipartFile : addFiles) {
                String imgUrl = imageService.uploadImage(multipartFile, imageDirName);
                boardImageRepository.save(new BoardImage(imgUrl, originBoard));
            }
        }

        if(!CollectionUtils.isEmpty(deleteFiles)) {
            for (BoardImage boardImage : deleteFiles) {
                imageService.deleteImage(boardImage.getFileName());
                boardImageRepository.delete(boardImage);
            }
        }

    }

    /**
     * 입력받은 Board 를 삭제한다.
     * @param board 게시글 정보
     */
    public void deleteBoard(Board board) {

        List<BoardImage> deleteBoards = boardImageRepository.findAllByBoard(board);
        for (BoardImage deleteBoard : deleteBoards) {
            imageService.deleteImage(deleteBoard.getFileName());
        }

        boardImageRepository.deleteAll(deleteBoards);
        commentService.deleteCommentAtBoard(board);
        boardRepository.delete(board);
    }

    /**
     * @param member 작성자 member
     * @param pageable 페이징 정보
     * @return SimpleBoardDto 의 Slice
     */

    public Slice<SimpleBoardDto> findSimpleBoardDtoByMember(Member member, Pageable pageable) {
        return boardRepository.findSimpleBoardDtoByMember(member, pageable);
    }


}
