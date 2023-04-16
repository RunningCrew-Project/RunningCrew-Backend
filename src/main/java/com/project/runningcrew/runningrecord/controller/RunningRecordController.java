package com.project.runningcrew.runningrecord.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.common.dto.SimpleRunningRecordDto;
import com.project.runningcrew.common.dto.YearMonthDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.exception.badinput.RunningNoticeDoneException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.entity.RunningStatus;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
import com.project.runningcrew.runningrecord.dto.*;
import com.project.runningcrew.runningrecord.entity.CrewRunningRecord;
import com.project.runningcrew.runningrecord.entity.Gps;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.runningrecord.service.RunningRecordService;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.service.UserService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "RunningRecord", description = "런닝기록에 관한 api")
@RestController
@RequiredArgsConstructor
public class RunningRecordController {

    private final RunningRecordService runningRecordService;
    private final RunningNoticeService runningNoticeService;
    private final UserService userService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;
    @Value("${domain.name}")
    private String host;
    private int pagingSize = 15;


    @Operation(summary = "런닝기록 가져오기", description = "runningRecordId 에 해당하는 런닝기록을 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetRunningRecordResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-records/{runningRecordId}")
    public ResponseEntity<GetRunningRecordResponse> getRunningRecords(@PathVariable("runningRecordId") Long runningRecordId,
                                                                      @Parameter(hidden = true) @CurrentUser User user) {

        RunningRecord runningRecord = runningRecordService.findById(runningRecordId);
        return ResponseEntity.ok(new GetRunningRecordResponse(runningRecord));
    }


    @Operation(summary = "개인 런닝기록 생성", description = "개인 런닝기록을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/running-records/personal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPersonalRunningRecord(
            @ModelAttribute @Valid CreatePersonalRunningRequest createPersonalRunningRequest,
            @Parameter(hidden = true) @CurrentUser User user) {

        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("개인 런닝")
                .startDateTime(createPersonalRunningRequest.getStartDateTime())
                .location(createPersonalRunningRequest.getLocation())
                .runningDistance(createPersonalRunningRequest.getRunningDistance())
                .runningTime(createPersonalRunningRequest.getRunningTime())
                .runningFace(createPersonalRunningRequest.getRunningFace())
                .calories(createPersonalRunningRequest.getCalories())
                .running_detail(createPersonalRunningRequest.getRunningDetails())
                .user(user)
                .build();

        List<GpsDto> gpsDtoList = createPersonalRunningRequest.getGps();
        for (int i = 0; i < gpsDtoList.size(); i++) {
            GpsDto gpsDto = gpsDtoList.get(i);
            new Gps(gpsDto.getLatitude(), gpsDto.getLongitude(), i, personalRunningRecord);
        }

        Long runningRecordId = runningRecordService.saveRunningRecord(personalRunningRecord,
                createPersonalRunningRequest.getFiles());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/running-records/{id}")
                .build(runningRecordId);
        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "크루 런닝기록 생성", description = "크루 런닝기록을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/running-records/crew", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCrewRunningRecord(
            @ModelAttribute @Valid CreateCrewRunningRequest createCrewRunningRequest,
            @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(createCrewRunningRequest.getRunningNoticeId());
        if (runningNotice.getStatus() == RunningStatus.DONE) {
            throw new RunningNoticeDoneException();
        }
        Crew crew = runningNotice.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .title(crew.getName() + " " + runningNotice.getNoticeType().getName())
                .startDateTime(createCrewRunningRequest.getStartDateTime())
                .location(createCrewRunningRequest.getLocation())
                .runningDistance(createCrewRunningRequest.getRunningDistance())
                .runningTime(createCrewRunningRequest.getRunningTime())
                .runningFace(createCrewRunningRequest.getRunningFace())
                .calories(createCrewRunningRequest.getCalories())
                .running_detail(createCrewRunningRequest.getRunningDetails())
                .user(user)
                .build();

        List<GpsDto> gpsDtoList = createCrewRunningRequest.getGps();
        for (int i = 0; i < gpsDtoList.size(); i++) {
            GpsDto gpsDto = gpsDtoList.get(i);
            new Gps(gpsDto.getLatitude(), gpsDto.getLongitude(), i, crewRunningRecord);
        }

        Long runningRecordId = runningRecordService.saveRunningRecord(crewRunningRecord,
                createCrewRunningRequest.getFiles());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/running-records/{id}")
                .build(runningRecordId);
        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "유저의 모든 런닝기록 가져오기", description = "유저의 모든 런닝기록을 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/users/{userId}/running-records")
    public ResponseEntity<PagingResponse<SimpleRunningRecordDto>> getRunningRecordsByUser(
            @PathVariable("userId") Long userId,
            @RequestParam("page") @PositiveOrZero int page,
            @Parameter(hidden = true) @CurrentUser User user) {

        User findUser = userService.findById(userId);
        PageRequest pageRequest = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<RunningRecord> runningRecordSlice = runningRecordService.findByUser(findUser, pageRequest);

        List<SimpleRunningRecordDto> simpleRunningRecordDtoList = runningRecordSlice.stream()
                .map(SimpleRunningRecordDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(simpleRunningRecordDtoList, runningRecordSlice.getPageable(), runningRecordSlice.hasNext())));
    }


    @Operation(summary = "특정 날의 런닝 기록 가져오기", description = "로그인한 유저의 특정 날의 모든 런닝기록을 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunningRecordListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-records/on-date")
    public ResponseEntity<RunningRecordListResponse> getRunningRecordByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @Parameter(hidden = true) @CurrentUser User user) {
        List<RunningRecord> runningRecords = runningRecordService.findAllByUserAndStartDate(user, date);
        List<SimpleRunningRecordDto> simpleRunningRecordDtos = runningRecords.stream()
                .map(SimpleRunningRecordDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new RunningRecordListResponse(simpleRunningRecordDtos));
    }


    @Operation(summary = "특정 월의 런닝 누적 데이터 가져오기", description = "로그인한 유저의 특정 월의 런닝 누적 거리와 누적 시간을 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonthDataResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-records/month-data")
    public ResponseEntity<MonthDataResponse> getRunningRecordMonthData(@ModelAttribute @Valid YearMonthDto yearMonthDto,
                                                                       @Parameter(hidden = true) @CurrentUser User user) {
        int year = yearMonthDto.getYear();
        int month = yearMonthDto.getMonth();
        double totalRunningDistance = runningRecordService.getSumOfRunningDistanceOfMonth(user, year, month);
        int totalRunningTime = runningRecordService.getSumOfRunningTimeOfMonth(user, year, month);

        return ResponseEntity.ok(new MonthDataResponse(totalRunningDistance, totalRunningTime));
    }

}
