package com.project.runningcrew.crew.controller;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.area.service.GuAreaService;
import com.project.runningcrew.crew.controller.CrewController;
import com.project.runningcrew.crew.dto.CreateCrewRequest;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exception.notFound.CrewNotFoundException;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = CrewController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CrewControllerTest {

    @MockBean
    private CrewService crewService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private DongAreaService dongAreaService;

    @MockBean
    private GuAreaService guAreaService;

    @MockBean
    private DongArea dongArea;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("크루 가져오기 성공 테스트")
    @Test
    public void getCrewTest1() throws Exception {
        //given
        Long crewId = 1L;
        String name = "name";
        String introduction = "introduction";
        String crewImgUrl = "crewImgUrl";
        String dongFullName = "서울시 동대문구 전농동";
        Long memberCount = 10L;
        Crew crew = Crew.builder()
                .id(crewId)
                .name(name)
                .introduction(introduction)
                .crewImgUrl(crewImgUrl)
                .dongArea(dongArea)
                .build();
        when(crewService.findById(crewId)).thenReturn(crew);
        when(memberService.countAllByCrew(crew)).thenReturn(memberCount);
        when(dongArea.getFullName()).thenReturn(dongFullName);

        ///when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/crews/%d", crewId)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(crewId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.introduction").value(introduction))
                .andExpect(jsonPath("$.crewImgUrl").value(crewImgUrl))
                .andExpect(jsonPath("$.dong").value(dongFullName))
                .andExpect(jsonPath("$.memberCount").value(memberCount));
    }

    @WithMockUser
    @DisplayName("크루 가져오기 성공 테스트")
    @Test
    public void getCrewTest2() throws Exception {
        //given
        Long crewId = 1L;
        when(crewService.findById(crewId)).thenThrow(CrewNotFoundException.class);

        ///when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/crews/%d", crewId)));

        //then
        resultActions.andExpect(status().isNotFound());
    }

//    @DisplayName("크루 생성하기 테스트")
//    @Test
//    public void createCrewTest(@Mock GuArea guArea, @Mock MultipartFile multipartFile) throws Exception{
//        //given
//        Long dongId = 1L;
//        Long crewId = 2L;
//        DongArea dongArea = new DongArea(dongId, "전농동", guArea);
//        CreateCrewRequest createCrewRequest = new CreateCrewRequest("crew", "introduction",
//                dongId, multipartFile);
//        when(dongAreaService.findById(dongId)).thenReturn(dongArea);
//        when(crewService.saveCrew(any(), any(), multipartFile)).thenReturn(crewId);
//
//        ///when
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/crews")
//                        .content();
//
//        //then
//        resultActions.andExpect(status().isCreated())
//                .andExpect(header().string("Location", ""));
//    }

}