package com.project.runningcrew.crew.service;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.comment.repository.CommentRepository;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.recruitanswer.repository.RecruitAnswerRepository;
import com.project.runningcrew.recruitquestion.repository.RecruitQuestionRepository;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import com.project.runningcrew.resourceimage.repository.RunningNoticeImageRepository;
import com.project.runningcrew.runningmember.repository.RunningMemberRepository;
import com.project.runningcrew.runningnotice.repository.RunningNoticeRepository;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.duplicate.CrewNameDuplicateException;
import com.project.runningcrew.exception.notFound.CrewNotFoundException;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.image.ImageService;
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
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final RunningMemberRepository runningMemberRepository;
    private final RunningNoticeRepository runningNoticeRepository;
    private final BoardImageRepository boardImageRepository;
    private final RunningNoticeImageRepository runningNoticeImageRepository;
    private final MemberRepository memberRepository;
    private final RecruitAnswerRepository recruitAnswerRepository;
    private final RecruitQuestionRepository recruitQuestionRepository;
    private final ImageService imageService;
    private final String imageDirName = "crew";

    /**
     * 입력받은 crewId 를 가진 Crew 를 찾아 반환한다. 없다면 CrewNotFoundException 을 throw 한다.
     *
     * @param crewId 찾는 Crew 의 id
     * @return 입력받은 crewId 를 가진 Crew
     * @throws CrewNotFoundException crewId 를 가진 Crew 가 없을 때
     */
    public Crew findById(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(CrewNotFoundException::new);
    }

    /**
     * 입력받은 Crew 이미지와 Crew 를 저장하고 Crew 의 리더를 생성한 후, Crew 에 부여된 id 를 반환한다.
     *
     * @param crew          저장할 Crew
     * @param multipartFile Crew 의 이미지
     * @return Crew 에 부여된 id
     * @throws CrewNameDuplicateException 입력받은 Crew 의 이름을 가진 크루가 이미 존재할 때
     */
    @Transactional
    public Long saveCrew(User user, Crew crew, MultipartFile multipartFile) {
        if (crewRepository.existsByName(crew.getName())) {
            throw new CrewNameDuplicateException(crew.getName());
        }
        String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
        crew.updateCrewImgUrl(imageUrl);
        Crew savedCrew = crewRepository.save(crew);
        memberRepository.save(new Member(user, crew, MemberRole.ROLE_LEADER));
        return savedCrew.getId();
    }

    /**
     * 변경된 Crew 정보를 확인하고, 변경된 Crew 정보를 수정한다.
     *
     * @param originCrew    기존 Crew
     * @param newCrew       변경된 Crew
     * @param multipartFile 변경된 Crew 이미지
     * @throws CrewNameDuplicateException 변경된 Crew 의 이름을 가진 크루가 이미 존재할 때
     */
    @Transactional
    public void updateCrew(Crew originCrew, Crew newCrew, MultipartFile multipartFile) {
        if (!originCrew.getName().equals(newCrew.getName())) {
            if (crewRepository.existsByName(newCrew.getName())) {
                throw new CrewNameDuplicateException(newCrew.getName());
            }
            originCrew.updateName(newCrew.getName());
        }
        if (!originCrew.getIntroduction().equals(newCrew.getIntroduction())) {
            originCrew.updateIntroduction(newCrew.getIntroduction());
        }
        if (!originCrew.getDongArea().equals(newCrew.getDongArea())) {
            originCrew.updateDongArea(newCrew.getDongArea());
        }
        if (multipartFile!= null && !multipartFile.isEmpty()) {
            imageService.deleteImage(originCrew.getCrewImgUrl());
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            originCrew.updateCrewImgUrl(imageUrl);
        }
    }

    /**
     * 입력받은 Crew 에 속한 Member 들을 삭제하고 Crew 와 Crew 이미지를 삭제한다.
     *
     * @param crew 삭제할 Crew
     */
    @Transactional
    public void deleteCrew(Crew crew) {
        recruitAnswerRepository.deleteAllByCrew(crew);
        recruitQuestionRepository.deleteAllByCrew(crew);
        runningNoticeImageRepository.deleteAllByCrew(crew);
        //TODO runningMember 삭제
        //TODO runningNotice 삭제
        boardImageRepository.deleteAllByCrew(crew);
        commentRepository.deleteAllByCrew(crew);
        boardRepository.deleteAllByCrew(crew);
        memberRepository.deleteAllByCrew(crew);
        crewRepository.delete(crew);
        imageService.deleteImage(crew.getCrewImgUrl());
    }

    /**
     * name 또는 introduction 또는 area 의 이름에 keyword 가 포함된 모든 crew 의 개수 반환
     *
     * @param keyword 검색어
     * @return name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 개수
     */
    public Long countAllByKeyword(String keyword) {
        return crewRepository.countAllByNameOrIntroductionOrArea(keyword);
    }

    /**
     * name 또는 introduction 또는 area 의 이름에 keyword 가 포함된 crew 들을 페이징하여 반환
     *
     * @param pageable
     * @param keyword  검색어
     * @return 페이징 조건에 맞는 name 또는 introduction 또는 area 에 keyword 가 포함된 crew 들이 담긴 Slice
     */
    public Slice<Crew> findByKeyword(Pageable pageable, String keyword) {
        return crewRepository.findByNameOrIntroductionOrArea(pageable, keyword);
    }

    /**
     * 특정 dongArea 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     *
     * @param dongArea crew 들이 속한 DongArea
     * @param maxSize  반환할 crew 의 최대 개수
     * @return dongArea 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    public List<Crew> findRandomByDongArea(DongArea dongArea, int maxSize) {
        return crewRepository.findRandomByDongAreaId(dongArea.getId(), maxSize);
    }

    /**
     * 특정 GuArea 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     *
     * @param guArea  crew 들이 속한 GuArea
     * @param maxSize 반환할 crew 의 최대 개수
     * @return guArea 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    public List<Crew> findRandomByGuArea(GuArea guArea, int maxSize) {
        return crewRepository.findRandomByGuAreaId(guArea.getId(), maxSize);
    }

    /**
     * 입력받은 User 가 속한 모든 Crew 를 반환한다.
     *
     * @param user
     * @return user 가 속한 모든 Crew
     */
    public List<Crew> findAllByUser(User user) {
        return crewRepository.findAllByUser(user);
    }

    /**
     * 입력받은 이름을 가지는 크루가 있는지 확인한다. 있다면 true, 없다면 false 를 반환
     *
     * @param name 크루의 이름
     * @return 입력받은 이름을 가지는 크루가 있다면 true, 없다면 false
     */
    public boolean existsByName(String name) {
        return crewRepository.existsByName(name);
    }

}
