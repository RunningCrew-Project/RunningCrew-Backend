package com.project.runningcrew.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.runningmember.entity.RunningMember;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.runningmember.repository.RunningMemberRepository;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.entity.RunningStatus;
import com.project.runningcrew.runningnotice.repository.RunningNoticeRepository;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.area.repository.DongAreaRepository;
import com.project.runningcrew.area.repository.GuAreaRepository;
import com.project.runningcrew.area.repository.SidoAreaRepository;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.runningrecord.repository.RunningRecordRepository;
import com.project.runningcrew.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TestEntityFactory {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RunningNoticeRepository runningNoticeRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    RunningRecordRepository runningRecordRepository;

    @Autowired
    RunningMemberRepository runningMemberRepository;

    @Autowired
    SidoAreaRepository sidoAreaRepository;

    @Autowired
    GuAreaRepository guAreaRepository;

    @Autowired
    DongAreaRepository dongAreaRepository;

    public User getUser(DongArea dongArea, int num) {
        User user = User.builder().email("email" + num + "@naver.com")
                .name("name" + num)
                .nickname("nickname" + num)
                .imgUrl("imgUrl" + num)
                .login_type(LoginType.EMAIL)
                .phoneNumber("010-0000-0000")
                .dongArea(dongArea)
                .password("123a!")
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .height(170)
                .weight(70)
                .build();
        return userRepository.save(user);
    }

    public Crew getCrew(DongArea dongArea, int num) {
        Crew crew = Crew.builder().name("crew" + num)
                .introduction("introduction" + num)
                .crewImgUrl("crewImageUrl" + num)
                .dongArea(dongArea)
                .build();
        return crewRepository.save(crew);
    }

    public Member getMember(User user, Crew crew) {
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }

    public RunningNotice getRegularRunningNotice(Member member, int num) {
        RunningNotice runningNotice = RunningNotice.builder().title("title" + num)
                .detail("detail" + num)
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.READY)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }

    public RunningNotice getInstantRunningNotice(Member member, int num) {
        RunningNotice runningNotice = RunningNotice.builder().title("title" + num)
                .detail("detail" + num)
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.READY)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }

    public FreeBoard getFreeBoard(Member member, int num) {
        FreeBoard freeBoard = new FreeBoard(member, "title" + num, "content" + num);
        return boardRepository.save(freeBoard);
    }

    public NoticeBoard getNoticeBoard(Member member, int num) {
        NoticeBoard noticeBoard = new NoticeBoard(member, "title" + num, "content" + num);
        return boardRepository.save(noticeBoard);
    }

    public PersonalRunningRecord getPersonalRunningRecord(User user, int num) {
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("personal")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail(String.valueOf(num))
                .user(user)
                .build();
        return runningRecordRepository.save(personalRunningRecord);
    }

    public SidoArea getSidoArea(int num) {
        SidoArea sidoArea = new SidoArea("sido" + num);
        return sidoAreaRepository.save(sidoArea);
    }

    public GuArea getGuArea(SidoArea sidoArea, int num) {
        GuArea guArea = new GuArea("gu" + num, sidoArea);
        return guAreaRepository.save(guArea);
    }

    public DongArea getDongArea(GuArea guArea, int num) {
        DongArea dongArea = new DongArea("dong" + num, guArea);
        return dongAreaRepository.save(dongArea);
    }

    public RunningMember getRunningMember(RunningNotice runningNotice, Member member) {
        RunningMember runningMember = new RunningMember(runningNotice, member);
        return runningMemberRepository.save(runningMember);
    }

}
