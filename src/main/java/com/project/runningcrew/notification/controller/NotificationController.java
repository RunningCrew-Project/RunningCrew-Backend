package com.project.runningcrew.notification.controller;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.notification.dto.SimpleNotificationDto;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.notification.service.NotificationService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Notification", description = "알림에 관한 api")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private int pagingSize = 15;


    @Operation(summary = "알림 가져오기", description = "로그인한 유저의 알림을 페이징하여 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/notifications")
    public ResponseEntity<PagingResponse<SimpleNotificationDto>> getNotifications(
            @RequestParam("page") @PositiveOrZero int page,
            @Parameter(hidden = true) @CurrentUser User user) {
        PageRequest pageRequest = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Notification> sliceNotifications = notificationService.findByUser(user, pageRequest);
        List<SimpleNotificationDto> content = sliceNotifications.stream().map(SimpleNotificationDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(content, sliceNotifications.getPageable(), sliceNotifications.hasNext())));
    }


}
