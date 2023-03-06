package com.project.runningcrew.crew;

import com.project.runningcrew.crew.dto.GetCrewResponse;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.service.CrewService;
import com.project.runningcrew.service.MemberService;
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
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Crew", description = "런닝 크루에 관한 api")
@RestController
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;
    private final MemberService memberService;

    @Operation(summary = "크루 가져오기", description = "crewId 에 해당하는 크루를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCrewResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/crews/{crewId}")
    public ResponseEntity<GetCrewResponse> getCrew(@PathVariable("crewId") Long crewId) {
        Crew crew = crewService.findById(crewId);
        Long memberCount = memberService.countAllByCrew(crew);
        return ResponseEntity.ok(new GetCrewResponse(crew, memberCount));
    }

    //TODO 크루 생성

    //TODO 크루 정보 수정

    //TODO 크루 삭제

    //TODO 검색어로 찾기

    //TODO 유저가 가입한 모든 크루

    //TODO 동에 따른 추천 크루

    //TODO 구에 따른 추천 크루

}
