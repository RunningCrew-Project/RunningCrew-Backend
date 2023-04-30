package com.project.runningcrew.totalpost.contorller;

import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.resourceimage.service.BoardImageService;
import com.project.runningcrew.resourceimage.service.RunningNoticeImageService;
import com.project.runningcrew.totalpost.dto.PagingTotalPostDto;
import com.project.runningcrew.totalpost.entity.PostType;
import com.project.runningcrew.totalpost.entity.TotalPost;
import com.project.runningcrew.totalpost.repository.TotalPostRepository;
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
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "TotalPost", description = "게시글, 런닝공지를 포함한 전체 글에 관한 api")
@RestController
@RequiredArgsConstructor
public class TotalPostController {

    private final TotalPostRepository totalPostRepository;
    private final CrewService crewService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final BoardImageService boardImageService;
    private final RunningNoticeImageService runningNoticeImageService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;
    private int pagingSize = 15;


    @Operation(summary = "전체 글 가져오기", description = "전체 글을 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/total-posts")
    public ResponseEntity<PagingResponse<PagingTotalPostDto>> getTotalPost(
            @PathVariable("crewId") Long crewId,
            @RequestParam("page") @PositiveOrZero int page,
            @Parameter(hidden = true) @CurrentUser User user) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPost(crew, pageRequest);

        return ResponseEntity.ok(new PagingResponse<>(getPagingTotalPostDtoSlice(totalPosts)));
    }

    @Operation(summary = "검색어로 전체 글 가져오기", description = "검색어에 해당하는 전체 글을 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/total-posts/search")
    public ResponseEntity<PagingResponse<PagingTotalPostDto>> getTotalPostByKeyword(
            @PathVariable("crewId") Long crewId,
            @RequestParam("keyword") @NotBlank String keyword,
            @RequestParam("page") @PositiveOrZero int page,
            @Parameter(hidden = true) @CurrentUser User user) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByKeyword(crew, keyword, pageRequest);

        return ResponseEntity.ok(new PagingResponse<>(getPagingTotalPostDtoSlice(totalPosts)));
    }

    @Operation(summary = "멤버가 작성한 전체 글 가져오기", description = "멤버가 작성한 전체 글을 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/member/{memberId}/total-posts")
    public ResponseEntity<PagingResponse<PagingTotalPostDto>> getTotalPostByMemberId(
            @PathVariable("memberId") Long memberId,
            @RequestParam("page") @PositiveOrZero int page,
            @Parameter(hidden = true) @CurrentUser User user) {

        Member member = memberService.findById(memberId);
        memberAuthorizationChecker.checkMember(user, member.getCrew());

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByMember(member, pageRequest);

        return ResponseEntity.ok(new PagingResponse<>(getPagingTotalPostDtoSlice(totalPosts)));
    }


    @Operation(summary = "멤버가 댓글 단 전체 글 정보 가져오기",
            description = "멤버가 댓글 단 전체 글 정보를 페이징하여 가져온다.",
            security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/members/{memberId}/comments/total-posts")
    public ResponseEntity<PagingResponse<PagingTotalPostDto>> getCommentPageOfMember(
            @RequestParam("page") int page,
            @PathVariable("memberId") Long memberId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Member member = memberService.findById(memberId);
        memberAuthorizationChecker.checkMember(user, member.getCrew());

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<TotalPost> totalPosts = totalPostRepository.getTotalPostByCommentOfMember(member, pageRequest);

        return ResponseEntity.ok(new PagingResponse<>(getPagingTotalPostDtoSlice(totalPosts)));
    }




    private Slice<PagingTotalPostDto> getPagingTotalPostDtoSlice(Slice<TotalPost> totalPosts) {
        List<Long> boardIds = totalPosts.stream()
                .filter(p -> p.getPostType().equals(PostType.BOARD))
                .map(TotalPost::getId)
                .collect(Collectors.toList());
        Map<Long, String> boardFirstImageUrls = boardImageService.findFirstImageUrls(boardIds);
        Map<Long, Long> boardComments = commentService.countAllByBoardIds(boardIds);

        List<Long> runningNoticeIds = totalPosts.stream()
                .filter(p -> p.getPostType().equals(PostType.RUNNING_NOTICE))
                .map(TotalPost::getId)
                .collect(Collectors.toList());
        Map<Long, String> runningNoticeFirstImageUrls = runningNoticeImageService.findFirstImageUrls(runningNoticeIds);
        Map<Long, Long> runningNoticeComments = commentService.countAllByRunningNoticeIds(runningNoticeIds);

        List<PagingTotalPostDto> content = totalPosts.stream()
                .map(t -> {
                    if (t.getPostType().equals(PostType.BOARD)) {
                        return new PagingTotalPostDto(t,
                                boardFirstImageUrls.get(t.getId()),
                                boardComments.get(t.getId()));
                    } else {
                        return new PagingTotalPostDto(t,
                                runningNoticeFirstImageUrls.get(t.getId()),
                                runningNoticeComments.get(t.getId()));
                    }
                })
                .collect(Collectors.toList());
        return new SliceImpl<>(content, totalPosts.getPageable(), totalPosts.hasNext());
    }

}
