package com.project.runningcrew.service.areaService;

import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.repository.areas.DongAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DongAreaService {

    private final DongAreaRepository dongAreaRepository;

    /**
     * 특정 구 에 속한 모둔 동를 가져온다. 이때 이름순으로 오름차순 정렬되어 있다.
     *
     * @param guArea 동들이 속한 특정 구
     * @return 특정 구에 속하고 이름순으로 오름차순 정렬된 모든 동
     */
    public List<DongArea> findAllByGuArea(GuArea guArea) {
        return dongAreaRepository.findAllByGuAreaOrderByNameAsc(guArea);
    }

}
