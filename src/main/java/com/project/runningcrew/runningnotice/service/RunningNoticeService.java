package com.project.runningcrew.runningnotice.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.exception.badinput.RunningDateTimeBeforeException;
import com.project.runningcrew.exception.badinput.YearMonthFormatException;
import com.project.runningcrew.runningmember.entity.RunningMember;
import com.project.runningcrew.resourceimage.entity.RunningNoticeImage;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.runningnotice.dto.NoticeWithUserDto;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.entity.RunningStatus;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.AuthorizationException;
import com.project.runningcrew.exception.notFound.MemberNotFoundException;
import com.project.runningcrew.exception.notFound.RunningNoticeNotFoundException;
import com.project.runningcrew.fcm.FirebaseMessagingService;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.runningmember.repository.RunningMemberRepository;
import com.project.runningcrew.runningnotice.repository.RunningNoticeRepository;
import com.project.runningcrew.comment.repository.RunningNoticeCommentRepository;
import com.project.runningcrew.resourceimage.repository.RunningNoticeImageRepository;
import com.project.runningcrew.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningNoticeService {

    private final RunningNoticeRepository runningNoticeRepository;
    private final ImageService imageService;
    private final RunningNoticeImageRepository runningNoticeImageRepository;
    private final RunningMemberRepository runningMemberRepository;
    private final MemberRepository memberRepository;
    private final RunningNoticeCommentRepository runningNoticeCommentRepository;
    private final FirebaseMessagingService firebaseMessagingService;
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
     * 입력받은 MultipartFile 들과 noticeType 이 REGULAR 인 RunningNotice 를 저장하고, 부여된 id 를 반환.
     * 생성하는 멤버가 매니저 이상이 아닐 때 throw AuthorizationException
     *
     * @param runningNotice  저장할 RunningNotice
     * @param multipartFiles 저장할 모든 MultipartFile
     * @return RunningNotice 에 부여된 id
     * @throws AuthorizationException 생성하는 멤버가 매니저 이상이 아닐 때
     */
    @Transactional
    public Long saveRegularRunningNotice(RunningNotice runningNotice, List<MultipartFile> multipartFiles) {
        MemberRole role = runningNotice.getMember().getRole();
        if (!role.isManager()) {
            throw new AuthorizationException();
        }

        RunningNotice savedRunningNotice = runningNoticeRepository.save(runningNotice);
        firebaseMessagingService.sendRegularRunningNoticeMessages(savedRunningNotice.getMember().getCrew(), savedRunningNotice);
        for (MultipartFile multipartFile : multipartFiles) {
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            runningNoticeImageRepository.save(new RunningNoticeImage(imageUrl, savedRunningNotice));
        }

        RunningMember runningMember = new RunningMember(runningNotice, runningNotice.getMember());
        runningMemberRepository.save(runningMember);
        return savedRunningNotice.getId();
    }

    /**
     * 입력받은 MultipartFile 들과 noticeType 이 INSTANT 인 RunningNotice 를 저장하고, 부여된 id 를 반환.
     *
     * @param runningNotice  저장할 RunningNotice
     * @param multipartFiles 저장할 모든 MultipartFile
     * @return RunningNotice 에 부여된 id
     */
    @Transactional
    public Long saveInstantRunningNotice(RunningNotice runningNotice, List<MultipartFile> multipartFiles) {
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
        if (!Objects.equals(originRunningNotice.getPreRunningRecord(), newRunningNotice.getPreRunningRecord())) {
            originRunningNotice.updatePreRunningRecord(newRunningNotice.getPreRunningRecord());
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
     * RunningNotice 의 status 를 Done 으로 변경한다. 런닝 시간 이전이라면 throw RunningDateTimeBeforeException
     *
     * @param runningNotice status 를 변경할 RunningNotice
     * @throws RunningDateTimeBeforeException 런닝 시간 이전에 종료를 변경하려 했을 때
     */
    @Transactional
    public void updateRunningStatusDone(RunningNotice runningNotice) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(runningNotice.getRunningDateTime())) {
            throw new RunningDateTimeBeforeException(now, runningNotice.getRunningDateTime());
        }
        runningNotice.updateStatus(RunningStatus.DONE);
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
        runningNoticeCommentRepository.deleteCommentAtRunningNotice(runningNotice);
        runningNoticeRepository.delete(runningNotice);
    }

    /**
     * 특정 크루의 정기 런닝공지들을 페이징하여 반환
     *
     * @param crew
     * @param pageable
     * @return 페이징 조건에 맞고, NoticeType 이 REGULAR 인 특정 크루의 모든 RunningNotice
     */
    public Slice<NoticeWithUserDto> findRegularsByCrew(Crew crew, Pageable pageable) {
        return runningNoticeRepository.findAllDtoByCrewAndNoticeType(NoticeType.REGULAR, crew, pageable);
    }

    /**
     * 특정 크루의 번개 런닝공지들을 페이징하여 반환
     *
     * @param crew
     * @param pageable
     * @return 페이징 조건에 맞고, NoticeType 이 INSTANT 인 특정 크루의 모든 RunningNotice
     */
    public Slice<NoticeWithUserDto> findInstantsByCrew(Crew crew, Pageable pageable) {
        return runningNoticeRepository.findAllDtoByCrewAndNoticeType(NoticeType.INSTANT, crew, pageable);
    }

    /**
     * 제목이나 내용에 keyword 가 포함된 특정 크루의 런닝 공지들을 페이징하여 반환
     *
     * @param crew
     * @param keyword  검색어
     * @param pageable
     * @return 제목이나 내용에 keyword 가 포함된 특정 크루의 런닝 공지
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
     * 입력받은 Crew 에서 특정 날에 런닝이 예정된 noticeType 이 REGULAR 인 모든 RunningNotice 를 반환
     *
     * @param crew
     * @param localDate 런닝 예정 일자
     * @return 입력받은 Crew 에서 localDate 에 런닝이 예정된 모든 REGULAR RunningNotice
     */
    public List<RunningNotice> findRegularsByCrewAndRunningDate(Crew crew, LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        LocalDateTime nextDateTime = dateTime.plusDays(1);
        return runningNoticeRepository.findAllByCrewAndRunningDateAndNoticeType(
                dateTime, nextDateTime, crew, NoticeType.REGULAR);
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

    /**
     * 입력받은 crew 에서 특정 년도 특정 월에 시행한 모든 REGULAR RunningRecord 들을 반환. 년, 월의 정보가 잘못되었다면
     * throw YearMonthFormatException
     *
     * @param crew  특정 크루
     * @param year  찾는 년도
     * @param month 찾는 월
     * @return 특정 crew 에서 특정 년도 특정 월에 시행한 모든 REGULAR RunningRecord
     * @throws YearMonthFormatException 잘못된 형식의 년, 월을 입력받았을 때
     */
    public List<RunningNotice> findRegularsByCrewAndMonth(Crew crew, int year, int month) {
        try {
            LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime end = start.plusMonths(1);

            return runningNoticeRepository
                    .findAllByCrewAndRunningDateAndNoticeType(start, end, crew, NoticeType.REGULAR);
        } catch (DateTimeException e) {
            throw new YearMonthFormatException(year, month);
        }
    }

    /**
     * 특정 멤버가 참여한 런닝공지들을 페이징하여 반환한다.
     *
     * @param member
     * @param pageable
     * @return 특정 멤버가 참여한 페이징된 런닝공지들
     */
    public Slice<RunningNotice> findRunningNoticesByApplyMember(Member member, Pageable pageable) {
        return runningNoticeRepository.findRunningNoticesByApplyMember(member, pageable);
    }


}
