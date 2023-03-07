package com.project.runningcrew.repository.images;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.repository.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea,0);
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

    @DisplayName("Board 에 포함된 모든 BoardImage 삭제")
    @Test
    public void deleteAllByBoardTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea,0);
        Member member = testEntityFactory.getMember(user, crew);
        FreeBoard freeBoard = testEntityFactory.getFreeBoard(member, 0);

        for (int i = 0; i < 10; i++) {
            BoardImage boardImage = new BoardImage("boardImage" + i, freeBoard);
            boardImageRepository.save(boardImage);
        }

        ///when
        boardImageRepository.deleteAllByBoard(freeBoard);

        //then
        List<BoardImage> images = boardImageRepository.findAllByBoard(freeBoard);
        assertThat(images.isEmpty()).isTrue();
    }

    @DisplayName("boardId 의 리스트에 포함된 boardId 를 가진 BoardImage 반환 테스트")
    @Test
    public void findImagesByBoardIdsTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea,0);
        Member member = testEntityFactory.getMember(user, crew);

        FreeBoard freeBoard0 = testEntityFactory.getFreeBoard(member, 0);
        for (int i = 0; i < 3; i++) {
            BoardImage boardImage = new BoardImage("boardImage" + i, freeBoard0);
            boardImageRepository.save(boardImage);
        }

        FreeBoard freeBoard1 = testEntityFactory.getFreeBoard(member, 1);
        for (int i = 0; i < 4; i++) {
            BoardImage boardImage = new BoardImage("boardImage" + i, freeBoard1);
            boardImageRepository.save(boardImage);
        }

        FreeBoard freeBoard2 = testEntityFactory.getFreeBoard(member, 2);
        for (int i = 0; i < 5; i++) {
            BoardImage boardImage = new BoardImage("boardImage" + i, freeBoard2);
            boardImageRepository.save(boardImage);
        }

        List<Long> boardIds = List.of(freeBoard0.getId(), freeBoard1.getId(), freeBoard2.getId());

        ///when
        List<BoardImage> images = boardImageRepository.findImagesByBoardIds(boardIds);

        //then
        List<BoardImage> images0 = images.stream()
                .filter(boardImage -> boardImage.getBoard().equals(freeBoard0))
                .collect(Collectors.toList());
        List<BoardImage> images1 = images.stream()
                .filter(boardImage -> boardImage.getBoard().equals(freeBoard1))
                .collect(Collectors.toList());
        List<BoardImage> images2 = images.stream()
                .filter(boardImage -> boardImage.getBoard().equals(freeBoard2))
                .collect(Collectors.toList());
        assertThat(images.size()).isSameAs(12);
        assertThat(images0.size()).isSameAs(3);
        assertThat(images1.size()).isSameAs(4);
        assertThat(images2.size()).isSameAs(5);
    }

}