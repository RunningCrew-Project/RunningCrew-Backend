package com.project.runningcrew.resourceimage.service;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.exception.notFound.ImageNotFoundException;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;
    private final String defaultImageUrl = "defaultImageUrl";


    /**
     * 입력받은 id 를 가진 BoardImage 를 찾아 반환한다. 없다면 ImageNotFoundException 을 throw 한다.
     *
     * @param boardImageId 찾는 BoardImage 의 id
     * @return boardImageId 를 id 로 가지는 BoardImage
     * @throws ImageNotFoundException boardImageId 에 해당하는 BoardImage 가 존재하지 않을 때
     */
    public BoardImage findById(Long boardImageId) {
        return boardImageRepository.findById(boardImageId).orElseThrow(ImageNotFoundException::new);
    }

    /**
     * Board 에 포함된 모든 BoardImage 를 반환한다.
     *
     * @param board
     * @return Board 에 포함된 모든 BoardImage
     */
    public List<BoardImage> findAllByBoard(Board board) {
        return boardImageRepository.findAllByBoard(board);
    }

    /**
     * boardId 의 리스트를 받아, boardId 와 BoardImage 의 Map 을 반환한다. boardId 에 포함된 BoardImage 가
     * 있다면 BoardImage 중 하나를 가지고, boardId 에 포함된 BoardImage 가 없다면 defaultImageUrl 을 가진
     * BoardImage 를 가진다.
     *
     * @param boardIds Board 의 id 를 가진 리스트
     * @return boardId 와 boardId 에 포함된 BoardImage 의 Map. 포함된 BoardImage 가 없다면
     * defaultImageUrl 을 가진 BoardImage
     */
    public Map<Long, BoardImage> findFirstImages(List<Long> boardIds) {
        Map<Long, BoardImage> maps = new HashMap<>();
        List<BoardImage> images = boardImageRepository.findImagesByBoardIds(boardIds);
        for (Long boardId : boardIds) {
            BoardImage first = images.stream()
                    .filter(image -> image.getBoard().getId().equals(boardId))
                    .findFirst().orElseGet(() -> new BoardImage(defaultImageUrl, null));
            maps.put(boardId, first);
        }
        return maps;
    }

}
