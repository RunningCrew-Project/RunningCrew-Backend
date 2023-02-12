package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
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


    public User testUser(int num) {
            User user = User.builder()
                    .email("email@email.com" + num)
                    .password("password123!" + num)
                    .name("name"+ num)
                    .nickname("nickname"+ num)
                    .imgUrl("imgUrl"+ num)
                    .login_type(LoginType.EMAIL)
                    .phoneNumber("phoneNumber"+ num)
                    .location("location"+ num)
                    .sex(Sex.MAN)
                    .birthday(LocalDate.now())
                    .height(100)
                    .weight(100)
                    .build();
            return userRepository.save(user);
        }

        public Crew testCrew(int num) {
            Crew crew = Crew.builder()
                    .name("name"+ num)
                    .location("location"+ num)
                    .introduction("introduction"+ num)
                    .crewImgUrl("crewImgUrl"+ num)
                    .build();
            return crewRepository.save(crew);
        }

        public Member testMember(int num) {
            Member member = new Member(testUser(num), testCrew(num), MemberRole.ROLE_NORMAL);
            return memberRepository.save(member);
    }



    @DisplayName("FreeBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        FreeBoard freeBoard = new FreeBoard(testMember(num), title, content);

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
        FreeBoard savedFreeBoard = freeBoardRepository.save( new FreeBoard(testMember(num), title, content));

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
        FreeBoard savedFreeBoard = freeBoardRepository.save( new FreeBoard(testMember(num), title, content));

        //when
        freeBoardRepository.delete(savedFreeBoard);
        Optional<FreeBoard> findFreeBoardOpt = freeBoardRepository.findById(savedFreeBoard.getId());

        //then
        Assertions.assertThat(findFreeBoardOpt).isEmpty();
    }



    @DisplayName("FreeBoard 페이징 테스트")
    @Test
    void findFreeBoardAllTest() throws Exception {
        //given
        for (int i = 0; i < 100; i++) {
            freeBoardRepository.save(
                    new FreeBoard(testMember(i), "title" + i, "content" + i)
                    // FreeBoard 100개 save
            );
        }
        PageRequest pageRequest = PageRequest.of(0, 15); // Page size = 15

        //when
        Slice<FreeBoard> slice = freeBoardRepository.findFreeBoardAll(pageRequest);
        List<FreeBoard> content = slice.getContent();

        //then
        Assertions.assertThat(content.size()).isEqualTo(15);
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.getNumberOfElements()).isEqualTo(15);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isTrue();
    }


}