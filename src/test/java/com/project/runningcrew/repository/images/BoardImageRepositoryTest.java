package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.UserRepository;
import com.project.runningcrew.repository.boards.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    BoardRepository boardRepository;

    public User testUser() {
        User user = User.builder().email("email@naver.com")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("010-0000-0000")
                .password("123a!")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .height(170)
                .weight(70)
                .build();
        return userRepository.save(user);
    }

    public Crew testCrew() {
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember() {
        Member member = new Member(testUser(), testCrew(), MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }

    public Board testBoard() {
        Board board = new FreeBoard(testMember(), "title", "detail");
        return boardRepository.save(board);
    }

    @Test
    public void saveTest() {
        //given
        BoardImage boardImage = new BoardImage("boardImage", testBoard());

        ///when
        BoardImage savedImage = boardImageRepository.save(boardImage);

        //then
        assertThat(savedImage).isEqualTo(boardImage);
    }

    @Test
    public void findById() {
        //given
        BoardImage boardImage = new BoardImage("boardImage", testBoard());
        boardImageRepository.save(boardImage);

        ///when
        Optional<BoardImage> optBoardImage = boardImageRepository.findById(boardImage.getId());

        //then
        assertThat(optBoardImage).isNotEmpty();
        assertThat(optBoardImage).hasValue(boardImage);
    }

    @Test
    public void delete() {
        //given
        BoardImage boardImage = new BoardImage("boardImage", testBoard());
        boardImageRepository.save(boardImage);

        ///when
        boardImageRepository.delete(boardImage);

        //then
        Optional<BoardImage> optBoardImage = boardImageRepository.findById(boardImage.getId());
        assertThat(optBoardImage).isEmpty();
    }

    @Test
    void findAllByBoardTest() {
        //given
        Board board = testBoard();
        for (int i = 0; i < 10; i++) {
            BoardImage boardImage = new BoardImage("boardImage" + i, board);
            boardImageRepository.save(boardImage);
        }

        //when
        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(board);

        //then
        assertThat(boardImages.size()).isSameAs(10);
        for (BoardImage boardImage : boardImages) {
            assertThat(boardImage.getBoard()).isEqualTo(board);
        }
    }

}