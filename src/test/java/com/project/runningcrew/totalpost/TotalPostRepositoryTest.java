package com.project.runningcrew.totalpost;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.totalpost.entity.TotalPost;
import com.project.runningcrew.totalpost.repository.TotalPostRepository;
import com.project.runningcrew.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TotalPostRepositoryTest {

    @Autowired
    TotalPostRepository totalPostRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("키워드로 전체 글 페이징 처음")
    @Test
    public void getTotalPostByKeywordTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        for (int i = 0; i < 20; i++) {
            testEntityFactory.getFreeBoard(member, i);
            testEntityFactory.getInstantRunningNotice(member, i);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByKeyword("title1", pageRequest);

        //then
        assertThat(totalPosts.getNumberOfElements()).isEqualTo(10);
        assertThat(totalPosts.getNumber()).isSameAs(0);
        assertThat(totalPosts.getSize()).isSameAs(10);
        assertThat(totalPosts.isFirst()).isTrue();
        assertThat(totalPosts.hasPrevious()).isFalse();
        assertThat(totalPosts.hasNext()).isTrue();
    }

    @DisplayName("키워드로 전체 글 페이징 마지막")
    @Test
    public void getTotalPostByKeywordTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        for (int i = 0; i < 20; i++) {
            testEntityFactory.getFreeBoard(member, i);
            testEntityFactory.getInstantRunningNotice(member, i);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(2, 10);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByKeyword("title1", pageRequest);

        //then
        assertThat(totalPosts.getNumberOfElements()).isEqualTo(2);
        assertThat(totalPosts.getNumber()).isSameAs(2);
        assertThat(totalPosts.getSize()).isSameAs(10);
        assertThat(totalPosts.isLast()).isTrue();
        assertThat(totalPosts.hasPrevious()).isTrue();
        assertThat(totalPosts.hasNext()).isFalse();
    }

    @DisplayName("멤버로 전체 글 페이징 처음")
    @Test
    public void getTotalPostByMemberTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        for (int i = 0; i < 8; i++) {
            testEntityFactory.getFreeBoard(member, i);
            testEntityFactory.getInstantRunningNotice(member, i);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByMember(member.getId(), pageRequest);

        //then
        assertThat(totalPosts.getNumberOfElements()).isEqualTo(10);
        assertThat(totalPosts.getNumber()).isSameAs(0);
        assertThat(totalPosts.getSize()).isSameAs(10);
        assertThat(totalPosts.isFirst()).isTrue();
        assertThat(totalPosts.hasPrevious()).isFalse();
        assertThat(totalPosts.hasNext()).isTrue();
    }

    @DisplayName("멤버로 전체 글 페이징 마지막")
    @Test
    public void getTotalPostByMemberTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        for (int i = 0; i < 8; i++) {
            testEntityFactory.getFreeBoard(member, i);
            testEntityFactory.getInstantRunningNotice(member, i);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(1, 10);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByMember(member.getId(), pageRequest);

        //then
        assertThat(totalPosts.getNumberOfElements()).isEqualTo(6);
        assertThat(totalPosts.getNumber()).isSameAs(1);
        assertThat(totalPosts.getSize()).isSameAs(10);
        assertThat(totalPosts.isLast()).isTrue();
        assertThat(totalPosts.hasPrevious()).isTrue();
        assertThat(totalPosts.hasNext()).isFalse();
    }

}