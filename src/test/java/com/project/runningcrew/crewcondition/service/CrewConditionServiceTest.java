package com.project.runningcrew.crewcondition.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import com.project.runningcrew.crewcondition.repository.CrewConditionRepository;
import com.project.runningcrew.exception.notFound.CrewConditionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewConditionServiceTest {

    @Mock
    CrewConditionRepository crewConditionRepository;

    @InjectMocks
    CrewConditionService crewConditionService;


    @DisplayName("crew 로 CrewCondition 찾기 성공 테스트")
    @Test
    public void findByCrewTest1(@Mock Crew crew) {
        //given
        CrewCondition crewCondition = new CrewCondition(crew);
        when(crewConditionRepository.findByCrew(crew)).thenReturn(Optional.of(crewCondition));

        ///when
        CrewCondition findCrewCondition = crewConditionService.findByCrew(crew);

        //then
        assertThat(findCrewCondition).isEqualTo(crewCondition);
        verify(crewConditionRepository, times(1)).findByCrew(crew);
    }

    @DisplayName("crew 로 CrewCondition 찾기 예외 테스트")
    @Test
    public void findByCrewTest2(@Mock Crew crew) {
        //given
        CrewCondition crewCondition = new CrewCondition(crew);
        when(crewConditionRepository.findByCrew(crew)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> crewConditionService.findByCrew(crew))
                .isInstanceOf(CrewConditionNotFoundException.class);
    }

    @DisplayName("joinApply true 변경 테스트")
    @Test
    public void updateJoinApplyTrueTest(@Mock Crew crew) {
        //given
        CrewCondition crewCondition = new CrewCondition(crew);

        ///when
        crewCondition.updateJoinApply(true);

        //then
        assertThat(crewCondition.isJoinApply()).isTrue();
    }

    @DisplayName("joinApply false 변경 테스트")
    @Test
    public void updateJoinApplyFalseTest(@Mock Crew crew) {
        //given
        CrewCondition crewCondition = new CrewCondition(crew);

        ///when
        crewCondition.updateJoinApply(false);

        //then
        assertThat(crewCondition.isJoinApply()).isFalse();
    }

    @DisplayName("joinQuestion true 변경 테스트")
    @Test
    public void updateJoinQuestionTrueTest(@Mock Crew crew) {
        //given
        CrewCondition crewCondition = new CrewCondition(crew);

        ///when
        crewCondition.updateJoinQuestion(true);

        //then
        assertThat(crewCondition.isJoinQuestion()).isTrue();
    }

    @DisplayName("joinQuestion false 변경 테스트")
    @Test
    public void updateJoinQuestionFalseTest(@Mock Crew crew) {
        //given
        CrewCondition crewCondition = new CrewCondition(crew);

        ///when
        crewCondition.updateJoinQuestion(false);

        //then
        assertThat(crewCondition.isJoinQuestion()).isFalse();
    }

}