package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RecruitQuestion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecruitQuestionRepositoryTest {

    @Autowired CrewRepository crewRepository;
    @Autowired RecruitQuestionRepository recruitQuestionRepository;



    @DisplayName("RecruitQuestion save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .location("location")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );
        //when
        int offset = 0;
        String question = "question";
        RecruitQuestion recruitQuestion = new RecruitQuestion(crew, question, offset);
        RecruitQuestion savedRecruitQuestion = recruitQuestionRepository.save(recruitQuestion);
        //then
        Assertions.assertThat(recruitQuestion).isEqualTo(savedRecruitQuestion);
    }



    @DisplayName("RecruitQuestion findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .location("location")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );
        //when
        int offset = 0;
        String question = "question";
        RecruitQuestion savedRecruitQuestion = recruitQuestionRepository.save(new RecruitQuestion(crew, question, offset));
        Optional<RecruitQuestion> findRecruitQuestionOpt = recruitQuestionRepository.findById(savedRecruitQuestion.getId());
        //then
        Assertions.assertThat(findRecruitQuestionOpt).isNotEmpty();
        Assertions.assertThat(findRecruitQuestionOpt).hasValue(savedRecruitQuestion);
    }



    @DisplayName("RecruitQuestion delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .location("location")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );
        //when
        int offset = 0;
        String question = "question";
        RecruitQuestion savedRecruitQuestion = recruitQuestionRepository.save(new RecruitQuestion(crew, question, offset));
        recruitQuestionRepository.delete(savedRecruitQuestion);
        //then
        Optional<RecruitQuestion> findRecruitQuestionOpt = recruitQuestionRepository.findById(savedRecruitQuestion.getId());
        Assertions.assertThat(findRecruitQuestionOpt).isEmpty();
    }



    @DisplayName("특정 crewId 를 가진 RecruitQuestion 출력 테스트")
    @Test
    void findAllByCrewIdTest() throws Exception {
        //given
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .location("location")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );
        //when
        String question = "question";
        for (int i = 10; i >= 0 ; i--) {
            recruitQuestionRepository.save(new RecruitQuestion(crew, question, i));
        }  // offset save [10, 0]
        List<RecruitQuestion> findRecruitQuestionList = recruitQuestionRepository.findAllByCrewId(crew.getId());
        //then
        for (int i = 0; i <= 10 ; i++) {
            Assertions.assertThat(findRecruitQuestionList.get(i).getQuestionOffset()).isEqualTo(i);
        } // offset [0, 10] 정렬 확인
    }

}