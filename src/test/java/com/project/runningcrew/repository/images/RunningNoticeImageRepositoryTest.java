package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.RunningNoticeRepository;
import com.project.runningcrew.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RunningNoticeImageRepositoryTest {

    @Autowired
    RunningNoticeImageRepository runningNoticeImageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RunningNoticeRepository runningNoticeRepository;


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

    public RunningNotice testRunningNotice() {
        RunningNotice runningNotice = RunningNotice.builder().title("title")
                .detail("detail")
                .member(testMember())
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023,02,11,15,0))
                .runningPersonnel(4)
                .status(RunningStatus.WAIT)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }

    @Test
    public void saveTest() {
        //given
        RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage", testRunningNotice());

        ///when
        RunningNoticeImage savedImage = runningNoticeImageRepository.save(runningNoticeImage);

        //then
        assertThat(savedImage).isEqualTo(runningNoticeImage);
    }

    @Test
    public void findById() {
        //given
        RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage", testRunningNotice());
        runningNoticeImageRepository.save(runningNoticeImage);

        ///when
        Optional<RunningNoticeImage> optRunningNoticeImage = runningNoticeImageRepository.findById(runningNoticeImage.getId());

        //then
        assertThat(optRunningNoticeImage).isNotEmpty();
        assertThat(optRunningNoticeImage).hasValue(runningNoticeImage);
    }

    @Test
    public void delete() {
        //given
        RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage", testRunningNotice());
        runningNoticeImageRepository.save(runningNoticeImage);

        ///when
        runningNoticeImageRepository.delete(runningNoticeImage);

        //then
        Optional<RunningNoticeImage> optRunningNoticeImage = runningNoticeImageRepository.findById(runningNoticeImage.getId());
        assertThat(optRunningNoticeImage).isEmpty();
    }

    @Test
    void findAllByRunningNotice() {
        //given
        RunningNotice runningNotice = testRunningNotice();
        for (int i = 0; i < 10; i++) {
            RunningNoticeImage runningNoticeImage = new RunningNoticeImage("boardImage" + i, runningNotice);
            runningNoticeImageRepository.save(runningNoticeImage);
        }

        //when
        List<RunningNoticeImage> runningNoticeImagesImages = runningNoticeImageRepository.findAllByRunningNotice(runningNotice);

        //then
        assertThat(runningNoticeImagesImages.size()).isSameAs(10);
        for (RunningNoticeImage runningNoticeImage : runningNoticeImagesImages) {
            assertThat(runningNoticeImage.getRunningNotice()).isEqualTo(runningNotice);
        }
    }

}