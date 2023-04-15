package com.project.runningcrew.crew.service;

import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import com.project.runningcrew.crewcondition.repository.CrewConditionRepository;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.duplicate.CrewNameDuplicateException;
import com.project.runningcrew.exception.notFound.CrewNotFoundException;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewServiceTest {

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CrewConditionRepository crewConditionRepository;

    @InjectMocks
    private CrewService crewService;

    private final String CREW_IMAGE_DIR_NAME = "crew";
    private final String DEFAULT_CREW_IMG = "크루 기본 이미지.svg";
    private final String DEFAULT_CREW_IMG_PATH = CREW_IMAGE_DIR_NAME + "/" + DEFAULT_CREW_IMG;

    @DisplayName("id 로 크루 하나 가져오기 성공 테스트")
    @Test
    public void findByIdTest1() {
        //given
        Long crewId = 1L;
        Crew crew = Crew.builder().build();

        when(crewRepository.findById(crewId)).thenReturn(Optional.of(crew));

        ///when
        Crew findCrew = crewService.findById(crewId);

        //then
        assertThat(findCrew).isEqualTo(crew);
        verify(crewRepository, times(1)).findById(crewId);
    }

    @DisplayName("id 로 크루 하나 가져오기 예외 테스트")
    @Test
    public void findByTest2() {
        //given
        Long crewId = 1L;
        when(crewRepository.findById(crewId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> crewService.findById(crewId))
                .isInstanceOf(CrewNotFoundException.class);
        verify(crewRepository, times(1)).findById(crewId);
    }

    @DisplayName("크루 저장 성공 테스트")
    @Test
    public void saveTest1(@Mock DongArea dongArea, @Mock User user) {
        //given
        Long crewId = 1L;
        Long memberId = 2L;
        Crew crew = Crew.builder().id(crewId)
                .name("name")
                .introduction("introduction")
                .dongArea(dongArea)
                .build();
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_LEADER);
        CrewCondition crewCondition = new CrewCondition(crew);
        when(crewRepository.existsByName(crew.getName())).thenReturn(false);
        when(imageService.getImage(CREW_IMAGE_DIR_NAME, DEFAULT_CREW_IMG)).thenReturn("img");
        when(crewRepository.save(crew)).thenReturn(crew);
        when(crewConditionRepository.save(any())).thenReturn(crewCondition);
        when(memberRepository.save(any())).thenReturn(member);

        ///when
        Long saveId = crewService.saveCrew(user, crew);

        //then
        assertThat(saveId).isSameAs(crewId);
        verify(crewRepository, times(1)).existsByName(crew.getName());
        verify(imageService, times(1)).getImage(CREW_IMAGE_DIR_NAME, DEFAULT_CREW_IMG);
        verify(crewRepository, times(1)).save(crew);
        verify(crewConditionRepository, times(1)).save(any());
        verify(memberRepository, times(1)).save(any());
    }

    @DisplayName("크루 저장 예외 테스트")
    @Test
    public void saveTest2(@Mock DongArea dongArea, @Mock User user) {
        //given
        Crew crew = Crew.builder()
                .name("name")
                .introduction("introduction")
                .dongArea(dongArea)
                .build();
        when(crewRepository.existsByName(crew.getName())).thenReturn(true);

        ///when
        //then
        assertThatThrownBy(() -> crewService.saveCrew(user, crew))
                .isInstanceOf(CrewNameDuplicateException.class);
        verify(crewRepository, times(1)).existsByName(crew.getName());
    }

    @DisplayName("기본 이미지 크루 수정 성공 테스트")
    @Test
    public void updateCrewTest1(@Mock DongArea dongArea1, @Mock DongArea dongArea2,
                                @Mock MultipartFile multipartFile) {
        //given
        Long crewId = 1L;
        String newCrewImgUrl = "newCrewImgUrl";
        Crew originCrew = Crew.builder().id(crewId)
                .name("name1")
                .introduction("introduction1")
                .crewImgUrl("defaultUrl")
                .dongArea(dongArea1)
                .build();
        Crew newCrew = Crew.builder()
                .name("name2")
                .introduction("introduction2")
                .dongArea(dongArea2)
                .build();
        when(crewRepository.existsByName(newCrew.getName())).thenReturn(false);
        when(imageService.decodeURL("defaultUrl")).thenReturn(DEFAULT_CREW_IMG_PATH);
        when(imageService.uploadImage(multipartFile, "crew")).thenReturn(newCrewImgUrl);

        ///when
        crewService.updateCrew(originCrew, newCrew, multipartFile);

        //then
        assertThat(originCrew.getName()).isEqualTo(newCrew.getName());
        assertThat(originCrew.getIntroduction()).isEqualTo(newCrew.getIntroduction());
        assertThat(originCrew.getCrewImgUrl()).isEqualTo(newCrewImgUrl);
        assertThat(originCrew.getDongArea()).isEqualTo(newCrew.getDongArea());
        verify(crewRepository, times(1)).existsByName(newCrew.getName());
        verify(imageService, times(1)).uploadImage(multipartFile, "crew");
    }

    @DisplayName("기본 이미지 아닌 크루 수정 성공 테스트")
    @Test
    public void updateCrewTest2(@Mock DongArea dongArea1, @Mock DongArea dongArea2,
                                @Mock MultipartFile multipartFile) {
        //given
        Long crewId = 1L;
        String newCrewImgUrl = "crewImgUrl2";
        Crew originCrew = Crew.builder().id(crewId)
                .name("name1")
                .introduction("introduction1")
                .crewImgUrl("crewImgUrl")
                .dongArea(dongArea1)
                .build();
        Crew newCrew = Crew.builder()
                .name("name2")
                .introduction("introduction2")
                .dongArea(dongArea2)
                .build();
        when(crewRepository.existsByName(newCrew.getName())).thenReturn(false);
        when(imageService.decodeURL("crewImgUrl")).thenReturn("crewImgUrl");
        doNothing().when(imageService).deleteImage(originCrew.getCrewImgUrl());
        when(imageService.uploadImage(multipartFile, "crew")).thenReturn(newCrewImgUrl);

        ///when
        crewService.updateCrew(originCrew, newCrew, multipartFile);

        //then
        assertThat(originCrew.getName()).isEqualTo(newCrew.getName());
        assertThat(originCrew.getIntroduction()).isEqualTo(newCrew.getIntroduction());
        assertThat(originCrew.getCrewImgUrl()).isEqualTo(newCrewImgUrl);
        assertThat(originCrew.getDongArea()).isEqualTo(newCrew.getDongArea());
        verify(crewRepository, times(1)).existsByName(newCrew.getName());
        verify(imageService, times(1)).deleteImage(any());
        verify(imageService, times(1)).uploadImage(multipartFile, "crew");
    }

    @DisplayName("크루 수정 예외 테스트")
    @Test
    public void updateCrewTest3(@Mock DongArea dongArea1, @Mock DongArea dongArea2,
                                @Mock MultipartFile multipartFile) {
        //given
        Long crewId = 1L;
        String newCrewImgUrl = "crewImgUrl2";
        Crew originCrew = Crew.builder().id(crewId)
                .name("name1")
                .introduction("introduction1")
                .crewImgUrl("crewImgUrl1")
                .dongArea(dongArea1)
                .build();
        Crew newCrew = Crew.builder()
                .name("name2")
                .introduction("introduction2")
                .dongArea(dongArea2)
                .build();
        when(crewRepository.existsByName(newCrew.getName())).thenReturn(true);

        ///when
        //then
        assertThatThrownBy(() -> crewService.updateCrew(originCrew, newCrew, multipartFile))
                .isInstanceOf(CrewNameDuplicateException.class);
        verify(crewRepository, times(1)).existsByName(newCrew.getName());
    }

    @DisplayName("키워드로 크루 개수 반환 테스트")
    @Test
    public void countAllByKeywordTest(@Mock DongArea dongArea) {
        //given
        Long count = 10L;
        String keyword = "duction";
        when(crewRepository.countAllByNameOrIntroductionOrArea(keyword)).thenReturn(count);

        ///when
        Long result = crewService.countAllByKeyword(keyword);

        //then
        assertThat(result).isSameAs(count);
        verify(crewRepository, times(1)).countAllByNameOrIntroductionOrArea(keyword);
    }

    @DisplayName("키워드로 크루 찾기 첫 페이지 테스트")
    @Test
    public void findByKeywordTest1(@Mock DongArea dongArea) {
        //given
        String keyword = "duction";
        PageRequest pageRequest = PageRequest.of(0, 7);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        Slice<Crew> crewSlice = new SliceImpl<>(crews, pageRequest, true);
        when(crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword)).thenReturn(crewSlice);

        ///when
        Slice<Crew> result = crewService.findByKeyword(pageRequest, keyword);

        //then
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        verify(crewRepository, times(1)).findByNameOrIntroductionOrArea(pageRequest, keyword);
    }

    @DisplayName("키워드로 크루 찾기 중간 페이지 테스트")
    @Test
    public void findByKeywordTest2(@Mock DongArea dongArea) {
        //given
        String keyword = "duction";
        PageRequest pageRequest = PageRequest.of(1, 7);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        Slice<Crew> crewSlice = new SliceImpl<>(crews, pageRequest, true);
        when(crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword)).thenReturn(crewSlice);

        ///when
        Slice<Crew> result = crewService.findByKeyword(pageRequest, keyword);

        //then
        assertThat(result.getNumber()).isSameAs(1);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isFalse();
        verify(crewRepository, times(1)).findByNameOrIntroductionOrArea(pageRequest, keyword);
    }

    @DisplayName("키워드로 크루 찾기 마지막 페이지 테스트")
    @Test
    public void findByKeywordTest3(@Mock DongArea dongArea) {
        //given
        String keyword = "duction";
        PageRequest pageRequest = PageRequest.of(2, 7);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        Slice<Crew> crewSlice = new SliceImpl<>(crews, pageRequest, false);
        when(crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword)).thenReturn(crewSlice);

        ///when
        Slice<Crew> result = crewService.findByKeyword(pageRequest, keyword);

        //then
        assertThat(result.getNumber()).isSameAs(2);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(5);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isTrue();
        verify(crewRepository, times(1)).findByNameOrIntroductionOrArea(pageRequest, keyword);
    }

    @DisplayName("동 id 로 랜덤한 크루 maxSize 개 가져오기 테스트")
    @Test
    public void findRandomByDongAreaIdTest(@Mock GuArea guArea) {
        //given
        Long dongId = 1L;
        int maxSize = 10;
        DongArea dongArea = new DongArea(dongId, "dong1", guArea);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        when(crewRepository.findRandomByDongAreaId(dongId, maxSize)).thenReturn(crews);

        ///when
        List<Crew> crewList = crewService.findRandomByDongArea(dongArea, maxSize);

        //then
        assertThat(crewList.size()).isLessThanOrEqualTo(maxSize);
        for (Crew crew : crewList) {
            assertThat(crew.getDongArea().getId()).isSameAs(dongId);
        }
        verify(crewRepository, times(1)).findRandomByDongAreaId(dongId, maxSize);
    }

    @DisplayName("구 id 로 랜덤한 크루 maxSize 개 가져오기 테스트")
    @Test
    public void findRandomByGuAreaTest(@Mock SidoArea sidoArea) {
        //given
        Long guId = 1L;
        int maxSize = 10;
        GuArea guArea = new GuArea(guId, "gu1", sidoArea);
        DongArea dongArea = new DongArea(2L, "dong1", guArea);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        when(crewRepository.findRandomByGuAreaId(guId, maxSize)).thenReturn(crews);

        ///when
        List<Crew> crewList = crewService.findRandomByGuArea(guArea, maxSize);

        //then
        assertThat(crewList.size()).isLessThanOrEqualTo(maxSize);
        for (Crew crew : crewList) {
            assertThat(crew.getDongArea().getGuArea().getId()).isSameAs(guId);
        }
        verify(crewRepository, times(1)).findRandomByGuAreaId(guId, maxSize);
    }

    @DisplayName("유저가 속한 모든 크루 찾기 테스트")
    @Test
    public void findAllByUserTest(@Mock User user, @Mock DongArea dongArea) {
        //given
        List<Crew> crews = new ArrayList<>();
        for (int i = 50; i < 60; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl")
                    .dongArea(dongArea)
                    .build();
        }
        when(crewRepository.findAllByUser(user)).thenReturn(crews);

        ///when
        List<Crew> crewList = crewService.findAllByUser(user);

        //then
        assertThat(crewList.size()).isSameAs(crews.size());
        verify(crewRepository, times(1)).findAllByUser(user);
    }

    @DisplayName("입력받은 이름을 가진 크루 존재함 테스트")
    @Test
    public void existsByNameTest1() {
        //given
        String name = "crew";
        when(crewRepository.existsByName(name)).thenReturn(true);

        ///when
        boolean result = crewService.existsByName(name);

        //then
        assertThat(result).isTrue();
        verify(crewRepository, times(1)).existsByName(name);
    }

    @DisplayName("입력받은 이름을 가진 크루 존재 안함 테스트")
    @Test
    public void existsByNameTest2() {
        //given
        String name = "crew";
        when(crewRepository.existsByName(name)).thenReturn(false);

        ///when
        boolean result = crewService.existsByName(name);

        //then
        assertThat(result).isFalse();
        verify(crewRepository, times(1)).existsByName(name);
    }

}