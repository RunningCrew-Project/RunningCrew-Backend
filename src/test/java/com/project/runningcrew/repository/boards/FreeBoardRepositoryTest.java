package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.TestEntityFactory;
import com.project.runningcrew.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FreeBoardRepositoryTest {

    @Autowired FreeBoardRepository freeBoardRepository;
    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TestEntityFactory testEntityFactory;


    public User testUser(DongArea dongArea, int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname"+ num)
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(dongArea)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        return userRepository.save(user);
    }

    public Crew testCrew(DongArea dongArea, int num) {
        Crew crew = Crew.builder()
                .name("name"+ num)
                .dongArea(dongArea)
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(User user, Crew crew) {
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }



    @DisplayName("FreeBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard freeBoard = new FreeBoard(member, title, content);

        //when
        FreeBoard savedFreeBoard = freeBoardRepository.save(freeBoard);

        //then
        Assertions.assertThat(savedFreeBoard).isEqualTo(freeBoard);
    }



    @DisplayName("FreeBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard savedFreeBoard = freeBoardRepository.save( new FreeBoard(member, title, content));

        //when
        Optional<FreeBoard> findFreeBoardOpt = freeBoardRepository.findById(savedFreeBoard.getId());

        //then
        Assertions.assertThat(findFreeBoardOpt).isNotEmpty();
        Assertions.assertThat(findFreeBoardOpt).hasValue(savedFreeBoard);
    }



    @DisplayName("FreeBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard savedFreeBoard = freeBoardRepository.save( new FreeBoard(member, title, content));

        //when
        freeBoardRepository.delete(savedFreeBoard);
        Optional<FreeBoard> findFreeBoardOpt = freeBoardRepository.findById(savedFreeBoard.getId());

        //then
        Assertions.assertThat(findFreeBoardOpt).isEmpty();
    }



    @DisplayName("각 Crew 의 자유게시판 paging 출력 테스트")
    @Test
    void findFreeBoardByCrewTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew); // user(1), crew(1), member(1)

        for (int i = 0; i < 100; i++) {
            freeBoardRepository.save(
                    new FreeBoard(member, "title" + i, "content" + i)
                    // FreeBoard 100개 save
            );
        }
        PageRequest pageRequest = PageRequest.of(0, 15); // Page size = 15

        //when
        Slice<FreeBoard> slice = freeBoardRepository.findFreeBoardByCrew(member.getCrew(), pageRequest);
        List<FreeBoard> content = slice.getContent();

        //then
        Assertions.assertThat(content.size()).isEqualTo(15);
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.getNumberOfElements()).isEqualTo(15);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isTrue();
    }


}