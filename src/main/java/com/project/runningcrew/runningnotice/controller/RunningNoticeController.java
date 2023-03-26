package com.project.runningcrew.runningnotice.controller;

import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.ApplyResponse;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.common.dto.YearMonthDto;
import com.project.runningcrew.runningnotice.dto.ReadyResponse;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.resourceimage.entity.RunningNoticeImage;
import com.project.runningcrew.resourceimage.service.RunningNoticeImageService;
import com.project.runningcrew.runningmember.service.RunningMemberService;
import com.project.runningcrew.runningnotice.dto.*;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.entity.RunningStatus;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.runningrecord.service.RunningRecordService;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "RunningNotice", description = "런닝 공지에 관한 api")
@RestController
@RequiredArgsConstructor
public class RunningNoticeController {

    private final RunningNoticeService runningNoticeService;
    private final RunningMemberService runningMemberService;
    private final CrewService crewService;
    private final RunningRecordService runningRecordService;
    private final RunningNoticeImageService runningNoticeImageService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;
    @Value("${domain.name}")
    private String host;
    private int pagingSize = 15;


    @Operation(summary = "런닝공지 가져오기", description = "runningNoticeId 에 해당하는 런닝공지를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetRunningNoticeResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-notices/{runningNoticeId}")
    public ResponseEntity<GetRunningNoticeResponse> getRunningNotice(
            @PathVariable("runningNoticeId") Long runningNoticeId,
            @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        memberAuthorizationChecker.checkMember(user, runningNotice.getMember().getCrew());

        Long runningMemberCount = runningMemberService.countAllByRunningNotice(runningNotice);

        return ResponseEntity.ok(new GetRunningNoticeResponse(runningNotice, runningMemberCount));
    }


    @Operation(summary = "정기런닝 공지 생성하기", description = "정기런닝 공지를 생성한다. 매니저 이상만 생성할 수 있다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/running-notices/regular", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createRegularRunningNotice(@PathVariable("crewId") Long crewId,
                                                           @ModelAttribute @Valid CreateRunningNoticeRequest createRunningNoticeRequest,
                                                           @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        Member member = memberAuthorizationChecker.checkManager(user, crew);

        RunningRecord preRunningRecord = null;
        if (createRunningNoticeRequest.getPreRunningRecordId() != null) {
            preRunningRecord = runningRecordService.findById(createRunningNoticeRequest.getPreRunningRecordId());
        }
        RunningNotice runningNotice = RunningNotice.builder()
                .title(createRunningNoticeRequest.getTitle())
                .detail(createRunningNoticeRequest.getDetail())
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(createRunningNoticeRequest.getRunningDateTime())
                .runningPersonnel(createRunningNoticeRequest.getRunningPersonnel())
                .status(RunningStatus.READY)
                .preRunningRecord(preRunningRecord)
                .build();

        Long runningNoticeId = runningNoticeService.saveRegularRunningNotice(
                runningNotice, createRunningNoticeRequest.getFiles());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/running-notices/{id}")
                .build(runningNoticeId);
        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "번개런닝 공지 생성하기", description = "번개런닝 공지를 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/running-notices/instant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createInstantRunningNotice(@PathVariable("crewId") Long crewId,
                                                           @ModelAttribute @Valid CreateRunningNoticeRequest createRunningNoticeRequest,
                                                           @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        Member member = memberAuthorizationChecker.checkMember(user, crew);

        RunningRecord preRunningRecord = null;
        if (createRunningNoticeRequest.getPreRunningRecordId() != null) {
            preRunningRecord = runningRecordService.findById(createRunningNoticeRequest.getPreRunningRecordId());
        }
        RunningNotice runningNotice = RunningNotice.builder()
                .title(createRunningNoticeRequest.getTitle())
                .detail(createRunningNoticeRequest.getDetail())
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(createRunningNoticeRequest.getRunningDateTime())
                .runningPersonnel(createRunningNoticeRequest.getRunningPersonnel())
                .status(RunningStatus.READY)
                .preRunningRecord(preRunningRecord)
                .build();

        Long runningNoticeId = runningNoticeService.saveRegularRunningNotice(
                runningNotice, createRunningNoticeRequest.getFiles());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/running-notices/{id}")
                .build(runningNoticeId);
        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "런닝공지 수정하기", description = "런닝공지를 수정한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/api/running-notices/{runningNoticeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateRunningNotice(@PathVariable("runningNoticeId") Long runningNoticeId,
                                                    @ModelAttribute @Valid UpdateRunningNoticeRequest updateRunningNoticeRequest,
                                                    @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice originRunningNotice = runningNoticeService.findById(runningNoticeId);
        Member writeMember = originRunningNotice.getMember();
        memberAuthorizationChecker.checkAuthOnlyUser(user, writeMember.getCrew(), writeMember.getId());

        RunningRecord newPreRunningRecord = null;
        if (updateRunningNoticeRequest.getPreRunningRecordId() != null) {
            newPreRunningRecord = runningRecordService.findById(updateRunningNoticeRequest.getPreRunningRecordId());
        }
        List<RunningNoticeImage> deleteImages = updateRunningNoticeRequest.getDeleteFiles().stream()
                .map(runningNoticeImageService::findById)
                .collect(Collectors.toList());

        RunningNotice newRunningNotice = RunningNotice.builder()
                .title(updateRunningNoticeRequest.getTitle())
                .detail(updateRunningNoticeRequest.getDetail())
                .runningDateTime(updateRunningNoticeRequest.getRunningDateTime())
                .preRunningRecord(newPreRunningRecord)
                .build();

        runningNoticeService.updateRunningNotice(originRunningNotice, newRunningNotice,
                updateRunningNoticeRequest.getAddFiles(), deleteImages);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "런닝 공지 삭제하기", description = "런닝 공지를 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/api/running-notices/{runningNoticeId}")
    public ResponseEntity<Void> deleteRunningNotice(@PathVariable("runningNoticeId") Long runningNoticeId,
                                                    @Parameter(hidden = true) @CurrentUser User user) {

        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        Member writeMember = runningNotice.getMember();
        memberAuthorizationChecker.checkAuthUserAndManger(user, writeMember.getCrew(), writeMember.getId());

        runningNoticeService.deleteRunningNotice(runningNotice);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "정기런닝 공지 가져오기", description = "정기런닝 공지를 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/running-notices/regular")
    public ResponseEntity<PagingResponse<PagingRunningNoticeDto>> findRegularRunningNotices(@PathVariable("crewId") Long crewId,
                                                                                            @RequestParam("page") @PositiveOrZero int page,
                                                                                            @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        PageRequest pageRequest = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<RunningNotice> regulars = runningNoticeService.findRegularsByCrew(crew, pageRequest);
        List<Long> runningNoticeIds = regulars.stream().map(RunningNotice::getId).collect(Collectors.toList());
        Map<Long, String> firstImages = runningNoticeImageService.findFirstImageUrls(runningNoticeIds);
        Map<Long, Long> commentCountMap = commentService.countAllByRunningNoticeIds(runningNoticeIds);

        List<PagingRunningNoticeDto> contents = regulars.stream()
                .map(r -> new PagingRunningNoticeDto(r, firstImages.get(r.getId()), commentCountMap.get(r.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(contents, regulars.getPageable(), regulars.hasNext())));
    }


    @Operation(summary = "번개런닝 공지 가져오기", description = "번개런닝 공지를 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/running-notices/instant")
    public ResponseEntity<PagingResponse<PagingRunningNoticeDto>> findInstantRunningNotices(@PathVariable("crewId") Long crewId,
                                                                                            @RequestParam("page") @PositiveOrZero int page,
                                                                                            @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        PageRequest pageRequest = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<RunningNotice> instants = runningNoticeService.findInstantsByCrew(crew, pageRequest);
        List<Long> runningNoticeIds = instants.stream().map(RunningNotice::getId).collect(Collectors.toList());
        Map<Long, String> firstImages = runningNoticeImageService.findFirstImageUrls(runningNoticeIds);
        Map<Long, Long> commentCountMap = commentService.countAllByRunningNoticeIds(runningNoticeIds);

        List<PagingRunningNoticeDto> contents = instants.stream()
                .map(r -> new PagingRunningNoticeDto(r, firstImages.get(r.getId()), commentCountMap.get(r.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(contents, instants.getPageable(), instants.hasNext())));
    }


    @Operation(summary = "검색어로 런닝공지 가져오기", description = "검색어에 해당하는 런닝공지를 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/running-notices")
    public ResponseEntity<PagingResponse<PagingRunningNoticeDto>> findRunningNoticesByKeyword(@PathVariable("crewId") Long crewId,
                                                                                              @RequestParam("keyword") @NotBlank String keyword,
                                                                                              @RequestParam("page") @PositiveOrZero int page,
                                                                                              @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        PageRequest pageRequest = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<RunningNotice> runningNotices = runningNoticeService.findByCrewAndKeyword(crew, keyword, pageRequest);
        List<Long> runningNoticeIds = runningNotices.stream().map(RunningNotice::getId).collect(Collectors.toList());
        Map<Long, String> firstImages = runningNoticeImageService.findFirstImageUrls(runningNoticeIds);
        Map<Long, Long> commentCountMap = commentService.countAllByRunningNoticeIds(runningNoticeIds);

        List<PagingRunningNoticeDto> contents = runningNotices.stream()
                .map(r -> new PagingRunningNoticeDto(r, firstImages.get(r.getId()), commentCountMap.get(r.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(contents, runningNotices.getPageable(), runningNotices.hasNext())));
    }


    @Operation(summary = "특정 날에 시작하는 크루 정기런닝 공지 가져오기", description = "crewId 에 속하는 크루에서 특정 날에 시작하는 정기런닝 공지를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunningNoticeListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/running-notices/regular/on-date")
    public ResponseEntity<RunningNoticeListResponse<RunningNoticeOnDateDto>> getRegularRunningNoticesByDate(
            @PathVariable("crewId") Long crewId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        List<RunningNotice> runningNotices = runningNoticeService.findRegularsByCrewAndRunningDate(crew, date);
        List<RunningNoticeOnDateDto> runningNoticeOnDateDtoList = runningNotices.stream()
                .map(RunningNoticeOnDateDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new RunningNoticeListResponse<>(runningNoticeOnDateDtoList));
    }


    @Operation(summary = "유저의 예정된 런닝공지 가져오기", description = "유저의 예정된 런닝공지를 모두 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunningNoticeListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-notices")
    public ResponseEntity<RunningNoticeListResponse<RunningNoticeInfoDto>> getReadyRunningNotices(
            @Parameter(hidden = true) @CurrentUser User user) {
        List<RunningNotice> runningNotices = runningNoticeService.findReadyRunningNoticesByUser(user);
        List<Long> runningNoticeIds = runningNotices.stream().map(RunningNotice::getId).collect(Collectors.toList());
        Map<Long, Long> memberCounts = runningMemberService.countRunningMembersByRunningNoticeIds(runningNoticeIds);

        List<RunningNoticeInfoDto> runningNoticeInfoDtoList = runningNotices.stream()
                .map(r -> new RunningNoticeInfoDto(r, memberCounts.get(r.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new RunningNoticeListResponse<>(runningNoticeInfoDtoList));
    }


    @Operation(summary = "런닝 종료하기", description = "런닝 공지의 상태를 종료로 변경한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/api/running-notices/{runningNoticeId}/status")
    public ResponseEntity<Void> finishRunning(@PathVariable("runningNoticeId") Long runningNoticeId,
                                              @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        Member writeMember = runningNotice.getMember();
        memberAuthorizationChecker.checkAuthOnlyUser(user, writeMember.getCrew(), writeMember.getId());

        runningNoticeService.updateRunningStatusDone(runningNotice);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "런닝 참가하기", description = "런닝에 참가한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/running-notices/{runningNoticeId}/members")
    public ResponseEntity<Void> applyRunning(@PathVariable("runningNoticeId") Long runningNoticeId,
                                             @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        Member member = memberAuthorizationChecker.checkMember(user, runningNotice.getMember().getCrew());

        runningMemberService.saveRunningMember(member, runningNotice);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "런닝 참가 취소하기", description = "런닝 참가를 취소한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/api/running-notices/{runningNoticeId}/members")
    public ResponseEntity<Void> cancelRunning(@PathVariable("runningNoticeId") Long runningNoticeId,
                                              @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        Member member = memberAuthorizationChecker.checkMember(user, runningNotice.getMember().getCrew());

        runningMemberService.deleteRunningMember(member, runningNotice);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "유저의 예정 런닝 체크하기", description = "유저의 예정된 런닝이 시작 가능한 런닝인지 체크한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadyResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-notices/{runningNoticeId}/check-ready")
    public ResponseEntity<ReadyResponse> checkRunningReady(
            @PathVariable("runningNoticeId") Long runningNoticeId,
            @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        boolean result = runningNoticeService.checkRunningNotice(user, runningNotice);

        return ResponseEntity.ok(new ReadyResponse(String.valueOf(result)));
    }


    @Operation(summary = "유저의 런닝 참여여부 체크하기", description = "유저의 런닝 참여여부를 체크한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplyResponse.class))), @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-notices/{runningNoticeId}/apply")
    public ResponseEntity<ApplyResponse> checkRunningApply(
            @PathVariable("runningNoticeId") Long runningNoticeId,
            @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        Member member = memberAuthorizationChecker.checkMember(user, runningNotice.getMember().getCrew());

        boolean result = runningMemberService.existsByMemberAndRunningNotice(member, runningNotice);

        return ResponseEntity.ok(new ApplyResponse(String.valueOf(result)));
    }


    @Operation(summary = "멤버가 참여한 런닝공지 가져오기", description = "멤버가 참여한 런닝공지를 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/members/{memberId}/running-notices")
    public ResponseEntity<PagingResponse<RunningNoticeInfoDto>> getApplyRunningNotices(
            @PathVariable("memberId") Long memberId,
            @RequestParam("page") @PositiveOrZero int page,
            @Parameter(hidden = true) @CurrentUser User user) {

        Member member = memberService.findById(memberId);
        memberAuthorizationChecker.checkMember(user, member.getCrew());

        PageRequest pageRequest = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<RunningNotice> runningNotices = runningNoticeService.findRunningNoticesByApplyMember(member, pageRequest);
        List<Long> runningNoticeIds = runningNotices.stream().map(RunningNotice::getId).collect(Collectors.toList());
        Map<Long, Long> memberCounts = runningMemberService.countRunningMembersByRunningNoticeIds(runningNoticeIds);

        List<RunningNoticeInfoDto> runningNoticeInfoDtoList = runningNotices.stream()
                .map(r -> new RunningNoticeInfoDto(r, memberCounts.get(r.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(runningNoticeInfoDtoList,
                        runningNotices.getPageable(),
                        runningNotices.hasNext())));
    }


    @Operation(summary = "특정 크루 정기러닝 월별 데이터 가져오기", description = "특정 크루의 월별 정기 런닝들의 누적횟수, 누적인원수, 실행 일자를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CrewMonthDataDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/running-notices/month-data")
    public ResponseEntity<CrewMonthDataDto> getCrewRunningMonthData(@PathVariable("crewId") Long crewId,
                                                                    @ModelAttribute @Valid YearMonthDto yearMonthDto,
                                                                    @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        List<RunningNotice> regulars = runningNoticeService.findRegularsByCrewAndMonth(crew,
                yearMonthDto.getYear(), yearMonthDto.getMonth());
        int totalRunningCount = regulars.size();

        List<Long> regularIds = regulars.stream().map(RunningNotice::getId).collect(Collectors.toList());
        long totalRunningMember = runningMemberService
                .countRunningMembersByRunningNoticeIds(regularIds)
                .values().stream().mapToLong(Long::longValue).sum();

        List<Integer> runningDates = regulars.stream().map(RunningNotice::getRunningDateTime)
                .map(LocalDateTime::getDayOfMonth)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CrewMonthDataDto(totalRunningCount, totalRunningMember, runningDates));
    }

}
