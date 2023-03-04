package com.project.runningcrew.service.areaService;

import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.repository.areas.SidoAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SidoAreaService {

    private final SidoAreaRepository sidoAreaRepository;

    /**
     * 모든 시/도를 가져온다. 이때 이름순으로 오름차순 정렬되어 있다.
     *
     * @return 이름순으로 오름차순 정렬된 모든 시/도
     */
    public List<SidoArea> findAll() {
        return sidoAreaRepository.findAllByOrderByNameAsc();
    }

}
