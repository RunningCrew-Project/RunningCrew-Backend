package com.project.runningcrew.area.service;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.repository.DongAreaRepository;
import com.project.runningcrew.exception.notFound.DongAreaNotFoundException;
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
     * 입력받은 dongAreaId 를 가진 DongArea 를 반환한다. 없다면 DongAreaNotFoundException 을 throw 한다
     *
     * @param dongAreaId 찾는 DongArea 의 id
     * @return dongAreaId 를 가진 DongArea
     * @throws DongAreaNotFoundException dongAreaId 를 가진 DongArea 가 없을 때
     */
    public DongArea findById(Long dongAreaId) {
        return dongAreaRepository.findById(dongAreaId).orElseThrow(DongAreaNotFoundException::new);
    }

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
