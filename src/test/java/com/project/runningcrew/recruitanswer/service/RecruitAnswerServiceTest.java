package com.project.runningcrew.recruitanswer.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import com.project.runningcrew.recruitanswer.service.RecruitAnswerService;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.recruitanswer.repository.RecruitAnswerRepository;
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
class RecruitAnswerServiceTest {

    @Mock
    RecruitAnswerRepository recruitAnswerRepository;

    @InjectMocks
    RecruitAnswerService recruitAnswerService;


    @DisplayName("가입 답변 저장하기 테스트")
    @Test
    void saveAllRecruitAnswerTest(@Mock User user, @Mock Crew crew) throws Exception {
        //given
        List<RecruitAnswer> answerList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            answerList.add(new RecruitAnswer(user, crew, "answer" + i, i));
        }

    }

    @DisplayName("특정 유저가 특정 크루에 작성한 가입 답변 모두 삭제하기 테스트")
    @Test
    void deleteAllRecruitAnswerTest(@Mock User user, @Mock Crew crew) throws Exception {
        //given
        List<RecruitAnswer> answerList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            answerList.add(new RecruitAnswer(user, crew, "answer" + i, i));
        } // size = 5
        doNothing().when(recruitAnswerRepository).deleteByUserAndCrew(user, crew);

        //when
        recruitAnswerService.deleteAllRecruitAnswer(user, crew);

        //then
        verify(recruitAnswerRepository, times(1)).deleteByUserAndCrew(user, crew);
    }

    @DisplayName("특정 유저가 특정 크루에 작성한 가입 답변 정렬후 출력 테스트")
    @Test
    void findAllByUserAndCrewTest(@Mock User user, @Mock Crew crew) throws Exception {
        //given
        List<RecruitAnswer> answerList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            answerList.add(new RecruitAnswer(user, crew, "answer" + i, i));
        } // [0 .. 4]
        when(recruitAnswerRepository.findAllByUserAndCrew(user, crew)).thenReturn(answerList);

        //when
        List<RecruitAnswer> findList = recruitAnswerService.findAllByUserAndCrew(user, crew);

        //then
        assertThat(findList.size()).isEqualTo(5);
        verify(recruitAnswerRepository, times(1)).findAllByUserAndCrew(user, crew);
    }

    @DisplayName("특정 크루에 가입답변을 작성한 유저 리스트 출력 테스트")
    @Test
    void findUserByCrewTest(@Mock Crew crew) throws Exception {
        //given
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userList.add(User.builder().build());
        }
        when(recruitAnswerRepository.findUserByCrew(crew)).thenReturn(userList);

        //when
        List<User> findList = recruitAnswerService.findUserByCrew(crew);

        //then
        assertThat(findList.size()).isEqualTo(5);
        verify(recruitAnswerRepository, times(1)).findUserByCrew(crew);
    }


}