package com.project.runningcrew.recruitanswer.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import com.project.runningcrew.crewcondition.service.CrewConditionService;
import com.project.runningcrew.exception.badinput.CrewJoinQuestionException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.recruitanswer.dto.request.CreateRecruitAnswerList;
import com.project.runningcrew.recruitanswer.dto.request.CreateRecruitAnswerDto;
import com.project.runningcrew.recruitanswer.dto.response.GetRecruitAnswerList;
import com.project.runningcrew.recruitanswer.dto.response.GetRecruitAnswerDto;
import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import com.project.runningcrew.recruitanswer.service.RecruitAnswerService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "RecruitAnswer", description = "가입 답변에 관한 api")
@RestController
@RequiredArgsConstructor
public class RecruitAnswerController {

    private final CrewService crewService;
    private final CrewConditionService crewConditionService;
    private final UserService userService;
    private final RecruitAnswerService recruitAnswerService;

    @Operation(summary = "가입 답변 묶음 생성하기",
            description = "가입 답변 묶음을 생성한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/crews/{crewId}/recruit-answers")
    public ResponseEntity<Void> createRecruitAnswer(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody @Valid CreateRecruitAnswerList<CreateRecruitAnswerDto> createRecruitAnswerList
    ) {
        Crew crew = crewService.findById(crewId);
        CrewCondition crewCondition = crewConditionService.findByCrew(crew);
        if (!crewCondition.isJoinQuestion()) {
            throw new CrewJoinQuestionException();
        }

        List<CreateRecruitAnswerDto> requestDtoList = createRecruitAnswerList.getAnswers();

        List<RecruitAnswer> answerList = new ArrayList<>();
        //note 입력받은 dtoList 를 기반으로 생성한 RecruitAnswerList
        for (CreateRecruitAnswerDto requestDto : requestDtoList) {
            RecruitAnswer recruitAnswer = new RecruitAnswer(user, crew, requestDto.getAnswer(), requestDto.getAnswerOffset());
            answerList.add(recruitAnswer);
        }

        recruitAnswerService.saveAllRecruitAnswer(answerList);
        return ResponseEntity.created(null).build();
        //note location 이 없는 created return, null 명시.
    }



    @Operation(summary = "가입 답변 모두 삭제하기",
            description = "유저가 작성한 크루 가입 답변을 모두 삭제한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/crews/{crewId}/users/{userId}/recruit-answers")
    public ResponseEntity<Void> deleteRecruitAnswer(
            @PathVariable("crewId") Long crewId,
            @PathVariable("userId") Long userId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        User findUser = userService.findById(userId);
        Crew findCrew = crewService.findById(crewId);
        CrewCondition crewCondition = crewConditionService.findByCrew(findCrew);
        if (!crewCondition.isJoinQuestion()) {
            throw new CrewJoinQuestionException();
        }

        recruitAnswerService.deleteAllRecruitAnswer(findUser, findCrew);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "가입 답변 조회하기",
            description = "유저가 특정 크루에 작성한 가입 답변들을 묶음 조회한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetRecruitAnswerList.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("api/crews/{crewId}/users/{userId}/recruit-answers")
    public ResponseEntity<GetRecruitAnswerList<GetRecruitAnswerDto>> getRecruitAnswer(
        @PathVariable("crewId") Long crewId,
        @PathVariable("userId") Long userId,
        @Parameter(hidden = true) @CurrentUser User user
    ) {
        User findUser = userService.findById(userId);
        Crew findCrew = crewService.findById(crewId);
        List<RecruitAnswer> recruitAnswerList = recruitAnswerService.findAllByUserAndCrew(findUser, findCrew);

        List<GetRecruitAnswerDto> dtoList = recruitAnswerList.stream()
                .map(GetRecruitAnswerDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GetRecruitAnswerList<>(dtoList));
    }


}
