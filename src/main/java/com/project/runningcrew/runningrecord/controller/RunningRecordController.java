package com.project.runningcrew.runningrecord.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.runningrecord.dto.CreatePersonalRunningRequest;
import com.project.runningcrew.runningrecord.dto.GetRunningRecordResponse;
import com.project.runningcrew.runningrecord.dto.GpsDto;
import com.project.runningcrew.runningrecord.entity.Gps;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Tag(name = "RunningRecord", description = "런닝기록에 관한 api")
@RestController
@RequiredArgsConstructor
public class RunningRecordController {

    private final RunningRecordService runningRecordService;
    @Value("${domain.name}")
    private String host;

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
    public ResponseEntity<Void> createPersonalRunning(
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

    //TODO 크루 런닝기록 생성

    //TODO 유저의 모든 런닝 기록 가져오기(페이징)

    //TODO 특정 날의 유저의 런닝기록 가져오기

    //TODO 특정 월의 유적의 런닝 누적거리, 시간 가져오기

}
