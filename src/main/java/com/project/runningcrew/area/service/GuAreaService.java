package com.project.runningcrew.area.service;

import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.exception.badinput.GuFullNameException;
import com.project.runningcrew.exception.notFound.DongAreaNotFoundException;
import com.project.runningcrew.exception.notFound.GuAreaNotFoundException;
import com.project.runningcrew.exception.notFound.SidoAreaNotFoundException;
import com.project.runningcrew.area.repository.GuAreaRepository;
import com.project.runningcrew.area.repository.SidoAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuAreaService {

    private final SidoAreaRepository sidoAreaRepository;
    private final GuAreaRepository guAreaRepository;

    /**
     * 입력받은 guAreaId 를 가진 GuArea 를 반환한다. 없다면 GuAreaNotFoundException 을 throw 한다
     *
     * @param guAreaId 찾는 GuArea 의 id
     * @return guAreaId 를 가진 GuArea
     * @throws GuAreaNotFoundException guAreaId 를 가진 GuArea 가 없을 때
     */
    public GuArea findById(Long guAreaId) {
        return guAreaRepository.findById(guAreaId).orElseThrow(GuAreaNotFoundException::new);
    }

    /**
     * 특정 시/도 에 속한 모둔 구를 가져온다. 이때 이름순으로 오름차순 정렬되어 있다.
     *
     * @param sidoArea 구들이 속한 특정 시/도
     * @return 특정 시/도에 속하고 이름순으로 오름차순 정렬된 모든 구
     */
    public List<GuArea> findAllBySidoArea(SidoArea sidoArea) {
        return guAreaRepository.findAllBySidoAreaOrderByNameAsc(sidoArea);
    }

    /**
     * 구의 fullName 을 통해 GuArea 를 찾은 후, GuArea 의 id 값을 반환한다.
     *
     * @param fullName SidoArea 의 name 과 GuArea 의 name 이 합쳐진 이름. 중간에 공백이 필요함
     *                 ex) 서울시 동대문구
     * @return 찾은 GuArea 의 id
     * @throws GuFullNameException       올바르지 않은 형식의 fullName 일 때
     * @throws SidoAreaNotFoundException 특정 이름을 가진 SidoArea 가 존재하지 않을 때
     * @throws GuAreaNotFoundException   특정 이름을 가진 GuArea 가 존재하지 않을 때
     */
    public Long getIdByGuAreaFullName(String fullName) {
        if (!fullName.matches("([가-힣A-Za-z]+(시|도)\\s[가-힣A-Za-z]+(시|군|구)+)")) {
            throw new GuFullNameException(fullName);
        }
        String[] words = fullName.split("\\s");
        String sidoAreaName = words[0];
        String guAreaName = words[1];
        SidoArea sidoArea = sidoAreaRepository.findByName(sidoAreaName)
                .orElseThrow(SidoAreaNotFoundException::new);
        GuArea guArea = guAreaRepository.findBySidoAreaAndName(sidoArea, guAreaName)
                .orElseThrow(GuAreaNotFoundException::new);
        return guArea.getId();
    }

}
