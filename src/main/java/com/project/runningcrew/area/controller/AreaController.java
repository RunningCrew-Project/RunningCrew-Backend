package com.project.runningcrew.area.controller;

import com.project.runningcrew.area.dto.*;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.area.service.GuAreaService;
import com.project.runningcrew.area.service.SidoAreaService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Area", description = "지역에 관한 api")
@RestController
@RequiredArgsConstructor
public class AreaController {

    private final SidoAreaService sidoAreaService;
    private final GuAreaService guAreaService;
    private final DongAreaService dongAreaService;


    @Operation(summary = "모든 시/도 가져오기", description = "모든 시/도를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaListResponse.class)))
    })
    @GetMapping(value = "/api/sido-areas")
    public ResponseEntity<AreaListResponse<SimpleSidoDto>> getSidoAreas() {
        List<SimpleSidoDto> sidoDtoList = sidoAreaService.findAll().stream()
                .map(SimpleSidoDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AreaListResponse<>(sidoDtoList));
    }


    @Operation(summary = "특정 시/도의 모든 구 가져오기", description = "특정 시/도의 모든 구를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaListResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/sido-areas/{sidoId}/gu-areas")
    public ResponseEntity<AreaListResponse<SimpleGuDto>> getGuAreas(@PathVariable("sidoId") Long sidoId) {
        SidoArea sidoArea = sidoAreaService.findById(sidoId);
        List<SimpleGuDto> guDtoList = guAreaService.findAllBySidoArea(sidoArea).stream()
                .map(SimpleGuDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AreaListResponse<>(guDtoList));
    }


    @Operation(summary = "특정 구의 모든 동 가져오기", description = "특정 구의 모든 동을 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaListResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/gu-areas/{guId}/dong-areas")
    public ResponseEntity<AreaListResponse<SimpleDongDto>> getDongAreas(@PathVariable("guId") Long guId) {
        GuArea guArea = guAreaService.findById(guId);
        List<SimpleDongDto> dongDtoList = dongAreaService.findAllByGuArea(guArea).stream()
                .map(SimpleDongDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AreaListResponse<>(dongDtoList));
    }


    @Operation(summary = "특정 구의 이름으로 id 가져오기", description = "특정 구의 이름으로 구의 id 를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AreaListResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/gu-areas")
    public ResponseEntity<GuAreaIdResponse> findGuIdByFullName(
            @RequestParam(value = "name")
            @Pattern(regexp = "([가-힣A-Za-z]+(시|도)\\s[가-힣A-Za-z]+(시|군|구)+)",
                    message = "올바르지 않은 형식의 이름입니다.") String name) {
        Long guId = guAreaService.getIdByGuAreaFullName(name);
        return ResponseEntity.ok(new GuAreaIdResponse(guId));
    }

}
