package com.project.runningcrew.member.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exception.AuthorizationException;
import com.project.runningcrew.exception.badinput.UpdateMemberRoleException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.dto.CreateMemberRequest;
import com.project.runningcrew.member.dto.MemberListResponse;
import com.project.runningcrew.member.dto.SimpleMemberDto;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Member", description = "멤버에 관한 api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CrewService crewService;
    private final UserService userService;
    private final RunningNoticeService runningNoticeService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;
    @Value("${domain.name}")
    private String host;


    @Operation(summary = "멤버 가져오기", description = "memberId 에 해당하는 멤버를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleMemberDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/members/{memberId}")
    public ResponseEntity<SimpleMemberDto> getMember(@PathVariable("memberId") Long memberId,
                                                     @Parameter(hidden = true) @CurrentUser User user) {
        Member member = memberService.findById(memberId);
        memberAuthorizationChecker.checkMember(user, member.getCrew());
        return ResponseEntity.ok(new SimpleMemberDto(member));
    }


    @Operation(summary = "멤버 생성하기", description = "크루 가입 신청한 유저의 멤버를 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/members")
    public ResponseEntity<Void> createMember(@PathVariable("crewId") Long crewId,
                                             @RequestBody @Valid CreateMemberRequest createMemberRequest,
                                             @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkManager(user, crew);

        User joinUser = userService.findById(createMemberRequest.getUserId());
        Long memberId = memberService.acceptMember(new Member(joinUser, crew, MemberRole.ROLE_NORMAL));

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/members/{id}")
                .build(memberId);
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "멤버 삭제하기", description = "memberId 에 해당하는 멤버를 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/api/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("memberId") Long memberId,
                                             @Parameter(hidden = true) @CurrentUser User user) {
        Member member = memberService.findById(memberId);
        Member currentMember = memberAuthorizationChecker
                .checkAuthUserAndManger(user, member.getCrew(), member.getId());

        if (currentMember.equals(member)) {
            throw new AuthorizationException();
        }
        memberService.deleteMember(member);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "크루에 속한 모든 멤버 가져오기", description = "crewId 에 속한 크루의 모든 멤버를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/members")
    public ResponseEntity<MemberListResponse> getCrewMembers(@PathVariable("crewId") Long crewId,
                                                             @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        List<SimpleMemberDto> members = memberService.findAllByCrew(crew).stream()
                .map(SimpleMemberDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MemberListResponse(members.size(), members));
    }


    @Operation(summary = "크루에 속한 모든 관리자 멤버 가져오기", description = "crewId 에 속한 크루의 모든 관리자 멤버들을 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/members/managers")
    public ResponseEntity<MemberListResponse> getCrewManagers(@PathVariable("crewId") Long crewId,
                                                              @Parameter(hidden = true) @CurrentUser User user) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        List<SimpleMemberDto> managers = memberService.findCrewMangers(crew).stream()
                .map(SimpleMemberDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MemberListResponse(managers.size(), managers));
    }


    @Operation(summary = "멤버 권한 리더 변경", description = "매니저 멤버의 권한을 리더로 변경한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PutMapping("/api/members/{memberId}/leader")
    public ResponseEntity<Void> updateMemberLeader(@PathVariable("memberId") Long memberId,
                                                   @Parameter(hidden = true) @CurrentUser User user) {
        Member updateMember = memberService.findById(memberId);
        Member leaderMember = memberAuthorizationChecker.checkLeader(user, updateMember.getCrew());

        if (updateMember.getRole() != MemberRole.ROLE_MANAGER) {
            throw new UpdateMemberRoleException(updateMember.getRole());
        }
        memberService.updateMemberLeader(leaderMember, updateMember);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "멤버 권한 매니저 변경", description = "일반 멤버의 권한을 매니저로 변경한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PutMapping("/api/members/{memberId}/manager")
    public ResponseEntity<Void> updateMemberManger(@PathVariable("memberId") Long memberId,
                                                   @Parameter(hidden = true) @CurrentUser User user) {
        Member updateMember = memberService.findById(memberId);
        memberAuthorizationChecker.checkLeader(user, updateMember.getCrew());

        if (updateMember.getRole() != MemberRole.ROLE_NORMAL) {
            throw new UpdateMemberRoleException(updateMember.getRole());
        }
        memberService.updateMemberManager(updateMember);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "멤버 권한 일반 변경", description = "매니저 멤버의 권한을 일반 멤버로 변경한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PutMapping("/api/members/{memberId}/normal")
    public ResponseEntity<Void> updateMemberNormal(@PathVariable("memberId") Long memberId,
                                                   @Parameter(hidden = true) @CurrentUser User user) {
        Member updateMember = memberService.findById(memberId);
        memberAuthorizationChecker.checkLeader(user, updateMember.getCrew());

        if (updateMember.getRole() != MemberRole.ROLE_MANAGER) {
            throw new UpdateMemberRoleException(updateMember.getRole());
        }
        memberService.updateMemberManager(updateMember);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "런닝에 참여한 모든 멤버 가져오기", description = "runningNoticeId 에 속한 런닝공지에 참여한 모든 멤버를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-notices/{runningNoticeId}/members")
    public ResponseEntity<MemberListResponse> getRunningMembers(@PathVariable("runningNoticeId") Long runningNoticeId,
                                                                @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        memberAuthorizationChecker.checkMember(user, runningNotice.getMember().getCrew());

        List<SimpleMemberDto> runningMembers = memberService.findAllByRunningNotice(runningNotice).stream()
                .map(SimpleMemberDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MemberListResponse(runningMembers.size(), runningMembers));
    }

}
