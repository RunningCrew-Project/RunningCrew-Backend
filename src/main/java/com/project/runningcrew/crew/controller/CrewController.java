package com.project.runningcrew.crew.controller;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.area.service.GuAreaService;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.crew.dto.CreateCrewRequest;
import com.project.runningcrew.crew.dto.CrewListResponse;
import com.project.runningcrew.crew.dto.GetCrewResponse;
import com.project.runningcrew.crew.dto.SimpleCrewDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Tag(name = "Crew", description = "런닝 크루에 관한 api")
@RestController
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;
    private final MemberService memberService;
    private final DongAreaService dongAreaService;
    private final GuAreaService guAreaService;
    private final String host = "localhost";
    private final int pagingSize = 15;

    @Operation(summary = "크루 가져오기", description = "crewId 에 해당하는 크루를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCrewResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}")
    public ResponseEntity<GetCrewResponse> getCrew(@PathVariable("crewId") Long crewId) {
        Crew crew = crewService.findById(crewId);
        Long memberCount = memberService.countAllByCrew(crew);
        return ResponseEntity.ok(new GetCrewResponse(crew, memberCount));
    }

    @Operation(summary = "크루 생성하기", description = "크루를 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCrew(@Parameter(hidden = true) User user,
                                     @ModelAttribute @Valid CreateCrewRequest createCrewRequest) {
        DongArea dongArea = dongAreaService.findById(createCrewRequest.getDongId());
        Crew crew = Crew.builder()
                .name(createCrewRequest.getName())
                .introduction(createCrewRequest.getIntroduction())
                .dongArea(dongArea)
                .build();
        Long crewId = crewService.saveCrew(user, crew, createCrewRequest.getFile());
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .path("/api/crews/{id}")
                .build(crewId);
        return ResponseEntity.created(uri).build();
    }

    //TODO 크루 정보 수정

    //TODO 크루 삭제

    @Operation(summary = "키워드에 맞는 크루 가져오기", description = "키워드에 맞는 크루를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class)))
    })
    @GetMapping(value = "/api/crews")
    public ResponseEntity<PagingResponse<Crew>> findCrewsByKeyword(@RequestParam("page") int page,
                                                             @RequestParam("keyword") String keyword) {
        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<Crew> crewSlice = crewService.findByKeyword(pageRequest, keyword);
        return ResponseEntity.ok(new PagingResponse<>(crewSlice));
    }

    //TODO 유저가 가입한 모든 크루

    @Operation(summary = "구에 대한 추천 크루 가져오기", description = "guAreaId 에 속하는 크루를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CrewListResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/gu-area/{guAreaId}")
    public ResponseEntity<CrewListResponse<SimpleCrewDto>> findRandomCrewsByGuArea(@PathVariable("guAreaId") Long guAreaId) {
        GuArea guArea = guAreaService.findById(guAreaId);
        List<Crew> crews = crewService.findRandomByGuArea(guArea, 4);
        List<Long> crewIds = crews.stream().map(Crew::getId).collect(Collectors.toList());
        Map<Long, Long> crewIdMemberCountMap = memberService.countAllByCrewIds(crewIds);
        List<SimpleCrewDto> simpleCrewDtoList = crews.stream()
                .map(c -> new SimpleCrewDto(c, crewIdMemberCountMap.get(c.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new CrewListResponse<>(simpleCrewDtoList));
    }

}
