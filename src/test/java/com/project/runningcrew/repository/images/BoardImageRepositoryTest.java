package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardImageRepositoryTest {

    @Autowired
    BoardImageRepository boardImageRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("BoardImage save 테스트")
    @Test
    public void saveTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        FreeBoard freeBoard = testEntityFactory.getFreeBoard(member, 0);
        BoardImage boardImage = new BoardImage("boardImage", freeBoard);

        ///when
        BoardImage savedImage = boardImageRepository.save(boardImage);

        //then
        assertThat(savedImage).isEqualTo(boardImage);
    }

    @DisplayName("BoardImage findById 테스트")
    @Test
    public void findById() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        FreeBoard freeBoard = testEntityFactory.getFreeBoard(member, 0);
        BoardImage boardImage = new BoardImage("boardImage", freeBoard);
        boardImageRepository.save(boardImage);

        ///when
        Optional<BoardImage> optBoardImage = boardImageRepository.findById(boardImage.getId());

        //then
        assertThat(optBoardImage).isNotEmpty();
        assertThat(optBoardImage).hasValue(boardImage);
    }

    @DisplayName("BoardImage delete 테스트")
    @Test
    public void delete() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        FreeBoard freeBoard = testEntityFactory.getFreeBoard(member, 0);
        BoardImage boardImage = new BoardImage("boardImage", freeBoard);
        boardImageRepository.save(boardImage);

        ///when
        boardImageRepository.delete(boardImage);

        //then
        Optional<BoardImage> optBoardImage = boardImageRepository.findById(boardImage.getId());
        assertThat(optBoardImage).isEmpty();
    }

    @DisplayName("BoardImage findAllByBoard 테스트")
    @Test
    void findAllByBoardTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        FreeBoard freeBoard = testEntityFactory.getFreeBoard(member, 0);

        for (int i = 0; i < 10; i++) {
            BoardImage boardImage = new BoardImage("boardImage" + i, freeBoard);
            boardImageRepository.save(boardImage);
        }

        //when
        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(freeBoard);

        //then
        assertThat(boardImages.size()).isSameAs(10);
        for (BoardImage boardImage : boardImages) {
            assertThat(boardImage.getBoard()).isEqualTo(freeBoard);
        }
    }

}