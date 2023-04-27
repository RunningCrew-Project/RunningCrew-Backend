package com.project.runningcrew.recruitquestion.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.exception.notFound.RecruitQuestionNotFoundException;
import com.project.runningcrew.recruitquestion.entity.RecruitQuestion;
import com.project.runningcrew.recruitquestion.repository.RecruitQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitQuestionService {

    private final RecruitQuestionRepository recruitQuestionRepository;


    /**
     * 입력받은 id 로 찾아낸 recruitQuestion 을 반환한다.
     * @param id 입력받은 id
     * @return recruitQuestion
     */
    public RecruitQuestion findById(Long id) {
        return recruitQuestionRepository.findById(id).orElseThrow(RecruitQuestionNotFoundException::new);
    }

    /**
     * 입력받은 recruitQuestion 하나를 저장하고 id 값을 반환한다.
     * @param question 저장할 recruitQuestion
     * @return 저장한 recruitQuestion 의 id 값
     */
    @Transactional
    public Long saveOneQuestion(RecruitQuestion question) {
        return recruitQuestionRepository.save(question).getId();
    }

    /**
     * 입력받은 recruitQuestion 하나를 삭제한다.
     * @param question 삭제할 recruitQuestion
     */
    @Transactional
    public void deleteOneQuestion(RecruitQuestion question) {
        recruitQuestionRepository.delete(question);
    }

    /**
     * 특정 crew 의 recruitQuestion 을 offset 순으로 정렬하여 출력한다.
     * @param crew 크루
     * @return 특정 crew 의 정렬된 recruitQuestion 리스트
     */
    public List<RecruitQuestion> findAllByCrew(Crew crew) {
        return recruitQuestionRepository.findAllByCrew(crew);
    }


    /**
     * 특정 crew 의 recruitQuestions 를 입력받은 것들로 변경한다.
     * @param crew 수정할 crew
     * @param recruitQuestions 변경할 recruitQuestion
     */
    @Transactional
    public void setRecruitQuestions(Crew crew, List<RecruitQuestion> recruitQuestions) {
        recruitQuestionRepository.deleteAllByCrew(crew);
        recruitQuestionRepository.saveAll(recruitQuestions);
    }

}
