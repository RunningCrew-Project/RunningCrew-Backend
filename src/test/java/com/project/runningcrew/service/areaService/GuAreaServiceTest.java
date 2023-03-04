package com.project.runningcrew.service.areaService;

import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.exception.notFound.GuAreaNotFoundException;
import com.project.runningcrew.exception.notFound.SidoAreaNotFoundException;
import com.project.runningcrew.repository.areas.GuAreaRepository;
import com.project.runningcrew.repository.areas.SidoAreaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuAreaServiceTest {

    @Mock
    GuAreaRepository guAreaRepository;

    @Mock
    SidoAreaRepository sidoAreaRepository;

    @InjectMocks
    GuAreaService guAreaService;


    @DisplayName("특정 시/도 의 모든 구 가져오기 테스트")
    @Test
    public void findAllBySidoAreaTest(@Mock SidoArea sidoArea) {
        //given
        List<GuArea> guAreas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GuArea guArea = new GuArea("gu" + i, sidoArea);
            guAreas.add(guArea);
        }
        when(guAreaRepository.findAllBySidoAreaOrderByNameAsc(sidoArea)).thenReturn(guAreas);

        ///when
        List<GuArea> result = guAreaService.findAllBySidoArea(sidoArea);

        //then
        assertThat(result.size()).isSameAs(guAreas.size());
        verify(guAreaRepository, times(1)).findAllBySidoAreaOrderByNameAsc(sidoArea);
    }

    @DisplayName("구의 fullName 으로 GuArea 의 id 반환 성공 테스트")
    @Test
    public void getIdByGuAreaFullNameTest1() {
        //given
        String sidoName = "서울시";
        String guName = "동대문구";
        String fullName = sidoName + " " + guName;
        Long guAreaId = 3L;
        SidoArea sidoArea = new SidoArea(sidoName);
        GuArea guArea = new GuArea(guAreaId, guName, sidoArea);
        when(sidoAreaRepository.findByName(sidoName)).thenReturn(Optional.of(sidoArea));
        when(guAreaRepository.findBySidoAreaAndName(sidoArea, guName))
                .thenReturn(Optional.of(guArea));

        ///when
        Long findGuAreaId = guAreaService.getIdByGuAreaFullName(fullName);

        //then
        assertThat(findGuAreaId).isEqualTo(guAreaId);
        verify(sidoAreaRepository, times(1)).findByName(sidoName);
        verify(guAreaRepository, times(1)).findBySidoAreaAndName(sidoArea, guName);
    }

    @DisplayName("구의 fullName 으로 GuArea 의 id 반환 올바르지 않은 fullName 예외 테스트")
    @Test
    public void getIdByGuAreaFullNameTest2() {
        //given
        String fullName = "서울시동대문구";

        ///when
        //then
        assertThatThrownBy(() -> guAreaService.getIdByGuAreaFullName(fullName))
                .isInstanceOf(ValidationException.class);
    }

    @DisplayName("구의 fullName 으로 GuArea 의 id 반환 SidoArea 존재 안함 예외 테스트")
    @Test
    public void getIdByGuAreaFullNameTest3() {
        String sidoName = "서울시";
        String guName = "동대문구";
        String fullName = sidoName + " " + guName;
        when(sidoAreaRepository.findByName(sidoName)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> guAreaService.getIdByGuAreaFullName(fullName))
                .isInstanceOf(SidoAreaNotFoundException.class);
        verify(sidoAreaRepository, times(1)).findByName(sidoName);
    }

    @DisplayName("구의 fullName 으로 GuArea 의 id 반환 GuArea 존재 안함 예외 테스트")
    @Test
    public void getIdByGuAreaFullNameTest4() {
        //given
        String sidoName = "서울시";
        String guName = "동대문구";
        String fullName = sidoName + " " + guName;
        SidoArea sidoArea = new SidoArea(sidoName);
        when(sidoAreaRepository.findByName(sidoName)).thenReturn(Optional.of(sidoArea));
        when(guAreaRepository.findBySidoAreaAndName(sidoArea, guName))
                .thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> guAreaService.getIdByGuAreaFullName(fullName))
                .isInstanceOf(GuAreaNotFoundException.class);
        verify(sidoAreaRepository, times(1)).findByName(sidoName);
        verify(guAreaRepository, times(1)).findBySidoAreaAndName(sidoArea, guName);
    }

}