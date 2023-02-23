package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.duplicate.CrewNameDuplicateException;
import com.project.runningcrew.exception.notFound.CrewNotFoundException;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final String imageDirName = "crew";

    /**
     * 입력받은 crewId 를 가진 Crew 를 찾아 반환한다. 없다면 CrewNotFoundException 을 throw 한다.
     * @param crewId 찾는 Crew 의 id
     * @return 입력받은 crewId 를 가진 Crew
     * @throws CrewNotFoundException crewId 를 가진 Crew 가 없을 때
     */
    public Crew findById(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(CrewNotFoundException::new);
    }

    /**
     * 입력받은 Crew 이미지와 Crew 를 저장하고 Crew 의 리더를 생성한 후, Crew 에 부여된 id 를 반환한다.
     * @param crew 저장할 Crew
     * @param multipartFile Crew 의 이미지
     * @return Crew 에 부여된 id
     * @throws CrewNameDuplicateException 입력받은 Crew 의 이름을 가진 크루가 이미 존재할 때
     */
    @Transactional
    public Long saveCrew(User user, Crew crew, MultipartFile multipartFile) {
        if (crewRepository.existsByName(crew.getName())) {
            throw new CrewNameDuplicateException();
        }
        String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
        crew.updateCrewImgUrl(imageUrl);
        Crew savedCrew = crewRepository.save(crew);
        memberRepository.save(new Member(user, crew, MemberRole.ROLE_LEADER));
        return savedCrew.getId();
    }

    /**
     * 변경된 Crew 정보를 확인하고, 변경된 Crew 정보를 수정한다.
     * @param originCrew 기존 Crew
     * @param newCrew 변경된 Crew
     * @param multipartFile 변경된 Crew 이미지
     * @throws CrewNameDuplicateException 변경된 Crew 의 이름을 가진 크루가 이미 존재할 때
     */
    @Transactional
    public void updateCrew(Crew originCrew, Crew newCrew, MultipartFile multipartFile) {
        if (!originCrew.getName().equals(newCrew.getName())) {
            if (crewRepository.existsByName(newCrew.getName())) {
                throw new CrewNameDuplicateException();
            }
            originCrew.updateName(newCrew.getName());
        }
        if (!originCrew.getIntroduction().equals(newCrew.getIntroduction())) {
            originCrew.updateIntroduction(newCrew.getIntroduction());
        }
        if (!originCrew.getDongArea().equals(newCrew.getDongArea())) {
            originCrew.updateDongArea(newCrew.getDongArea());
        }
        if (!multipartFile.isEmpty()) {
            imageService.deleteImage(originCrew.getCrewImgUrl());
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            originCrew.updateCrewImgUrl(imageUrl);
        }
    }

    /**
     * 입력받은 Crew 에 속한 Member 들을 삭제하고 Crew 와 Crew 이미지를 삭제한다.
     * @param crew 삭제할 Crew
     */
    @Transactional
    public void deleteCrew(Crew crew) {
        List<Member> members = memberRepository.findAllByCrew(crew);
        for (Member member : members) {
            memberRepository.delete(member);
        }
        crewRepository.delete(crew);
        imageService.deleteImage(crew.getCrewImgUrl());
    }

    /**
     * name 또는 introduction 또는 area 의 이름에 keyword 가 포함된 모든 crew 의 개수 반환
     * @param keyword 검색어
     * @return name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 개수
     */
    public Long countAllByKeyword(String keyword) {
        return crewRepository.countAllByNameOrIntroductionOrArea(keyword);
    }

    /**
     * name 또는 introduction 또는 area 의 이름에 keyword 가 포함된 crew 들을 페이징하여 반환
     * @param pageable
     * @param keyword 검색어
     * @return 페이징 조건에 맞는 name 또는 introduction 또는 area 에 keyword 가 포함된 crew 들이 담긴 Slice
     */
    public Slice<Crew> findByKeyword(Pageable pageable, String keyword) {
        return crewRepository.findByNameOrIntroductionOrArea(pageable, keyword);
    }

    /**
     * 특정 dongArea 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     * @param dongAreaId dongArea 의 id
     * @param maxSize 반환할 crew 의 최대 개수
     * @return dongArea 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    public List<Crew> findRandomByDongAreaId(Long dongAreaId, int maxSize) {
        return crewRepository.findRandomByDongAreaId(dongAreaId, maxSize);
    }

    /**
     * 입력받은 User 가 속한 모든 Crew 를 반환한다.
     * @param user
     * @return user 가 속한 모든 Crew
     */
    public List<Crew> findAllByUser(User user) {
        return crewRepository.findAllByUser(user);
    }

}
