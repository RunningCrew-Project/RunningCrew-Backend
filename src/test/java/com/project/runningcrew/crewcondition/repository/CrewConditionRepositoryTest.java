package com.project.runningcrew.crewcondition.repository;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CrewConditionRepositoryTest {

    @Autowired
    CrewConditionRepository crewConditionRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("CrewCondition 저장 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        CrewCondition crewCondition = new CrewCondition(crew);

        ///when
        CrewCondition savedCrewCondition = crewConditionRepository.save(crewCondition);

        //then
        assertThat(savedCrewCondition).isEqualTo(crewCondition);
    }

    @DisplayName("CrewCondition id 로 찾기 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        CrewCondition crewCondition = new CrewCondition(crew);
        crewConditionRepository.save(crewCondition);

        ///when
        Optional<CrewCondition> optionalCrewCondition = crewConditionRepository.findById(crewCondition.getId());

        //then
        assertThat(optionalCrewCondition).isNotEmpty();
        assertThat(optionalCrewCondition).hasValue(crewCondition);
    }

    @DisplayName("CrewCondition 삭제 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        CrewCondition crewCondition = new CrewCondition(crew);
        crewConditionRepository.save(crewCondition);

        ///when
        crewConditionRepository.delete(crewCondition);

        //then
        Optional<CrewCondition> optionalCrewCondition = crewConditionRepository
                .findById(crewCondition.getId());
        assertThat(optionalCrewCondition).isEmpty();
    }

    @DisplayName("crew 로 CrewCondition 찾기 테스트")
    @Test
    public void findByCrewTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        CrewCondition crewCondition = new CrewCondition(crew);
        crewConditionRepository.save(crewCondition);

        ///when
        Optional<CrewCondition> optionalCrewCondition = crewConditionRepository.findByCrew(crew);

        //then
        assertThat(optionalCrewCondition).isNotEmpty();
        assertThat(optionalCrewCondition).hasValue(crewCondition);
    }

    @DisplayName("crew 로 CrewCondition 삭제 테스트")
    @Test
    public void deleteByCrewTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        CrewCondition crewCondition = new CrewCondition(crew);
        crewConditionRepository.save(crewCondition);

        ///when
        crewConditionRepository.deleteByCrew(crew);

        //then
        Optional<CrewCondition> optionalCrewCondition = crewConditionRepository.findByCrew(crew);
        assertThat(optionalCrewCondition).isEmpty();
    }

}