package com.project.runningcrew.crew.repository;

import com.project.runningcrew.area.repository.GuAreaRepository;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.area.repository.DongAreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CrewRepositoryTest {

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    DongAreaRepository dongAreaRepository;

    @Autowired
    GuAreaRepository guAreaRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void saveTestCrews() {
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea1 = testEntityFactory.getDongArea(guArea, 1);
        DongArea dongArea2 = testEntityFactory.getDongArea(guArea, 2);

        for (int i = 0; i < 25; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl" + i)
                    .dongArea(dongArea1)
                    .build();
            crewRepository.save(crew);
        }

        for (int i = 25; i < 50; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl" + i)
                    .dongArea(dongArea2)
                    .build();
            crewRepository.save(crew);
        }
    }

    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = Crew.builder().name("crew")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .dongArea(dongArea)
                .build();

        ///when
        Crew savedCrew = crewRepository.save(crew);

        //then
        assertThat(savedCrew).isEqualTo(savedCrew);
    }

    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = Crew.builder().name("crew")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .dongArea(dongArea)
                .build();
        crewRepository.save(crew);
        em.flush();
        em.clear();

        ///when
        Optional<Crew> optCrew = crewRepository.findById(crew.getId());

        //then
        assertThat(optCrew).isNotEmpty();
        assertThat(optCrew.get().getId()).isEqualTo(crew.getId());
    }

    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = Crew.builder().name("crew")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .dongArea(dongArea)
                .build();
        crewRepository.save(crew);


        ///when
        crewRepository.delete(crew);
        em.flush();
        em.clear();

        //then
        Optional<Crew> optCrew = crewRepository.findById(crew.getId());
        assertThat(optCrew).isEmpty();
    }

    @Test
    public void findAllByNameOrIntroductionOrLocationTest() {
        //given
        String keyword = "ng1";

        ///when
        long start = System.currentTimeMillis();
        List<Crew> crewList = crewRepository.findAllByNameOrIntroductionOrArea(keyword);
        long end = System.currentTimeMillis();

        //then
        System.out.println("time = " + (end - start) + "ms");
        assertThat(crewList.size()).isEqualTo(25);
    }

    @Test
    public void countAllByNameOrIntroductionOrLocationTest() {
        //given
        String keyword = "ng1";

        ///when
        Long count = crewRepository.countAllByNameOrIntroductionOrArea(keyword);

        //then
        assertThat(count).isEqualTo(25L);
    }

    @Test
    public void findByNameOrIntroductionOrLocationTest() {
        //given
        int size = 15;
        PageRequest pageRequest = PageRequest.of(0, size);
        String keyword = "ng1";

        ///when
        Slice<Crew> slice = crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword);

        //then
        assertThat(slice.getSize()).isEqualTo(size);
    }

    @Test
    public void findRandomByDongAreaId() {
        //given
        int maxNum = 5;
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        for (int i = 50; i < 60; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl")
                    .dongArea(dongArea)
                    .build();
            crewRepository.save(crew);
        }

        ///when
        Long dongAreaId = dongArea.getId();
        List<Crew> randomCrewList = crewRepository.findRandomByDongAreaId(dongAreaId, maxNum);

        //then
        assertThat(randomCrewList.size()).isSameAs(maxNum);
        for (Crew crew : randomCrewList) {
            assertThat(crew.getDongArea()).isEqualTo(dongArea);
        }
    }

    @Test
    public void findAllByUserTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        for (int i = 50; i < 60; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl")
                    .dongArea(dongArea)
                    .build();
            crewRepository.save(crew);
            memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        }
        em.flush();
        em.clear();

        ///when
        List<Crew> crews = crewRepository.findAllByUser(user);

        //then
        assertThat(crews.size()).isSameAs(10);
    }

    @DisplayName("특정 이름을 가진 크루 존재 테스트")
    @Test
    public void existsByNameTest1() {
        //given
        String name = "crew13";

        ///when
        boolean exist = crewRepository.existsByName(name);

        //then
        assertThat(exist).isTrue();
    }

    @DisplayName("특정 이름을 가진 크루 미존재 테스트")
    @Test
    public void existsByNameTest2() {
        //given
        String name = "crew133";

        ///when
        boolean exist = crewRepository.existsByName(name);

        //then
        assertThat(exist).isFalse();
    }

    @DisplayName("특정 구의 id 를 가진 크루 랜덤 반환 테스트")
    @Test
    public void findRandomByGuAreaIdTest() {
        //given
        int maxNum = 5;
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        for (int i = 50; i < 60; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl")
                    .dongArea(dongArea)
                    .build();
            crewRepository.save(crew);
        }

        //when
        Long guAreaId = guArea.getId();
        List<Crew> randomCrewList = crewRepository.findRandomByGuAreaId(guAreaId, maxNum);

        //then
        assertThat(randomCrewList.size()).isSameAs(maxNum);
        for (Crew crew : randomCrewList) {
            assertThat(crew.getDongArea().getGuArea()).isEqualTo(guArea);
        }
    }

}