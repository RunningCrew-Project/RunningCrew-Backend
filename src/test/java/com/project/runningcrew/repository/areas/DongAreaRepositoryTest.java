package com.project.runningcrew.repository.areas;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.area.repository.DongAreaRepository;
import com.project.runningcrew.repository.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DongAreaRepositoryTest {

    @Autowired
    DongAreaRepository dongAreaRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("DongArea save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);
        DongArea dongArea = new DongArea("dong", guArea);

        ///when
        DongArea savedDongArea = dongAreaRepository.save(dongArea);

        //then
        assertThat(savedDongArea).isEqualTo(dongArea);
    }

    @DisplayName("DongArea findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);
        DongArea dongArea = new DongArea("dong", guArea);
        dongAreaRepository.save(dongArea);

        ///when
        Optional<DongArea> optDongArea = dongAreaRepository.findById(dongArea.getId());

        //then
        assertThat(optDongArea).isNotEmpty();
        assertThat(optDongArea).hasValue(dongArea);
    }

    @DisplayName("DongArea delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);
        DongArea dongArea = new DongArea("dong", guArea);
        dongAreaRepository.save(dongArea);

        ///when
        dongAreaRepository.delete(dongArea);

        //then
        Optional<DongArea> optDongArea = dongAreaRepository.findById(dongArea.getId());
        assertThat(optDongArea).isEmpty();
    }

    @DisplayName("DongArea findAllByGuAreaOrderByNameAsc 테스트")
    @Test
    public void findAllByGuAreaOrOrderByNameAscTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);
        for (int i = 0; i < 10; i++) {
            DongArea dongArea = new DongArea("dong" + i, guArea);
            dongAreaRepository.save(dongArea);
        }

        ///when
        List<DongArea> dongAreas = dongAreaRepository.findAllByGuAreaOrderByNameAsc(guArea);

        //then
        assertThat(dongAreas.size()).isEqualTo(10);
    }

    @DisplayName("특정 이름을 가진 DongArea 찾기 성공 테스트")
    @Test
    public void findByNameTest1() {
        //given
        String name = "dong";
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);
        DongArea dongArea = new DongArea(name, guArea);
        dongAreaRepository.save(dongArea);

        ///when
        Optional<DongArea> optDongArea = dongAreaRepository.findByName(name);

        //then
        assertThat(optDongArea).isNotEmpty();
        assertThat(optDongArea).hasValue(dongArea);
    }

    @DisplayName("특정 구에 속하고 특정 이름을 가진 DongArea 찾기 성공 테스트")
    @Test
    public void findByGuAreaAndNameTest1() {
        //given
        String name = "dong";
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);
        DongArea dongArea = new DongArea(name, guArea);
        dongAreaRepository.save(dongArea);

        ///when
        Optional<DongArea> optDongArea = dongAreaRepository.findByGuAreaAndName(guArea, name);

        //then
        assertThat(optDongArea).isNotEmpty();
        assertThat(optDongArea).hasValue(dongArea);
    }

    @DisplayName("특정 구에 속하고 특정 이름을 가진 DongArea 찾기 실패 테스트")
    @Test
    public void findByGuAreaAndNameTest2() {
        //given
        String name = "dong";
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea,0);

        ///when
        Optional<DongArea> optDongArea = dongAreaRepository.findByGuAreaAndName(guArea, name);

        //then
        assertThat(optDongArea).isEmpty();
    }

}