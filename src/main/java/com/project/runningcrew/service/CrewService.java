package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.CrewNotFoundException;
import com.project.runningcrew.repository.CrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;

    public Crew findById(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(CrewNotFoundException::new);
    }

    @Transactional
    public Long saveCrew(Crew crew) {
        //TODO MultipartFile flow 추가
        return crewRepository.save(crew).getId();
    }

    @Transactional
    public void updateCrew(Crew originCrew, Crew newCrew) {
        //TODO MultipartFile flow 추가
        if (!originCrew.getName().equals(newCrew.getName())) {
            originCrew.updateName(newCrew.getName());
        }
        if (!originCrew.getIntroduction().equals(newCrew.getIntroduction())) {
            originCrew.updateIntroduction(newCrew.getIntroduction());
        }
        if (!originCrew.getCrewImgUrl().equals(newCrew.getCrewImgUrl())) {
            originCrew.updateCrewImgUrl(newCrew.getCrewImgUrl());
        }
        if (!originCrew.getDongArea().equals(newCrew.getDongArea())) {
            originCrew.updateDongArea(newCrew.getDongArea());
        }
    }

    @Transactional
    public void deleteCrew(Crew crew) {
        //TODO MultipartFile flow 추가
        crewRepository.delete(crew);
    }

    public Slice<Crew> findByKeyword(Pageable pageable, String keyword) {
        return crewRepository.findByNameOrIntroductionOrArea(pageable, keyword);
    }

    public List<Crew> findRandomByDongAreaId(Long dongAreaId, int maxSize) {
        return crewRepository.findRandomByDongAreaId(dongAreaId, maxSize);
    }

    public List<Crew> findAllByUser(User user) {
        return crewRepository.findAllByUser(user);
    }


}
