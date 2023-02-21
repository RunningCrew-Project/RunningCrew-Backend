package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.dto.SimpleRunningRecordDto;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;


public interface SimpleRunningRecordDtoRepository {

    /**
     * 특정 user 의 SimpleRunningRecordDto 들을 페이징 하여 반환
     *
     * @param user
     * @param pageable
     * @return 페이징 조건에 맞는 user 의 SimpleRunningRecordDto 가 담긴 Slice
     */
    Slice<SimpleRunningRecordDto> findSimpleRunningRecordDtoByUser(User user, Pageable pageable);

    /**
     * 특정 user 의 SimpleRunningRecordDto 중 범위에 속하는 날짜에 런닝을 시작한
     * 모든 SimpleRunningRecordDto 를 반환
     *
     * @param user  찾는 user
     * @param start 범위 시작
     * @param end   범위 종료. end 는 포함하지 안흠
     * @return 특정 user 의 start 와 end 사이에 런닝을 시작한 모든 SimpleRunningRecordDto 들의 list
     */
    List<SimpleRunningRecordDto> findSimpleRunningRecordDtoByByUserAndStartDateTimes(User user,
                                                                                     LocalDateTime start,
                                                                                     LocalDateTime end);

}
