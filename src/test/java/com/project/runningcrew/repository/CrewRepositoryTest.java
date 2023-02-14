package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.repository.areas.DongAreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

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
    TestEntityFactory testEntityFactory;

    @BeforeEach
    public void saveTestCrews() {
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea1 = testEntityFactory.getDongArea(guArea, 1);
        DongArea dongArea2 = testEntityFactory.getDongArea(guArea, 2);

        for (int i = 0; i < 25; i++) {
            Crew crew = Crew.builder().name("crew"+i)
                    .introduction("introduction"+i)
                    .crewImgUrl("crewImageUrl"+i)
                    .dongArea(dongArea1)
                    .build();
            crewRepository.save(crew);
        }

        for (int i = 25; i < 50; i++) {
            Crew crew = Crew.builder().name("crew"+i)
                    .introduction("introduction"+i)
                    .crewImgUrl("crewImageUrl"+i)
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

        ///when
        Optional<Crew> optCrew = crewRepository.findById(crew.getId());

        //then
        assertThat(optCrew).isNotEmpty();
        assertThat(optCrew).hasValue(crew);
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

        //then
        Optional<Crew> optCrew = crewRepository.findById(crew.getId());
        assertThat(optCrew).isEmpty();
    }

    @Test
    public void findAllByNameOrIntroductionOrLocationTest() {
        //given
        String keyword = "ng1";

        ///when
        List<Crew> crewList = crewRepository.findAllByNameOrIntroductionOrArea(keyword);

        //then
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
    public void findRandomByLocationTest() {
        //given
        int maxNum = 5;
        String dongAreaName = "dong1";
        Optional<DongArea> optDongArea = dongAreaRepository.findByName(dongAreaName);

        ///when
        Long dongAreaId = optDongArea.get().getId();
        List<Crew> randomCrewList = crewRepository.findRandomByDongAreaId(dongAreaId, maxNum);

        //then
        assertThat(randomCrewList.size()).isSameAs(maxNum);
        for (Crew crew : randomCrewList) {
            assertThat(crew.getDongArea()).isEqualTo(optDongArea.get());
        }
    }

}