package com.project.runningcrew.crew.controller;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.area.service.GuAreaService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.crew.dto.*;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import com.project.runningcrew.crewcondition.service.CrewConditionService;
import com.project.runningcrew.exception.duplicate.CrewNameDuplicateException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import com.project.runningcrew.recruitanswer.service.RecruitAnswerService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
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
    private final RecruitAnswerService recruitAnswerService;
    private final CrewConditionService crewConditionService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;
    @Value("${domain.name}")
    private String host;
    private int pagingSize = 15;

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
        CrewCondition crewCondition = crewConditionService.findByCrew(crew);
        return ResponseEntity.ok(new GetCrewResponse(crew, memberCount, crewCondition));
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
    public ResponseEntity<Void> createCrew(@Parameter(hidden = true) @CurrentUser User user,
                                           @ModelAttribute @Valid CreateCrewRequest createCrewRequest) {
        DongArea dongArea = dongAreaService.findById(createCrewRequest.getDongId());
        Crew crew = Crew.builder()
                .name(createCrewRequest.getName())
                .introduction(createCrewRequest.getIntroduction())
                .dongArea(dongArea)
                .build();
        Long crewId = crewService.saveCrew(user, crew, createCrewRequest.getFile());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/crews/{id}")
                .build(crewId);
        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "크루 수정하기", description = "크루의 정보를 수정한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/api/crews/{crewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCrew(@Parameter(hidden = true) @CurrentUser User user,
                                           @PathVariable("crewId") Long crewId,
                                           @ModelAttribute @Valid UpdateCrewRequest updateCrewRequest) {
        Crew originCrew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, originCrew);

        DongArea dongArea = dongAreaService.findById(updateCrewRequest.getDongId());
        Crew newCrew = Crew.builder()
                .name(updateCrewRequest.getName())
                .introduction(updateCrewRequest.getIntroduction())
                .dongArea(dongArea)
                .build();

        crewService.updateCrew(originCrew, newCrew, updateCrewRequest.getFile());

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "크루 삭제하기", description = "크루를 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @DeleteMapping(value = "/api/crews/{crewId}")
    public ResponseEntity<Void> deleteCrew(@Parameter(hidden = true) @CurrentUser User user,
                                           @PathVariable("crewId") Long crewId) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, crew);

        crewService.deleteCrew(crew);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "크루 이름 중복 확인", description = "입력받은 이름을 가진 크루 확인한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/name")
    public ResponseEntity<Void> checkCrewNameDuplicate(@RequestBody CrewNameRequest crewNameRequest) {
        String crewName = crewNameRequest.getName();
        if (crewService.existsByName(crewName)) {
            throw new CrewNameDuplicateException(crewName);
        }

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "키워드에 맞는 크루 가져오기", description = "키워드에 맞는 크루를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class)))
    })
    @GetMapping(value = "/api/crews")
    public ResponseEntity<PagingResponse<SimpleCrewDto>> findCrewsByKeyword(
            @RequestParam("page") @PositiveOrZero int page,
            @RequestParam("keyword") String keyword) {
        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<Crew> crewSlice = crewService.findByKeyword(pageRequest, keyword);
        List<Long> crewIds = crewSlice.stream().map(Crew::getId).collect(Collectors.toList());
        Map<Long, Long> crewIdMemberCountMap = memberService.countAllByCrewIds(crewIds);
        List<SimpleCrewDto> simpleCrewDtos = crewSlice.stream()
                .map(c -> new SimpleCrewDto(c, crewIdMemberCountMap.get(c.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(simpleCrewDtos, crewSlice.getPageable(), crewSlice.hasNext())));
    }


    @Operation(summary = "유저가 가입한 크루 가져오기", description = "유저가 가입한 크루를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CrewListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/users/crews")
    public ResponseEntity<CrewListResponse<UserCrewDto>> findCrewsByUser(@Parameter(hidden = true) @CurrentUser User user) {
        List<Crew> crews = crewService.findAllByUser(user);
        List<UserCrewDto> userCrewDtos = crews.stream().map(UserCrewDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new CrewListResponse<>(userCrewDtos));
    }


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


    @Operation(summary = "유저의 크루 가입여부", description = "유저의 크루 가입여부를 확인한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JoinResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/join")
    public ResponseEntity<JoinResponse> checkCrewJoin(@PathVariable("crewId") Long crewId,
                                                      @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        List<Crew> crews = crewService.findAllByUser(user);
        boolean result = crews.stream().anyMatch(c -> c.equals(crew));

        return ResponseEntity.ok(new JoinResponse(String.valueOf(result)));
    }


    @Operation(summary = "유저의 크루 가입 신청여부", description = "유저의 크루 가입 신청 여부를 확인한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JoinResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/apply")
    public ResponseEntity<JoinResponse> checkCrewApply(@PathVariable("crewId") Long crewId,
                                                       @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        List<RecruitAnswer> recruitAnswers = recruitAnswerService.findAllByUserAndCrew(user, crew);

        return ResponseEntity.ok(new JoinResponse(String.valueOf(!recruitAnswers.isEmpty())));
    }

}
