package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.repository.images.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;

    /**
     * Board 에 포함된 모든 BoardImage 를 반환한다.
     *
     * @param board
     * @return Board 에 포함된 모든 BoardImage
     */
    public List<BoardImage> findAllByBoard(Board board) {
        return boardImageRepository.findAllByBoard(board);
    }

}
