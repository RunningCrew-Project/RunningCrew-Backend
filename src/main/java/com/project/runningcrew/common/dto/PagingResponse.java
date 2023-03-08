package com.project.runningcrew.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class PagingResponse<T> {

    @Schema(description = "조회된 데이터")
    private List<T> content;

    @Schema(description = "페이지 크기", example = "15")
    private int size;

    @Schema(description = "현재 페이지 번호. 0 시작", example = "0")
    private int number;

    @Schema(description = "조회된 데이터 수", example = "15")
    private int numberOfElements;

    @Schema(description = "첫 페이지 여부", example = "true")
    private boolean first;

    @Schema(description = "마지막 페이지 여부", example = "false")
    private boolean last;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    public PagingResponse(Slice<T> slice) {
        this.content = slice.getContent();
        this.size = slice.getSize();
        this.number = slice.getNumber();
        this.numberOfElements = slice.getNumberOfElements();
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
    }

}
