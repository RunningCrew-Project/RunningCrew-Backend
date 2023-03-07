package com.project.runningcrew.area.repository;

import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class GuAreaRepositoryTest {

    @Autowired
    GuAreaRepository guAreaRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("GuArea save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea("gu", sidoArea);

        ///when
        GuArea savedGuArea = guAreaRepository.save(guArea);

        //then
        assertThat(savedGuArea).isEqualTo(guArea);
    }

    @DisplayName("GuArea findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea("gu", sidoArea);
        guAreaRepository.save(guArea);

        ///when
        Optional<GuArea> optGuArea = guAreaRepository.findById(guArea.getId());

        //then
        assertThat(optGuArea).isNotEmpty();
        assertThat(optGuArea).hasValue(guArea);
    }

    @DisplayName("GuArea delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea("gu", sidoArea);
        guAreaRepository.save(guArea);

        ///when
        guAreaRepository.delete(guArea);

        //then
        Optional<GuArea> optGuArea = guAreaRepository.findById(guArea.getId());
        assertThat(optGuArea).isEmpty();
    }

    @DisplayName("GuArea findAllBySidoAreaOrOrderByNameAsc 테스트")
    @Test
    public void findAllBySidoAreaOrOrderByNameAscTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        for (int i = 0; i < 10; i++) {
            GuArea guArea = new GuArea("gu" + i, sidoArea);
            guAreaRepository.save(guArea);
        }

        ///when
        List<GuArea> guAreas = guAreaRepository.findAllBySidoAreaOrderByNameAsc(sidoArea);

        //then
        assertThat(guAreas.size()).isEqualTo(10);
    }

    @DisplayName("특정 시/도 에 속하고 특정 이름을 가진 GuArea 찾기 성공 테스트")
    @Test
    public void findBySidoAreaAndNameTest1() {
        //given
        String name = "동대문구";
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea(name, sidoArea);
        guAreaRepository.save(guArea);

        ///when
        Optional<GuArea> optionalGuArea = guAreaRepository.findBySidoAreaAndName(sidoArea, name);

        //then
        assertThat(optionalGuArea).isNotEmpty();
        assertThat(optionalGuArea).hasValue(guArea);
    }

    @DisplayName("특정 시/도 에 속하고 특정 이름을 가진 GuArea 찾기 실패 테스트")
    @Test
    public void findBySidoAreaAndNameTest2() {
        //given
        String name = "동대문구";
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);

        ///when
        Optional<GuArea> optionalGuArea = guAreaRepository.findBySidoAreaAndName(sidoArea, name);

        //then
        assertThat(optionalGuArea).isEmpty();
    }

}