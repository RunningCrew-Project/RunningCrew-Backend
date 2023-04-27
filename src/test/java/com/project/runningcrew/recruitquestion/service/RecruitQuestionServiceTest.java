package com.project.runningcrew.recruitquestion.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.recruitquestion.entity.RecruitQuestion;
import com.project.runningcrew.recruitquestion.repository.RecruitQuestionRepository;
import com.project.runningcrew.recruitquestion.service.RecruitQuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitQuestionServiceTest {

    @Mock
    RecruitQuestionRepository recruitQuestionRepository;

    @InjectMocks
    RecruitQuestionService recruitQuestionService;

    @DisplayName("가입 질문 하나 저장하기 테스트")
    @Test
    void saveOneQuestionTest(@Mock Crew crew) throws Exception {
        //given
        Long id = 1L;
        RecruitQuestion recruitQuestion = new RecruitQuestion(id, crew, "question", 0);
        when(recruitQuestionRepository.save(recruitQuestion)).thenReturn(recruitQuestion);

        //when
        Long savedId = recruitQuestionService.saveOneQuestion(recruitQuestion);

        //then
        assertThat(savedId).isSameAs(id);
    }

    @DisplayName("가입 질문 하나 삭제하기 테스트")
    @Test
    void deleteOneQuestionTest(@Mock Crew crew) throws Exception {
        //given
        Long id = 1L;
        RecruitQuestion recruitQuestion = new RecruitQuestion(id, crew, "question", 0);
        doNothing().when(recruitQuestionRepository).delete(recruitQuestion);

        //when
        recruitQuestionService.deleteOneQuestion(recruitQuestion);

        //then
        verify(recruitQuestionRepository, times(1)).delete(recruitQuestion);
    }

    @DisplayName("가입 질문 리스트를 정렬하여 출력하기 테스트")
    @Test
    void findAllByCrewTest(@Mock Crew crew) throws Exception {
        //given
        List<RecruitQuestion> questionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            questionList.add(new RecruitQuestion(crew, "question" + i, i));
        }
        when(recruitQuestionRepository.findAllByCrew(crew)).thenReturn(questionList);

        //when
        List<RecruitQuestion> findQuestionList = recruitQuestionService.findAllByCrew(crew);

        //then
        verify(recruitQuestionRepository, times(1)).findAllByCrew(crew);
    }

    @DisplayName("가입 질문 리스트 설정 테스트")
    @Test
    public void setRecruitQuestionTest(@Mock Crew crew) {
        //given
        List<RecruitQuestion> recruitQuestions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            recruitQuestions.add(new RecruitQuestion((long) i, crew, "question" + i, i));
        }
        doNothing().when(recruitQuestionRepository).deleteAllByCrew(crew);
        when(recruitQuestionRepository.saveAll(recruitQuestions)).thenReturn(recruitQuestions);

        ///when
        recruitQuestionService.setRecruitQuestions(crew, recruitQuestions);

        //then
        verify(recruitQuestionRepository, times(1)).deleteAllByCrew(crew);
        verify(recruitQuestionRepository, times(1)).saveAll(recruitQuestions);
    }


}