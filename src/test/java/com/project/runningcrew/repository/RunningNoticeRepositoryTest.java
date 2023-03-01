package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.runningrecords.CrewRunningRecord;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.runningrecords.CrewRunningRecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RunningNoticeRepositoryTest {


    @Autowired
    UserRepository userRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CrewRunningRecordRepository crewRunningRecordRepository;
    @Autowired
    RunningNoticeRepository runningNoticeRepository;
    @Autowired
    TestEntityFactory testEntityFactory;


    public User testUser(DongArea dongArea, int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname" + num)
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
                .name("name" + num)
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

    public RunningRecord testRunningRecord(User user) {
        int num = 1;
        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .title("crew")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();

        return crewRunningRecordRepository.save(crewRunningRecord);
    }


    @DisplayName("RunningNotice save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        int num = 1;
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNotice = RunningNotice.builder().title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.READY)
                .build();

        //when
        RunningNotice savedRunningNotice = runningNoticeRepository.save(runningNotice);

        //then
        Assertions.assertThat(savedRunningNotice).isEqualTo(runningNotice);
    }


    @DisplayName("RunningNotice findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        int num = 1;
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNotice = RunningNotice.builder().title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.READY)
                .build();
        RunningNotice savedRunningNotice = runningNoticeRepository.save(runningNotice);

        //when
        Optional<RunningNotice> findRunningNoticeOpt = runningNoticeRepository.findById(savedRunningNotice.getId());

        //then
        Assertions.assertThat(findRunningNoticeOpt).isNotEmpty();
        Assertions.assertThat(findRunningNoticeOpt).hasValue(savedRunningNotice);
    }


    @DisplayName("RunningNotice delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        int num = 1;
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNotice = RunningNotice.builder().title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.READY)
                .build();
        RunningNotice savedRunningNotice = runningNoticeRepository.save(runningNotice);

        //when
        runningNoticeRepository.delete(savedRunningNotice);
        Optional<RunningNotice> findRunningNoticeOpt = runningNoticeRepository.findById(savedRunningNotice.getId());

        //then
        Assertions.assertThat(findRunningNoticeOpt).isEmpty();
    }


    @DisplayName("특정 Member 가 작성한 RunningNotice 페이징 테스트")
    @Test
    void findAllByMemberTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User userA = testUser(dongArea, 1);
        User userB = testUser(dongArea, 2);
        Crew crew = testCrew(dongArea, 1);
        Member memberA = testMember(userA, crew);
        Member memberB = testMember(userB, crew);

        RunningNotice runningNoticeA = runningNoticeRepository.save(
                RunningNotice.builder().title("titleA")
                        .detail("detailA")
                        .member(memberA)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        );

        RunningNotice runningNoticeA2 = runningNoticeRepository.save(
                RunningNotice.builder().title("titleA2")
                        .detail("detailA2")
                        .member(memberA)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        );

        RunningNotice runningNoticeB = runningNoticeRepository.save(
                RunningNotice.builder().title("titleB")
                        .detail("detailB")
                        .member(memberB)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        );

        //when
        PageRequest pageRequest = PageRequest.of(0, 5);
        Slice<RunningNotice> findRunningNoticeListA = runningNoticeRepository.findAllByMember(memberA, pageRequest);
        Slice<RunningNotice> findRunningNoticeListB = runningNoticeRepository.findAllByMember(memberB, pageRequest);

        //then
        assertThat(findRunningNoticeListA.getNumber()).isSameAs(0);
        assertThat(findRunningNoticeListA.getSize()).isSameAs(5);
        assertThat(findRunningNoticeListA.getNumberOfElements()).isSameAs(2);
        assertThat(findRunningNoticeListA.hasPrevious()).isFalse();
        assertThat(findRunningNoticeListA.hasNext()).isFalse();
        assertThat(findRunningNoticeListA.isFirst()).isTrue();

        assertThat(findRunningNoticeListB.getSize()).isSameAs(5);
        assertThat(findRunningNoticeListB.getNumber()).isSameAs(0);
        assertThat(findRunningNoticeListB.getNumberOfElements()).isSameAs(1);
        assertThat(findRunningNoticeListB.hasPrevious()).isFalse();
        assertThat(findRunningNoticeListB.hasNext()).isFalse();
        assertThat(findRunningNoticeListB.isFirst()).isTrue();
    }

    @DisplayName("크루의 모든 런닝공지 반환 테스트")
    @Test
    public void findAllByCrewTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = RunningNotice.builder().title("title")
                    .detail("detail")
                    .member(member)
                    .noticeType(NoticeType.REGULAR)
                    .runningDateTime(LocalDateTime.of(2023, 2, 12, 0, 0))
                    .runningPersonnel(4)
                    .status(RunningStatus.READY)
                    .build();
            runningNoticeRepository.save(runningNotice);
        }

        ///when
        List<RunningNotice> runningNotices = runningNoticeRepository.findAllByCrew(crew);

        //then
        Assertions.assertThat(runningNotices.size()).isSameAs(10);
    }


    @DisplayName("각 NoticeType 에 해당하는 RunningNotice 페이징 출력 테스트")
    @Test
    void findAllByCrewAndNoticeTypeTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);

        RunningNotice runningNoticeA = runningNoticeRepository.save(
                RunningNotice.builder().title("title_REGULAR")
                        .detail("detail_REGULAR")
                        .member(member)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        );

        RunningNotice runningNoticeA2 = runningNoticeRepository.save(
                RunningNotice.builder().title("title_INSTANT")
                        .detail("detail_INSTANT")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        );

        RunningNotice runningNoticeB = runningNoticeRepository.save(
                RunningNotice.builder().title("title_INSTANT")
                        .detail("detail_INSTANT")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        );
        PageRequest pageRequest = PageRequest.of(0, 15); // size = 15

        //when
        Slice<RunningNotice> findRunningNoticeSliceA = runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.REGULAR, member.getCrew(), pageRequest);
        List<RunningNotice> contentA = findRunningNoticeSliceA.getContent();

        Slice<RunningNotice> findRunningNoticeSliceB = runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.INSTANT, member.getCrew(), pageRequest);
        List<RunningNotice> contentB = findRunningNoticeSliceB.getContent();

        //then
        Assertions.assertThat(contentA.size()).isEqualTo(1);
        Assertions.assertThat(contentB.size()).isEqualTo(2);

        // slice A Test
        Assertions.assertThat(findRunningNoticeSliceA.getNumber()).isEqualTo(0);
        Assertions.assertThat(findRunningNoticeSliceA.getNumberOfElements()).isEqualTo(1);
        Assertions.assertThat(findRunningNoticeSliceA.isFirst()).isTrue();
        Assertions.assertThat(findRunningNoticeSliceA.hasNext()).isFalse();

        // slice B Test
        Assertions.assertThat(findRunningNoticeSliceB.getNumber()).isEqualTo(0);
        Assertions.assertThat(findRunningNoticeSliceB.getNumberOfElements()).isEqualTo(2);
        Assertions.assertThat(findRunningNoticeSliceB.isFirst()).isTrue();
        Assertions.assertThat(findRunningNoticeSliceB.hasNext()).isFalse();
    }


    @DisplayName("특정 키워드 포함 RunningNotice 출력 테스트")
    @Test
    void findListAllByCrewAndKeyWordTest() throws Exception {
        //given
        String keyword = "key";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNoticeA = runningNoticeRepository.save(
                RunningNotice.builder().title("abc_key_def")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // title 에 포함

        RunningNotice runningNoticeA2 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("abc_key_def")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // detail 에 포함

        RunningNotice runningNoticeB = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 미포함

        //when
        List<RunningNotice> findRunningNoticeList = runningNoticeRepository.findListAllByCrewAndKeyWord(keyword, member.getCrew());

        //then
        Assertions.assertThat(findRunningNoticeList.size()).isEqualTo(2);
    }


    @DisplayName("특정 키워드 포함 RunningNotice 출력 테스트 paging 적용")
    @Test
    void findSliceAllByCrewAndKeyWordTest() throws Exception {
        //given
        String keyword = "key";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNoticeA = runningNoticeRepository.save(
                RunningNotice.builder().title("abc_key_def")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // title 에 포함

        RunningNotice runningNoticeA2 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("abc_key_def")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // detail 에 포함

        RunningNotice runningNoticeB = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 미포함

        //when
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("createdDate").descending());
        Slice<RunningNotice> slice = runningNoticeRepository
                .findSliceAllByCrewAndKeyWord(keyword, member.getCrew(),pageRequest);

        //then
        Assertions.assertThat(slice.getSize()).isEqualTo(5);
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.getNumberOfElements()).isEqualTo(2);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isFalse();
    }


    @DisplayName("특정 날에 시작하는 런닝의 RunningNotice 출력 테스트")
    @Test
    void findAllByCrewAndRunningDateTest() throws Exception {
        //given

        LocalDateTime today = LocalDateTime.of(2023, 2, 11, 0, 0);
        LocalDateTime tomorrow = LocalDateTime.of(2023, 2, 12, 0, 0);

        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNotice1 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 2, 11, 0, 1))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 23/02/11, 00시 01분

        RunningNotice runningNotice2 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 2, 11, 23, 59))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 23/02/11, 23시 59분

        RunningNotice runningNotice3 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 2, 12, 0, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 23/02/12, 00시 01분

        //when
        List<RunningNotice> findRunningNoticeList = runningNoticeRepository.findAllByCrewAndRunningDate(today, tomorrow, member.getCrew());

        //then
        Assertions.assertThat(findRunningNoticeList.size()).isEqualTo(2);
    }


    @DisplayName("특정 Crew 의 RunningNotice 를 status 에 따라 RunningDate 순으로 출력 테스트")
    @Test
    void findAllByCrewAndStatusTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        RunningNotice runningNotice1 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.REGULAR)
                        .runningDateTime(LocalDateTime.of(2023, 2, 12, 0, 0))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 23/02/12, 00시 00분

        RunningNotice runningNotice2 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 2, 11, 23, 59))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 23/02/11, 23시 59분

        RunningNotice runningNotice3 = runningNoticeRepository.save(
                RunningNotice.builder().title("title")
                        .detail("detail")
                        .member(member)
                        .noticeType(NoticeType.INSTANT)
                        .runningDateTime(LocalDateTime.of(2023, 2, 11, 0, 1))
                        .runningPersonnel(4)
                        .status(RunningStatus.READY)
                        .build()
        ); // 23/02/11, 00시 01분

        //when
        List<RunningNotice> findRunningNoticeList = runningNoticeRepository.findAllByCrewAndStatus(RunningStatus.READY, member.getCrew());

        //then
        Assertions.assertThat(findRunningNoticeList.size()).isEqualTo(3);
        Assertions.assertThat(findRunningNoticeList.get(0)).isEqualTo(runningNotice3);
        Assertions.assertThat(findRunningNoticeList.get(1)).isEqualTo(runningNotice2);
        Assertions.assertThat(findRunningNoticeList.get(2)).isEqualTo(runningNotice1);
        // 정렬 확인

    }

    @DisplayName("특정 유저가 신청한 특정 상태의 모든 러닝 공지 반환 테스트")
    @Test
    public void findAllByUserAndStatusTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        for (int i = 1; i < 11; i++) {
            User tempUser = testUser(dongArea, i);
            Crew tempCrew = testCrew(dongArea, i);
            Member tempMember = testMember(tempUser, tempCrew);
            RunningNotice runningNotice = RunningNotice.builder().title("title")
                    .detail("detail")
                    .member(tempMember)
                    .noticeType(NoticeType.REGULAR)
                    .runningDateTime(LocalDateTime.of(2023, 2, 12, 0, 0))
                    .runningPersonnel(4)
                    .status(RunningStatus.READY)
                    .build();
            runningNoticeRepository.save(runningNotice);
            RunningMember runningMember = testEntityFactory.getRunningMember(runningNotice, member);
        }

        ///when
        List<RunningNotice> runningNotices = runningNoticeRepository.findAllByUserAndStatus(user, RunningStatus.READY);

        //then
        Assertions.assertThat(runningNotices.size()).isSameAs(10);
        for (RunningNotice runningNotice : runningNotices) {
            Assertions.assertThat(runningNotice.getStatus()).isSameAs(RunningStatus.READY);
        }
    }

}