package com.project.runningcrew.blocked.controller;

import com.project.runningcrew.blocked.dto.GetBlockedInfoListResponse;
import com.project.runningcrew.blocked.dto.SimpleBlockedInfoDto;
import com.project.runningcrew.blocked.entity.BlockedInfo;
import com.project.runningcrew.blocked.repository.BlockedInfoRepository;
import com.project.runningcrew.blocked.service.BlockedInfoService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.dto.JoinResponse;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Blocked", description = "멤버 차단 정보에 관한 api")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BlockedInfoController {


    private final MemberService memberService;
    private final CrewService crewService;
    private final BlockedInfoService blockedInfoService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;


    @Operation(summary = "멤버 차단하기", description = "멤버 차단 정보를 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PostMapping(value = "/api/crews/{crewId}/blocked/{blockedId}")
    public ResponseEntity<Void> createBlockInfo(
            @PathVariable("crewId") Long crewId,
            @PathVariable("blockedId") Long blockedId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);
        memberAuthorizationChecker.checkMember(user, crew);

        BlockedInfo blockedInfo = new BlockedInfo(member, blockedId);
        blockedInfoService.saveBlockedInfo(blockedInfo);
        return ResponseEntity.created(null).build();
    }


    @Operation(summary = "멤버 차단 해제하기", description = "멤버 차단 정보를 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @DeleteMapping(value = "/api/crews/{crewId}/blocked/{blockedId}")
    public ResponseEntity<Void> deleteBlockInfo(
            @PathVariable("crewId") Long crewId,
            @PathVariable("blockedId") Long blockedId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);
        memberAuthorizationChecker.checkMember(user, crew);

        BlockedInfo blockedInfo = blockedInfoService.findByTwoMemberId(member.getId(), blockedId);
        blockedInfoService.deleteBlockedInfo(blockedInfo);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "멤버의 차단 목록 조회", description = "멤버가 차단한 멤버 목록을 조회한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetBlockedInfoListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/blocked/{memberId}")
    public ResponseEntity<GetBlockedInfoListResponse> getBlockedMemberList(
            @PathVariable("crewId") Long crewId,
            @PathVariable("memberId") Long memberId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);
        memberAuthorizationChecker.checkMember(user, crew);

        List<Long> findBlockedList = blockedInfoService.findBlockedListByMember(member);
        List<SimpleBlockedInfoDto> dtoList = findBlockedList.stream()
                .map(SimpleBlockedInfoDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(new GetBlockedInfoListResponse(dtoList));
    }


}
