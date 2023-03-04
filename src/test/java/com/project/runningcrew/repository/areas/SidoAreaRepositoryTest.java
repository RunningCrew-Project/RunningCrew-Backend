package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.SidoArea;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SidoAreaRepositoryTest {

    @Autowired
    SidoAreaRepository sidoAreaRepository;

    @DisplayName("SidoArea save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = new SidoArea("sido");

        ///when
        SidoArea savedSidoArea = sidoAreaRepository.save(sidoArea);

        //then
        assertThat(savedSidoArea).isEqualTo(sidoArea);
    }

    @DisplayName("SidoArea findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = new SidoArea("sido");
        sidoAreaRepository.save(sidoArea);

        ///when
        Optional<SidoArea> optSidoArea = sidoAreaRepository.findById(sidoArea.getId());

        //then
        assertThat(optSidoArea).isNotEmpty();
        assertThat(optSidoArea).hasValue(sidoArea);
    }

    @DisplayName("SidoArea delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = new SidoArea("sido");
        sidoAreaRepository.save(sidoArea);

        ///when
        sidoAreaRepository.delete(sidoArea);

        //then
        Optional<SidoArea> optSidoArea = sidoAreaRepository.findById(sidoArea.getId());
        assertThat(optSidoArea).isEmpty();
    }

    @DisplayName("SidoArea findAllByOrderOrderByNameAsc 테스트")
    @Test
    public void findAllByOrderOrderByNameAscTest() {
        //given
        for (int i = 0; i < 10; i++) {
            SidoArea sidoArea = new SidoArea("sido" + i);
            sidoAreaRepository.save(sidoArea);
        }

        ///when
        List<SidoArea> sidoAreas = sidoAreaRepository.findAllByOrderByNameAsc();

        //then
        assertThat(sidoAreas.size()).isEqualTo(10);
    }

    @DisplayName("특정 이름을 가진 시/도 찾기 테스트")
    @Test
    public void findByNameTest1() {
        //given
        String name = "서울시";
        SidoArea sidoArea = new SidoArea(name);
        sidoAreaRepository.save(sidoArea);

        ///when
        Optional<SidoArea> optionalSidoArea = sidoAreaRepository.findByName(name);

        //then
        assertThat(optionalSidoArea).isNotEmpty();
        assertThat(optionalSidoArea).hasValue(sidoArea);
    }

}