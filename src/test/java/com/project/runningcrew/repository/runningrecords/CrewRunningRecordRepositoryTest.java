package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.runningrecords.CrewRunningRecord;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CrewRunningRecordRepositoryTest {

    @Autowired
    CrewRunningRecordRepository crewRunningRecordRepository;

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
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.WAIT)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }

    @Test
    public void saveTest() {
        //given
        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .member(testMember())
                .runningNotice(testRunningNotice())
                .build();

        ///when
        CrewRunningRecord savedCrewRunningRecord = crewRunningRecordRepository.save(crewRunningRecord);

        //then
        assertThat(savedCrewRunningRecord).isEqualTo(crewRunningRecord);
    }

    @Test
    public void findById() {
        //given
        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .member(testMember())
                .runningNotice(testRunningNotice())
                .build();
        crewRunningRecordRepository.save(crewRunningRecord);

        ///when
        Optional<CrewRunningRecord> optCrewRunningRecord = crewRunningRecordRepository.findById(crewRunningRecord.getId());

        //then
        assertThat(optCrewRunningRecord).isNotEmpty();
        assertThat(optCrewRunningRecord).hasValue(crewRunningRecord);
    }

    @Test
    public void delete() {
        //given
        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .member(testMember())
                .runningNotice(testRunningNotice())
                .build();
        crewRunningRecordRepository.save(crewRunningRecord);

        ///when
        crewRunningRecordRepository.delete(crewRunningRecord);

        //then
        Optional<CrewRunningRecord> optCrewRunningRecord = crewRunningRecordRepository.findById(crewRunningRecord.getId());
        assertThat(optCrewRunningRecord).isEmpty();
    }

    @Test
    public void findAllByMemberTest() {
        //given
        Member member = testMember();
        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = RunningNotice.builder().title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.REGULAR)
                    .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                    .runningPersonnel(4)
                    .status(RunningStatus.WAIT)
                    .build();
            runningNoticeRepository.save(runningNotice);

            CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                    .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .member(member)
                    .runningNotice(runningNotice)
                    .build();
            crewRunningRecordRepository.save(crewRunningRecord);
        }

        ///when
        List<CrewRunningRecord> crewRunningRecords = crewRunningRecordRepository.findAllByMember(member);

        //then
        assertThat(crewRunningRecords.size()).isSameAs(10);
        for (CrewRunningRecord crewRunningRecord : crewRunningRecords) {
            assertThat(crewRunningRecord.getMember()).isEqualTo(member);
        }
    }

}