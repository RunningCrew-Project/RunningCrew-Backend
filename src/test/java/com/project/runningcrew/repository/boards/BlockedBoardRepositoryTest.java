package com.project.runningcrew.repository.boards;

import com.project.runningcrew.board.repository.BlockedBoardRepository;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.BlockedBoard;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.repository.TestEntityFactory;
import com.project.runningcrew.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
class BlockedBoardRepositoryTest {


    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BlockedBoardRepository blockedBoardRepository;
    @Autowired TestEntityFactory testEntityFactory;


    @DisplayName("BlockBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .dongArea(dongArea)
                        .build()
        );
        String title = "title";
        String detail = "detail";
        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        Board board = boardRepository.save(new FreeBoard(member, title, detail));
        //when
        BlockedBoard blockedBoard = new BlockedBoard(board, member);
        BlockedBoard savedBlockedBoard = blockedBoardRepository.save(blockedBoard);
        //then
        Assertions.assertThat(savedBlockedBoard).isEqualTo(blockedBoard);
    }



    @DisplayName("BlockedBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .dongArea(dongArea)
                        .build()
        );
        String title = "title";
        String detail = "detail";
        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        Board board = boardRepository.save(new FreeBoard(member, title, detail));
        //when
        BlockedBoard savedBlockedBoard = blockedBoardRepository.save(new BlockedBoard(board, member));
        Optional<BlockedBoard> findBlockedBoardOpt = blockedBoardRepository.findById(savedBlockedBoard.getId());
        //then
        Assertions.assertThat(findBlockedBoardOpt).isNotEmpty();
        Assertions.assertThat(findBlockedBoardOpt).hasValue(savedBlockedBoard);
    }



    @DisplayName("BlockedBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .dongArea(dongArea)
                        .build()
        );
        String title = "title";
        String detail = "detail";
        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        Board board = boardRepository.save(new FreeBoard(member, title, detail));
        //when
        BlockedBoard savedBlockedBoard = blockedBoardRepository.save(new BlockedBoard(board, member));
        blockedBoardRepository.delete(savedBlockedBoard);
        Optional<BlockedBoard> findBlockedBoardOpt = blockedBoardRepository.findById(savedBlockedBoard.getId());
        //then
        Assertions.assertThat(findBlockedBoardOpt).isEmpty();
    }
}