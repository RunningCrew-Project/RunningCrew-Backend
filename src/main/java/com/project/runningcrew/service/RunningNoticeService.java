package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.notFound.MemberNotFoundException;
import com.project.runningcrew.exception.notFound.RunningNoticeNotFoundException;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.RunningMemberRepository;
import com.project.runningcrew.repository.RunningNoticeRepository;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import com.project.runningcrew.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningNoticeService {

    private final RunningNoticeRepository runningNoticeRepository;
    private final ImageService imageService;
    private final RunningNoticeImageRepository runningNoticeImageRepository;
    private final RunningMemberRepository runningMemberRepository;
    private final MemberRepository memberRepository;
    private final String imageDirName = "runningNotice";

    /**
     * 입력받은 id 를 가진 RunningNotice 를 찾아 반환한다. 없다면 RunningNoticeNotFoundException 를 throw
     *
     * @param runningNoticeId 찾는 RunningNotice 의 id
     * @return 입력받은 id 를 가지는 RunningNotice
     * @throws RunningNoticeNotFoundException 입력받은 id 를 가진 RunningNotice 가 없을 때
     */
    public RunningNotice findById(Long runningNoticeId) {
        return runningNoticeRepository.findById(runningNoticeId)
                .orElseThrow(RunningNoticeNotFoundException::new);
    }

    /**
     * 입력받은 MultipartFile 들과 RunningNotice 를 저장하고, 부여된 id 를 반환
     *
     * @param runningNotice  저장할 RunningNotice
     * @param multipartFiles 저장할 모든 MultipartFile
     * @return RunningNotice 에 부여된 id
     */
    @Transactional
    public Long saveRunningNotice(RunningNotice runningNotice, List<MultipartFile> multipartFiles) {
        RunningNotice savedRunningNotice = runningNoticeRepository.save(runningNotice);
        for (MultipartFile multipartFile : multipartFiles) {
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            runningNoticeImageRepository.save(new RunningNoticeImage(imageUrl, savedRunningNotice));
        }

        RunningMember runningMember = new RunningMember(runningNotice, runningNotice.getMember());
        runningMemberRepository.save(runningMember);
        return savedRunningNotice.getId();
    }

    /**
     * 변경된 RunningNotice 정보를 확인하고, 변경된 정보로 수정
     *
     * @param originRunningNotice 기존 RunningNotice
     * @param newRunningNotice    변경된 RunningNotice
     * @param addFiles            추가할 MultipartFile 들
     * @param deleteFiles         삭제할 RunningNoticeImage 의 id 를 가진 리스트
     */
    @Transactional
    public void updateRunningNotice(RunningNotice originRunningNotice, RunningNotice newRunningNotice,
                                    List<MultipartFile> addFiles, List<RunningNoticeImage> deleteFiles) {
        if (!originRunningNotice.getTitle().equals(newRunningNotice.getTitle())) {
            originRunningNotice.updateTitle(newRunningNotice.getTitle());
        }
        if (!originRunningNotice.getDetail().equals(newRunningNotice.getDetail())) {
            originRunningNotice.updateDetail(newRunningNotice.getDetail());
        }
        if (!originRunningNotice.getRunningDateTime().equals(newRunningNotice.getRunningDateTime())) {
            originRunningNotice.updateRunningTime(newRunningNotice.getRunningDateTime());
        }

        for (MultipartFile multipartFile : addFiles) {
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            runningNoticeImageRepository.save(new RunningNoticeImage(imageUrl, originRunningNotice));
        }

        for (RunningNoticeImage deleteFile : deleteFiles) {
            imageService.deleteImage(deleteFile.getFileName());
            runningNoticeImageRepository.delete(deleteFile);
        }
    }

    /**
     * RunningNotice 의 status 를 Done 으로 변경하고, RunningNotice 에 포함된 모든 RunningMember 들을 삭제
     *
     * @param runningNotice status 를 변경할 RunningNotice
     */
    @Transactional
    public void updateRunningStatusDone(RunningNotice runningNotice) {
        runningNotice.updateStatus(RunningStatus.DONE);
        runningMemberRepository.deleteAllByRunningNotice(runningNotice);
    }

    /**
     * 입력받은 RunningNotice 를 삭제한다. 연관된 RunningNoticeImage, RunningMember, Comment 도 함께 삭제한다.
     *
     * @param runningNotice 삭제할 RunningNotice
     */
    @Transactional
    public void deleteRunningNotice(RunningNotice runningNotice) {
        List<RunningNoticeImage> images = runningNoticeImageRepository.findAllByRunningNotice(runningNotice);
        for (RunningNoticeImage image : images) {
            imageService.deleteImage(image.getFileName());
        }
        runningNoticeImageRepository.deleteAllByRunningNotice(runningNotice);
        runningMemberRepository.deleteAllByRunningNotice(runningNotice);
        //TODO 댓글 모두 삭제
        runningNoticeRepository.delete(runningNotice);
    }

    /**
     * 특정 크루의 정기 런닝공지들을 페이징하여 반환
     *
     * @param crew
     * @param pageable
     * @return 페이징 조건에 맞고, NoticeType 이 REGULAR 인 특정 크루의 모든 RunningNotice
     */
    public Slice<RunningNotice> findRegularsByCrew(Crew crew, Pageable pageable) {
        return runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.REGULAR, crew, pageable);
    }

    /**
     * 특정 크루의 번개 런닝공지들을 페이징하여 반환
     *
     * @param crew
     * @param pageable
     * @return 페이징 조건에 맞고, NoticeType 이 INSTANT 인 특정 크루의 모든 RunningNotice
     */
    public Slice<RunningNotice> findInstantsByCrew(Crew crew, Pageable pageable) {
        return runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.INSTANT, crew, pageable);
    }

    /**
     * 제목이나 내용에 keyword 가 포함된 특정 크루의 런닝 공지들을 페이징하여 반환
     *
     * @param crew
     * @param keyword  검색어
     * @param pageable
     * @return
     */
    public Slice<RunningNotice> findByCrewAndKeyword(Crew crew, String keyword, Pageable pageable) {
        return runningNoticeRepository.findSliceAllByCrewAndKeyWord(keyword, crew, pageable);
    }

    /**
     * 특정 user 가 신청한 예정된 런닝 공지들을 반환
     *
     * @param user
     * @return 특정 user 가 신청했고, RunningStatus 가 READY 인 모든 RunningNotice
     */
    public List<RunningNotice> findReadyRunningNoticesByUser(User user) {
        return runningNoticeRepository.findAllByUserAndStatus(user, RunningStatus.READY);
    }

    /**
     * 입력받은 Crew 에서 특정 날에 런닝이 예정된 모든 RunningNotice 를 반환
     *
     * @param crew
     * @param localDate 런닝 예정 일자
     * @return 입력받은 Crew 에서 localDate 에 런닝이 예정된 모든 RunningNotice
     */
    public List<RunningNotice> findAllByCrewAndRunningDate(Crew crew, LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        LocalDateTime nextDateTime = dateTime.plusDays(1);
        return runningNoticeRepository.findAllByCrewAndRunningDate(dateTime, nextDateTime, crew);
    }

    /**
     * 특정 멤버가 작성한 모든 런닝 공지를 페이징하여 반환
     *
     * @param member
     * @param pageable
     * @return 특정 member 가 작성한 페이징된 모든 RunningNotice
     */
    public Slice<RunningNotice> findByMember(Member member, Pageable pageable) {
        return runningNoticeRepository.findAllByMember(member, pageable);
    }

    /**
     * 특정 user 가 특정 RunningNotice 에 대해 크루런닝을 시작할 수 있는지 확인하여, 가능하면 true, 불가능하면 false 를
     * 반환한다.
     *
     * @param user
     * @param runningNotice 확인할 RunningNotice
     * @return 크루런닝을 시작할 수 있으면 true, 시작할 수 없으면 false
     */
    public boolean checkRunningNotice(User user, RunningNotice runningNotice) {
        Member member = memberRepository.findByUserAndCrew(user, runningNotice.getMember().getCrew())
                .orElseThrow(MemberNotFoundException::new);
        if (!runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice)) {
            return false;
        }
        if (runningNotice.getStatus() != RunningStatus.READY) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(runningNotice.getRunningDateTime().minusMinutes(15))) {
            return false;
        }

        return true;
    }

}
