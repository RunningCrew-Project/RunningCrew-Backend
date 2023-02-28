package com.project.runningcrew.service.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.exception.notFound.BoardNotFoundException;
import com.project.runningcrew.repository.boards.BoardRepository;
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
public class BoardService {


    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final ImageService imageService;
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
        for (MultipartFile multipartFile : multipartFiles) {
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            boardImageRepository.save(new BoardImage(imageUrl, savedBoard));
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

        for (MultipartFile multipartFile : addFiles) {
            String imgUrl = imageService.uploadImage(multipartFile, imageDirName);
            boardImageRepository.save(new BoardImage(imgUrl, originBoard));
        }
        for (BoardImage boardImage : deleteFiles) {
            imageService.deleteImage(boardImage.getFileName());
            boardImageRepository.delete(boardImage);
        }
    }

    /**
     * 입력받은 Board 를 삭제한다.
     * @param board
     */
    public void deleteBoard(Board board) {
        boardRepository.delete(board);
        /**
         * 추가 내용 추후 작성예정
         */
    }

    /**
     * 입력받은 Member 가 작성한 모든 Board 를 반환한다.
     * @param member 작성자 member
     * @return 입력받은 Member 가 작성한 모든 Board List
     */
    public Slice<Board> findBoardByMember(Member member, Pageable pageable) {
        return boardRepository.findByMember(member, pageable);
    }

    /**
     * 특정 Crew 의 Board 중 keyword 를 제목 or 내용에 포함하는 Board 를 반환한다.
     * @param crew 입력받은 crew 데이터
     * @param keyword 검색단어 keyword
     * @return 특정 Crew 의 Board 중 keyword 를 제목 or 내용에 포함하는 Board List
     */
    public Slice<Board> findBoardByCrewAndKeyWord(Crew crew, String keyword) {
        return boardRepository.findSliceAllByCrewAndKeyWord(keyword, crew);
    }



}
