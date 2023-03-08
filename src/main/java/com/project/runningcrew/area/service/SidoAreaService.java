package com.project.runningcrew.area.service;

import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.area.repository.SidoAreaRepository;
import com.project.runningcrew.exception.notFound.GuAreaNotFoundException;
import com.project.runningcrew.exception.notFound.SidoAreaNotFoundException;
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
     * 입력받은 sidoAreaId 를 가진 SidoArea 를 반환한다. 없다면 SidoAreaNotFoundException 을 throw 한다
     *
     * @param sidoAreaId 찾는 SidoArea 의 id
     * @return sidoAreaId 를 가진 SidoArea
     * @throws SidoAreaNotFoundException sidoAreaId 를 가진 SidoArea 가 없을 때
     */
    public SidoArea findById(Long sidoAreaId) {
        return sidoAreaRepository.findById(sidoAreaId).orElseThrow(SidoAreaNotFoundException::new);
    }

    /**
     * 모든 시/도를 가져온다. 이때 이름순으로 오름차순 정렬되어 있다.
     *
     * @return 이름순으로 오름차순 정렬된 모든 시/도
     */
    public List<SidoArea> findAll() {
        return sidoAreaRepository.findAllByOrderByNameAsc();
    }

}
