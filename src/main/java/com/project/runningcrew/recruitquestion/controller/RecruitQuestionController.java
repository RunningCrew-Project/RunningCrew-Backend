package com.project.runningcrew.recruitquestion.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.recruitquestion.dto.request.CreateRecruitQuestionDto;
import com.project.runningcrew.recruitquestion.dto.response.GetExistOfRecruitQuestion;
import com.project.runningcrew.recruitquestion.dto.response.GetRecruitQuestionDto;
import com.project.runningcrew.recruitquestion.dto.response.GetRecruitQuestionList;
import com.project.runningcrew.recruitquestion.entity.RecruitQuestion;
import com.project.runningcrew.recruitquestion.service.RecruitQuestionService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "recruitQuestion", description = "가입 질문에 관한 api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RecruitQuestionController {

    private final CrewService crewService;
    private final RecruitQuestionService recruitQuestionService;

    private final MemberAuthorizationChecker memberAuthorizationChecker;

    @Value("${domain.name}")
    private String host;



    @Operation(summary = "가입 질문 하나 생성하기", description = "가입 질문 하나를 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/crews/{crewId}/recruit-questions")
    public ResponseEntity<Void> createOneRecruitQuestion(
            @PathVariable("crewId") Long crewId,
            @RequestBody @Valid CreateRecruitQuestionDto createRecruitQuestionDto,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, crew);
        //note 요청 user 의 크루 Leader 자격 검증

        RecruitQuestion recruitQuestion = new RecruitQuestion(crew, createRecruitQuestionDto.getQuestion(), createRecruitQuestionDto.getQuestionOffset());
        Long questionId = recruitQuestionService.saveOneQuestion(recruitQuestion);

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/recruit-questions/{id}")
                .build(questionId);

        return ResponseEntity.created(uri).build();
    }



    @Operation(summary = "가입 질문 하나 삭제하기", description = "가입 질문 하나를 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/recruit-questions/{recruitQuestionId}")
    public ResponseEntity<Void> deleteOneRecruitQuestion(
            @PathVariable("recruitQuestionId") Long recruitQuestionId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        RecruitQuestion recruitQuestion = recruitQuestionService.findById(recruitQuestionId);
        Crew crew = recruitQuestion.getCrew();
        memberAuthorizationChecker.checkLeader(user, crew);
        //note 요청 user 의 크루 Leader 자격 검증

        recruitQuestionService.deleteOneQuestion(recruitQuestion);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "가입 질문 전체 조회하기", description = "가입 질문 전체를 조회한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetRecruitQuestionList.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/crews/{crewId}/recruit-questions")
    public ResponseEntity<GetRecruitQuestionList<GetRecruitQuestionDto>> getRecruitQuestionsOfCrew(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        List<RecruitQuestion> findQuestions = recruitQuestionService.findAllByCrew(crew);
        List<GetRecruitQuestionDto> dtoList = findQuestions.stream().map(GetRecruitQuestionDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new GetRecruitQuestionList<>(dtoList));
    }




    @Operation(summary = "특정 크루의 가입 질문 설정 여부",
            description = "크루의 가입 질문 설정 여부를 확인한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetExistOfRecruitQuestion.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/crews/{crewId}/set-questions")
    public ResponseEntity<GetExistOfRecruitQuestion> getExistOfRecruitQuestion(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        List<RecruitQuestion> questionList = recruitQuestionService.findAllByCrew(crew);

        if(!questionList.isEmpty()) {
            return ResponseEntity.ok(new GetExistOfRecruitQuestion(true));
        } else {
            return ResponseEntity.ok(new GetExistOfRecruitQuestion(false));
        }

    }





}
