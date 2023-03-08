package com.project.runningcrew.area.service;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.area.repository.DongAreaRepository;
import com.project.runningcrew.exception.notFound.DongAreaNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DongAreaServiceTest {

    @Mock
    DongAreaRepository dongAreaRepository;

    @InjectMocks
    DongAreaService dongAreaService;

    @DisplayName("id 로 동 가져오기 성공 테스트")
    @Test
    public void findByIdTest1(@Mock GuArea guArea) {
        //given
        Long dongId = 1L;
        DongArea dongArea = new DongArea(dongId, "dong", guArea);
        when(dongAreaRepository.findById(dongId)).thenReturn(Optional.of(dongArea));

        ///when
        DongArea findDongArea = dongAreaService.findById(dongId);

        //then
        assertThat(findDongArea).isEqualTo(dongArea);
        verify(dongAreaRepository, times(1)).findById(dongId);
    }

    @DisplayName("id 로 동 가져오기 예외 테스트")
    @Test
    public void findByIdTest2() {
        //given
        Long dongId = 1L;
        when(dongAreaRepository.findById(dongId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> dongAreaService.findById(dongId))
                .isInstanceOf(DongAreaNotFoundException.class);
        verify(dongAreaRepository, times(1)).findById(dongId);
    }

    @DisplayName("특정 구의 모든 동 가져오기 테스트")
    @Test
    public void findAllByGuAreaTest(@Mock GuArea guArea) {
        //given
        List<DongArea> dongAreas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DongArea dongArea = new DongArea("dong" + i, guArea);
            dongAreas.add(dongArea);
        }
        when(dongAreaRepository.findAllByGuAreaOrderByNameAsc(guArea)).thenReturn(dongAreas);

        ///when
        List<DongArea> result = dongAreaService.findAllByGuArea(guArea);

        //then
        assertThat(result.size()).isSameAs(dongAreas.size());
        verify(dongAreaRepository, times(1)).findAllByGuAreaOrderByNameAsc(guArea);
    }

}