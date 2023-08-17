package com.project.runningcrew.crewcondition.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.crewcondition.dto.ChangeCrewConditionRequest;
import com.project.runningcrew.crewcondition.dto.GetCrewConditionResponse;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import com.project.runningcrew.crewcondition.service.CrewConditionService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "CrewCondition", description = "크루 가입조건에 관한 api")
@RestController
@RequiredArgsConstructor
public class CrewConditionController {

    private final CrewConditionService crewConditionService;
    private final CrewService crewService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;

    @Operation(summary = "크루 가입조건 정보 가져오기", description = "크루 가입조건 정보를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCrewConditionResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/crew-conditions")
    public ResponseEntity<GetCrewConditionResponse> getCrewCondition(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, crew);

        CrewCondition crewCondition = crewConditionService.findByCrew(crew);
        return ResponseEntity.ok(new GetCrewConditionResponse(crewCondition));
    }

    @Operation(summary = "크루 가입 정보 수정", description = "크루 가입 관련 정보를 수정한다", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/api/crews/{crewId}/crew-conditions")
    public ResponseEntity<Void> changeCrewCondition(@PathVariable("crewId") Long crewId,
                                                    @RequestBody @Valid ChangeCrewConditionRequest request,
                                                    @Parameter(hidden = true) @CurrentUser User user) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, crew);

        CrewCondition crewCondition = crewConditionService.findByCrew(crew);
        crewConditionService.updateCrewCondition(crewCondition,
                request.isJoinApply(), request.isJoinQuestion());

        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "크루 가입 신청 받기 수정", description = "크루 가입 신청 받기 설정을 수정한다", security = {@SecurityRequirement(name = "Bearer-Key")})
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @PutMapping(value = "/api/crews/{crewId}/crew-conditions/apply")
//    public ResponseEntity<Void> changeJoinApply(@PathVariable("crewId") Long crewId,
//                                                @Parameter(hidden = true) @CurrentUser User user) {
//
//        Crew crew = crewService.findById(crewId);
//        memberAuthorizationChecker.checkLeader(user, crew);
//
//        CrewCondition crewCondition = crewConditionService.findByCrew(crew);
//        if (crewCondition.isJoinApply()) {
//            crewConditionService.updateJoinApplyFalse(crewCondition);
//        } else {
//            crewConditionService.updateJoinApplyTrue(crewCondition);
//        }
//
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @Operation(summary = "크루 가입 질문 수정", description = "크루 가입 질문 설정을 수정한다", security = {@SecurityRequirement(name = "Bearer-Key")})
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @PutMapping(value = "/api/crews/{crewId}/crew-conditions/question")
//    public ResponseEntity<Void> changeJoinQuestion(@PathVariable("crewId") Long crewId,
//                                                   @Parameter(hidden = true) @CurrentUser User user) {
//
//        Crew crew = crewService.findById(crewId);
//        memberAuthorizationChecker.checkLeader(user, crew);
//
//        CrewCondition crewCondition = crewConditionService.findByCrew(crew);
//        if (crewCondition.isJoinQuestion()) {
//            crewConditionService.updateJoinQuestionFalse(crewCondition);
//        } else {
//            crewConditionService.updateJoinQuestionTrue(crewCondition);
//        }
//        return ResponseEntity.noContent().build();
//    }

}
