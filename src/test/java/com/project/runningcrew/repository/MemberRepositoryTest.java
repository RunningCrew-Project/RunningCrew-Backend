package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CrewRepository crewRepository;

    @BeforeEach
    public void saveTestUserAndCrew() {

    }

    @Test
    public void save() {
        //given
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

        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();

        userRepository.save(user);
        crewRepository.save(crew);
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);

        ///when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember).isEqualTo(member);

    }

    @Test
    public void findById() {
        //given
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

        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();

        userRepository.save(user);
        crewRepository.save(crew);
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        memberRepository.save(member);

        ///when
        Optional<Member> optMember = memberRepository.findById(member.getId());

        //then
        assertThat(optMember).isNotEmpty();
        assertThat(optMember).hasValue(member);
    }

    @Test
    public void delete() {
        //given
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

        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();

        userRepository.save(user);
        crewRepository.save(crew);
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        Member savedMember = memberRepository.save(member);

        ///when
        memberRepository.delete(savedMember);

        //then
        Optional<Member> optMember = memberRepository.findById(savedMember.getId());
        assertThat(optMember).isEmpty();
    }

    @Test
    void findAllByUserTest() {
        //given
        User user = User.builder().email("email@naver.com")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("010-0000-0000")
                .password("123a!")
                .location("location")
                .password("123a!")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .height(170)
                .weight(70)
                .build();
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .location("location" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl" + i)
                    .build();
            crewRepository.save(crew);

            Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        ///when
        List<Member> members = memberRepository.findAllByUser(user);

        //then
        assertThat(members.size()).isSameAs(10);
        for (Member member : members) {
            assertThat(member.getUser()).isEqualTo(user);
        }
    }

    @Test
    void countAllByCrewTest() {
        //given
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();
        crewRepository.save(crew);

        for (int i = 0; i < 10; i++) {
            User user = User.builder().email("email" + i + "@naver.com")
                    .name("name" + i)
                    .nickname("nickname" + i)
                    .imgUrl("imgUrl" + i)
                    .login_type(LoginType.EMAIL)
                    .phoneNumber("010-0000-0000")
                    .password("123a!")
                    .location("location" + i)
                    .sex(Sex.MAN)
                    .birthday(LocalDate.of(1990, 1, 1))
                    .height(170)
                    .weight(70)
                    .build();
            userRepository.save(user);

            Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        ///when
        Long count = memberRepository.countAllByCrew(crew);

        //then
        assertThat(count).isSameAs(10L);
    }

    @Test
    void findAllByCrewTest() {
        //given
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();
        crewRepository.save(crew);

        for (int i = 0; i < 10; i++) {
            User user = User.builder().email("email" + i + "@naver.com")
                    .name("name" + i)
                    .nickname("nickname" + i)
                    .imgUrl("imgUrl" + i)
                    .login_type(LoginType.EMAIL)
                    .phoneNumber("010-0000-0000")
                    .password("123a!")
                    .location("location" + i)
                    .sex(Sex.MAN)
                    .birthday(LocalDate.of(1990, 1, 1))
                    .height(170)
                    .weight(70)
                    .build();
            userRepository.save(user);

            Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        ///when
        List<Member> members = memberRepository.findAllByCrew(crew);

        //then
        assertThat(members.size()).isSameAs(10);
        for (Member member : members) {
            assertThat(member.getCrew()).isEqualTo(crew);
        }
    }

    @Test
    void findAllByCrewAndRoleTest() {
        //given
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();
        crewRepository.save(crew);

        for (int i = 0; i < 10; i++) {
            User user = User.builder().email("email" + i + "@naver.com")
                    .name("name" + i)
                    .nickname("nickname" + i)
                    .imgUrl("imgUrl" + i)
                    .login_type(LoginType.EMAIL)
                    .phoneNumber("010-0000-0000")
                    .password("123a!")
                    .location("location" + i)
                    .sex(Sex.MAN)
                    .birthday(LocalDate.of(1990, 1, 1))
                    .height(170)
                    .weight(70)
                    .build();
            userRepository.save(user);

            Member member;
            if (i == 0) {
                member = new Member(user, crew, MemberRole.ROLE_LEADER);
            } else if (i >= 1 && i < 5) {
                member = new Member(user, crew, MemberRole.ROLE_MANAGER);
            } else {
                member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            }
            memberRepository.save(member);
        }

        ///when
        List<Member> leaderMembers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_LEADER);
        List<Member> managerMembers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_MANAGER);
        List<Member> normalMembers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_NORMAL);

        //then
        assertThat(leaderMembers.size()).isSameAs(1);
        assertThat(managerMembers.size()).isSameAs(4);
        assertThat(normalMembers.size()).isSameAs(5);

        for (Member leaderMember : leaderMembers) {
            assertThat(leaderMember.getRole()).isSameAs(MemberRole.ROLE_LEADER);
        }
        for (Member managerMember : managerMembers) {
            assertThat(managerMember.getRole()).isSameAs(MemberRole.ROLE_MANAGER);
        }
        for (Member normalMember : normalMembers) {
            assertThat(normalMember.getRole()).isSameAs(MemberRole.ROLE_NORMAL);
        }
    }

}