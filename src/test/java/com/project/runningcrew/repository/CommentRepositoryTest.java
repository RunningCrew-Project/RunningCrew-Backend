package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Comment;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.boards.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    /**
     * 테스트 용도 Board 는 FreeBoard 객체를 구현함.
     */

    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired CommentRepository commentRepository;
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

    public FreeBoard testFreeBoard(Member member) {
        FreeBoard freeBoard = new FreeBoard(member, "title", "detail");
        return boardRepository.save(freeBoard);
    }


    @DisplayName("Comment save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard freeBoard = testFreeBoard(member);
        Comment comment = new Comment(member, "detail", freeBoard);

        //when
        Comment savedComment = commentRepository.save(comment);

        //then
        Assertions.assertThat(savedComment).isEqualTo(comment);
    }


    @DisplayName("Comment findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard freeBoard = testFreeBoard(member);
        Comment savedComment = commentRepository.save(new Comment(member, "detail", freeBoard));

        //when
        Optional<Comment> findCommentOpt = commentRepository.findById(savedComment.getId());

        //then
        Assertions.assertThat(findCommentOpt).isNotEmpty();
        Assertions.assertThat(findCommentOpt).hasValue(savedComment);
    }

    @DisplayName("Comment delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard freeBoard = testFreeBoard(member);
        Comment savedComment = commentRepository.save(new Comment(member, "detail", freeBoard));

        //when
        commentRepository.delete(savedComment);

        //then
        Optional<Comment> findCommentOpt = commentRepository.findById(savedComment.getId());
        Assertions.assertThat(findCommentOpt).isEmpty();
    }


    @DisplayName("특정 BoardId 를 가진 Comment 출력 테스트")
    @Test
    void findAllByBoardTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
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
                        .dongArea(dongArea)
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );

        Member member = memberRepository.save(
                new Member(user, crew, MemberRole.ROLE_NORMAL)
        );


        String title = "title";
        String detail = "detail";
        FreeBoard boardA = boardRepository.save(new FreeBoard(member, title, detail));
        FreeBoard boardB = boardRepository.save(new FreeBoard(member, title, detail));
        commentRepository.save(new Comment(member, detail, boardA)); //A 저장
        commentRepository.save(new Comment(member ,detail, boardA)); //A 저장
        commentRepository.save(new Comment(member, detail, boardB)); //B 저장

        //when
        List<Comment> findCommentList = commentRepository.findAllByBoard(boardA);

        //then
        Assertions.assertThat(findCommentList.size()).isEqualTo(2);
    }



    @DisplayName("특정 MemberId 를 가진 Comment 출력 테스트")
    @Test
    void findAllByMemberIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
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
                        .dongArea(dongArea)
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );

        Member testBoardCreateMember = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL)); // 테스트 게시물 작성 멤버
        Member memberA = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL)); //A 멤버
        Member memberB = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL)); //B 멤버

        String title = "title";
        String detail = "detail";
        FreeBoard testBoard = boardRepository.save(new FreeBoard(testBoardCreateMember, title, detail)); // 테스트 게시물

        commentRepository.save(new Comment(memberA, detail, testBoard)); // A 멤버 댓글
        commentRepository.save(new Comment(memberA, detail, testBoard)); // A 멤버 댓글
        commentRepository.save(new Comment(memberB, detail, testBoard)); // B 멤버 댓글

        //when
        List<Comment> findCommentList = commentRepository.findAllByMember(memberA);

        //then
        Assertions.assertThat(findCommentList.size()).isEqualTo(2);
    }


}