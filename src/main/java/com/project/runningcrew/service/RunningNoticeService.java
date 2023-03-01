package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.notFound.RunningNoticeNotFoundException;
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
    private final String imageDirName = "runningNotice";

    public RunningNotice findById(Long runningNoticeId) {
        return runningNoticeRepository.findById(runningNoticeId)
                .orElseThrow(RunningNoticeNotFoundException::new);
    }

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

    @Transactional
    public void updateRunningStatus(RunningNotice runningNotice) {
        runningNotice.updateStatus(RunningStatus.DONE);
    }

    @Transactional
    public void deleteRunningNotice(RunningNotice runningNotice) {
        runningNoticeImageRepository.deleteAllByRunningNotice(runningNotice);
        //TODO 댓글 모두 삭제
        runningNoticeRepository.delete(runningNotice);
    }

    public Slice<RunningNotice> findRegularsByCrew(Crew crew, Pageable pageable) {
        return runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.REGULAR, crew, pageable);
    }

    public Slice<RunningNotice> findInstantsByCrew(Crew crew, Pageable pageable) {
        return runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.INSTANT, crew, pageable);
    }

    public Slice<RunningNotice> findByCrewAndKeyword(Crew crew, String keyword, Pageable pageable) {
        return runningNoticeRepository.findSliceAllByCrewAndKeyWord(keyword, crew, pageable);
    }

    public List<RunningNotice> findReadyRunningNoticesByUser(User user) {
        return runningNoticeRepository.findAllByUserAndStatus(user, RunningStatus.READY);
    }

    public List<RunningNotice> findAllByCrewAndRunningDate(Crew crew, LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        LocalDateTime nextDateTime = dateTime.plusDays(1);
        return runningNoticeRepository.findAllByCrewAndRunningDate(dateTime, nextDateTime, crew);
    }

    public Slice<RunningNotice> findByMember(Member member, Pageable pageable) {
        return runningNoticeRepository.findAllByMember(member, pageable);
    }

}
