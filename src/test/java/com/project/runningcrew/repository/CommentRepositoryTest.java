package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Comment;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.boards.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @DisplayName("Comment save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        Crew crew1 = Crew.builder()
                .name("name")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();

        Member member1 = new Member(user1, crew1, MemberRole.ROLE_NORMAL);

        String title = "title";
        String detail = "detail";
        Board board1 = new FreeBoard(member1, title, detail);
        userRepository.save(user1);
        crewRepository.save(crew1);
        memberRepository.save(member1);
        boardRepository.save(board1);
        //when
        Comment comment1 = new Comment(member1, detail, board1);
        Comment savedComment = commentRepository.save(comment1);
        //then
        Assertions.assertThat(savedComment).isEqualTo(comment1);
    }


    @DisplayName("Comment findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        Crew crew1 = Crew.builder()
                .name("name")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();

        Member member1 = new Member(user1, crew1, MemberRole.ROLE_NORMAL);

        String title = "title";
        String detail = "detail";
        Board board1 = new FreeBoard(member1, title, detail);
        userRepository.save(user1);
        crewRepository.save(crew1);
        memberRepository.save(member1);
        boardRepository.save(board1);
        //when
        Comment comment1 = new Comment(member1, detail, board1);
        Comment savedComment = commentRepository.save(comment1);
        Long savedCommentId = savedComment.getId();
        Optional<Comment> findCommentOpt = commentRepository.findById(savedCommentId);
        //then
        Assertions.assertThat(findCommentOpt).isNotEmpty();
        Assertions.assertThat(findCommentOpt).hasValue(savedComment);
    }

    @DisplayName("Comment delete 테스트")
    @Test
    void nameTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        Crew crew1 = Crew.builder()
                .name("name")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();

        Member member1 = new Member(user1, crew1, MemberRole.ROLE_NORMAL);

        String title = "title";
        String detail = "detail";
        Board board1 = new FreeBoard(member1, title, detail);
        userRepository.save(user1);
        crewRepository.save(crew1);
        memberRepository.save(member1);
        boardRepository.save(board1);
        //when
        Comment comment1 = new Comment(member1, detail, board1);
        Comment savedComment = commentRepository.save(comment1);
        Long savedCommentId = savedComment.getId();
        commentRepository.delete(savedComment);
        //then
        Optional<Comment> findCommentOpt = commentRepository.findById(savedCommentId);
        Assertions.assertThat(findCommentOpt).isEmpty();
    }


    @DisplayName("특정 BoardId 를 가진 Comment 출력 테스트")
    @Test
    void findAllByBoardIdTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        Crew crew1 = Crew.builder()
                .name("name")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();

        Member member1 = new Member(user1, crew1, MemberRole.ROLE_NORMAL);
        userRepository.save(user1);
        crewRepository.save(crew1);
        memberRepository.save(member1);

        String title = "title";
        String detail = "detail";
        FreeBoard boardA = boardRepository.save(new FreeBoard(member1, title, detail));
        FreeBoard boardB = boardRepository.save(new FreeBoard(member1, title, detail));
        commentRepository.save(new Comment(member1, detail, boardA)); //A 저장
        commentRepository.save(new Comment(member1 ,detail, boardA)); //A 저장
        commentRepository.save(new Comment(member1, detail, boardB)); //B 저장
        //when
        Long boardAId = boardA.getId();
        List<Comment> findCommentList = commentRepository.findAllByBoardId(boardAId);
        //then
        Assertions.assertThat(findCommentList.size()).isEqualTo(2);
    }



    @DisplayName("특정 MemberId 를 가진 Comment 출력 테스트")
    @Test
    void findAllByMemberIdTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        Crew crew1 = Crew.builder()
                .name("name")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();

        userRepository.save(user1);
        crewRepository.save(crew1);

        Member testBoardCreateMember = memberRepository.save(new Member(user1, crew1, MemberRole.ROLE_NORMAL)); // 테스트 게시물 작성 멤버
        Member memberA = memberRepository.save(new Member(user1, crew1, MemberRole.ROLE_NORMAL)); //A 멤버
        Member memberB = memberRepository.save(new Member(user1, crew1, MemberRole.ROLE_NORMAL)); //B 멤버

        String title = "title";
        String detail = "detail";
        FreeBoard testBoard = boardRepository.save(new FreeBoard(testBoardCreateMember, title, detail)); // 테스트 게시물

        //when
        commentRepository.save(new Comment(memberA, detail, testBoard)); // A 멤버 댓글
        commentRepository.save(new Comment(memberA, detail, testBoard)); // A 멤버 댓글
        commentRepository.save(new Comment(memberB, detail, testBoard)); // B 멤버 댓글

        Long memberAId = memberA.getId();
        List<Comment> findCommentList = commentRepository.findAllByMemberId(memberAId);
        //then
        Assertions.assertThat(findCommentList.size()).isEqualTo(2);
    }


}