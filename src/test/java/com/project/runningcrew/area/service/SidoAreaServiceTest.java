package com.project.runningcrew.area.service;

import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.area.service.SidoAreaService;
import com.project.runningcrew.area.repository.SidoAreaRepository;
import com.project.runningcrew.exception.notFound.SidoAreaNotFoundException;
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
class SidoAreaServiceTest {

    @Mock
    SidoAreaRepository sidoAreaRepository;

    @InjectMocks
    SidoAreaService sidoAreaService;

    @DisplayName("id 로 시/도 가져오기 성공 테스트")
    @Test
    public void findByIdTest1() {
        //given
        Long sidoId = 1L;
        SidoArea sidoArea = new SidoArea(sidoId, "sido");
        when(sidoAreaRepository.findById(sidoId)).thenReturn(Optional.of(sidoArea));

        ///when
        SidoArea findSidoArea = sidoAreaService.findById(sidoId);

        //then
        assertThat(findSidoArea).isEqualTo(sidoArea);
        verify(sidoAreaRepository, times(1)).findById(sidoId);
    }

    @DisplayName("id 로 시/도 가져오기 예외 테스트")
    @Test
    public void findByIdTest2() {
        //given
        Long sidoId = 1L;
        when(sidoAreaRepository.findById(sidoId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sidoAreaService.findById(sidoId))
                .isInstanceOf(SidoAreaNotFoundException.class);
    }

    @DisplayName("모든 시/도 가져오기 테스트")
    @Test
    void findAllTest() {
        //given
        List<SidoArea> sidoAreas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SidoArea sidoArea = new SidoArea("sido" + i);
            sidoAreas.add(sidoArea);
        }
        when(sidoAreaRepository.findAllByOrderByNameAsc()).thenReturn(sidoAreas);

        ///when
        List<SidoArea> result = sidoAreaService.findAll();

        //then
        assertThat(result.size()).isSameAs(sidoAreas.size());
        verify(sidoAreaRepository, times(1)).findAllByOrderByNameAsc();
    }

}