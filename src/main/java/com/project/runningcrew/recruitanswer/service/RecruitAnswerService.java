package com.project.runningcrew.recruitanswer.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.recruitanswer.repository.RecruitAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitAnswerService {

    private final RecruitAnswerRepository recruitAnswerRepository;

    /**
     * recruitAnswer 의 리스트를 받아 모두 저장한다.
     * @param answers 저장할 답변 리스트
     * @return 저장된 recruitAnswer 의 id 값 리스트
     */
    public List<Long> saveAllRecruitAnswer(List<RecruitAnswer> answers) {
        List<Long> idList = new ArrayList<>();
        for (RecruitAnswer answer : answers) {
            idList.add(recruitAnswerRepository.save(answer).getId());
        }
        return idList;
    }

    /**
     * 특정 user 가 특정 crew 에 작성한 RecruitAnswer 를 모두 삭제한다.
     * @param user recruitAnswer 의 user
     * @param crew recruitAnswer 의 crew
     */
    public void deleteAllRecruitAnswer(User user, Crew crew) {
        recruitAnswerRepository.deleteByUserAndCrew(user, crew);
    }

    /**
     * 특정 user 가 작성한 recruitAnswer 를 offset 순서대로 정렬해서 반환한다.
     * @return offset 순서대로 정렬된 recruitAnswer List
     */
    public List<RecruitAnswer> findAllByUserAndCrew(User user, Crew crew) {
        return recruitAnswerRepository.findAllByUserAndCrew(user, crew);
    }

    /**
     * 특정 crew 에 recruitAnswer 를 작성한 user 목록을 반환한다.
     * @return 특정 crew 에 recruitAnswer 를 작성한 user 목록
     */
    public List<User> findUserByCrew(Crew crew)  {
        return recruitAnswerRepository.findUserByCrew(crew);
    }




}
