package com.HelloWorldCRUD.example.unittest.controller;

import com.HelloWorldCRUD.example.controller.DistrictController;
import com.HelloWorldCRUD.example.converter.ApiResponseConverter;
import com.HelloWorldCRUD.example.converter.DistrictConverter;
import com.HelloWorldCRUD.example.dto.DistrictDto;
import com.HelloWorldCRUD.example.entity.District;
import com.HelloWorldCRUD.example.entity.HttpStatus;
import com.HelloWorldCRUD.example.service.DistrictServiceImpl;
import com.HelloWorldCRUD.example.util.ApiResponse;
import com.HelloWorldCRUD.example.util.ApiResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class DistrictControllerTest {

    @Mock
    private DistrictConverter districtConverter;

    @Mock
    private ApiResponseConverter apiResponseConverter;

    @Mock
    private DistrictServiceImpl districtService;

    @Mock
    private ApiResponseMessage responseMessage;

    @InjectMocks
    private DistrictController districtController;


    private District district_entity1, district_entity2;

    private DistrictDto districtDto1, districtDto2;

    private ApiResponse successApiResponse1, successApiResponse2, successEntitiesApiResponse, failApiResponse1, failApiResponse2, failEntitiesApiResponse;

    private ResponseEntity<ApiResponse> successEntityResponse1, successEntityResponse2, successEntitiesResponse, failEntityResponse1, failEntityResponse2, failEntitiesResponse;

    private List<District> district_entities;
    private List<DistrictDto> district_dtos;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ResultActions resultActions;


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(districtController).build();
        // given
        district_entity1 = District.builder().
                id(1).
                name("Sirajganj").
                dist_code(15).
                division("Rajshahi").
                build();

        district_entity2 = District.builder().
                id(2).
                name("Narail").
                dist_code(22).
                division("Zashore").
                build();


        district_entities = List.of(district_entity1, district_entity2);

        districtDto1 = new DistrictDto();
        districtDto1.setName("Sirajganj");
        districtDto1.setCode(15);
        districtDto1.setDivision("Rajshahi");

        districtDto2 = new DistrictDto();
        districtDto2.setName("Narail");
        districtDto2.setCode(22);
        districtDto2.setDivision("Zashore");

        district_dtos = List.of(districtDto1, districtDto2);
    }

    // district - post
    @Test
    @DisplayName("District successfully saved.")
    void whenDistrictIsValid_theSaveDistrict() throws Exception {

        // given
        String RESPONSE_MESSAGE = "District successfully created.";
        successApiResponse1 = new ApiResponse();
        successApiResponse1.setApiResponse(RESPONSE_MESSAGE, districtDto1, HttpStatus.SUCCESS);

        successEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(successApiResponse1);

        // when
        when(districtConverter.DistrictDtoToEntity(districtDto1)).thenReturn(district_entity1);
        when(districtService.saveDistrict(district_entity1)).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(districtDto1);
        when(apiResponseConverter.DtoToResponse(districtDto1, responseMessage.successfully_created("District"),responseMessage.not_created("District"))).thenReturn(successEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.saveDistrict(districtDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(successEntityResponse1);


        resultActions = mockMvc.perform(post("/districts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(districtDto1)));

        resultActions.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.name", is(districtDto1.getName())))
                .andExpect(jsonPath("$.object.code", is(districtDto1.getCode())))
                .andExpect(jsonPath("$.object.division", is(districtDto1.getDivision())))
                .andExpect(jsonPath("$.status", is(successApiResponse1.getStatus().toString())))
                ;

    }

    @Test
    @DisplayName("District not successfully saved.")
    void whenDistrictIsNotValid_thenNotSaveDistrict() throws Exception {

        // given
        String RESPONSE_MESSAGE = "District successfully not created.";
        failApiResponse1 = new ApiResponse();
        failApiResponse1.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        failEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(failApiResponse1);


        // when
        when(districtConverter.DistrictDtoToEntity(districtDto1)).thenReturn(district_entity1);
//        when(districtConverter.DistrictDtoToEntity(districtDto)).thenReturn(null);
        when(districtService.saveDistrict(district_entity1)).thenReturn(district_entity1);
//        when(districtService.saveDistrict(any())).thenReturn(district_entity1);
//        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(districtDto);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_created("District"),responseMessage.not_created("District"))).thenReturn(failEntityResponse1);


        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.saveDistrict(districtDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(failEntityResponse1);

        resultActions = mockMvc.perform(post("/districts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(districtDto1)));

        resultActions.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(failApiResponse1.getStatus().toString())))
        ;
    }

    // district - get
    @Test
    @DisplayName("Districts are successfully found.")
    void whenDistrictsAreFound_thenReturnDistricts() throws Exception {
        // given
        String RESPONSE_MESSAGE = "Districts are successfully found.";
        successEntitiesApiResponse = new ApiResponse();
        successEntitiesApiResponse.setApiResponse(RESPONSE_MESSAGE, district_dtos, HttpStatus.SUCCESS);
        successEntitiesResponse = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(successEntitiesApiResponse);

        // when
        when(districtService.getDistricts()).thenReturn(district_entities);
        when(districtConverter.DistrictEntityToDto(district_entities)).thenReturn(district_dtos);
        when(apiResponseConverter.DtoToResponse(district_dtos, responseMessage.successfully_found("Districts"), responseMessage.not_found("Districts"))).thenReturn(successEntitiesResponse);


        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.getDistricts();
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(successEntitiesResponse);

        resultActions = mockMvc.perform(get("/districts/"));

        resultActions.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.size()", is(district_entities.size())))
                .andExpect(jsonPath("$.object[0].name", is(district_entity1.getName())))
                .andExpect(jsonPath("$.object[1].name", is(district_entity2.getName())))
                .andExpect(jsonPath("$.status", is(successEntitiesApiResponse.getStatus().toString())))
        ;

    }

    @Test
    @DisplayName("Districts aren't successfully found.")
    void whenDistrictsAreNotFound_thenReturnNull() throws Exception {
        // given
        String RESPONSE_MESSAGE = "District aren't successfully not found.";
        failEntitiesApiResponse = new ApiResponse();
        failEntitiesApiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);
        failEntitiesResponse = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(failEntitiesApiResponse);

        // when
        when(districtService.getDistricts()).thenReturn(List.of());
        when(districtConverter.DistrictEntityToDto(List.of())).thenReturn(List.of());
        when(apiResponseConverter.DtoToResponse(List.of(), responseMessage.successfully_found("Districts"), responseMessage.not_found("Districts"))).thenReturn(failEntitiesResponse);


        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.getDistricts();
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(failEntitiesResponse);

        resultActions = mockMvc.perform(get("/districts/"));

        resultActions.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(failEntitiesApiResponse.getStatus().toString())))
        ;
    }

    // district - get by id
    @Test
    @DisplayName("District is successfully found.")
    void whenDistrictIdIsValid_thenReturnDistrict() throws Exception {

        // given
        String RESPONSE_MESSAGE = "District is successfully found.";
        successApiResponse1 = new ApiResponse();
        successApiResponse1.setApiResponse(RESPONSE_MESSAGE, districtDto1, HttpStatus.SUCCESS);

        successEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(successApiResponse1);

        // when
        when(districtService.getDistrict(district_entity1.getId())).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(districtDto1);
        when(apiResponseConverter.DtoToResponse(districtDto1,responseMessage.successfully_found("District"), responseMessage.not_found("District"))).thenReturn(successEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.getDistrict(district_entity1.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(successEntityResponse1);

        resultActions = mockMvc.perform(get("/districts/{id}/", district_entity1.getId()));

        resultActions.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.name", is(district_entity1.getName())))
                .andExpect(jsonPath("$.status", is(successApiResponse1.getStatus().toString())))
        ;
    }

    @Test
    @DisplayName("District isn't successfully found.")
    void whenDistrictIdIsNotValid_thenReturnNull() throws Exception {

        // given

        String RESPONSE_MESSAGE = "District isn't successfully found.";
        failApiResponse1 = new ApiResponse();
        failApiResponse1.setApiResponse(RESPONSE_MESSAGE, districtDto1, HttpStatus.ERROR);

        failEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(failApiResponse1);

        // when
        when(districtService.getDistrict(district_entity1.getId())).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null,responseMessage.successfully_found("District"), responseMessage.not_found("District"))).thenReturn(failEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.getDistrict(district_entity1.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(failEntityResponse1);

        resultActions = mockMvc.perform(get("/districts/{id}", district_entity1.getId()));

        resultActions.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(failApiResponse1.getStatus().toString())))
        ;
    }

    // district - put
    @Test
    @DisplayName("District is successfully updated")
    void whenDistrictIdIsValid_thenUpdateDistrict() throws Exception {

        // given
        String RESPONSE_MESSAGE = "District is successfully updated.";
        successApiResponse1 = new ApiResponse();
        successApiResponse1.setApiResponse(RESPONSE_MESSAGE, districtDto1, HttpStatus.SUCCESS);

        successEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(successApiResponse1);

        // when
        when(districtConverter.DistrictDtoToEntity(districtDto1)).thenReturn(district_entity1);
        when(districtService.updateDistrict(district_entity1, district_entity1.getId())).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(districtDto1);
        when(apiResponseConverter.DtoToResponse(districtDto1,responseMessage.successfully_updated("District"), responseMessage.not_updated("District"))).thenReturn(successEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.updateDistrict(district_entity1.getId(), districtDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(successEntityResponse1);

        resultActions = mockMvc.perform(put("/districts/{id}/", district_entity1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(districtDto1)));

        resultActions.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.name", is(district_entity1.getName())))
                .andExpect(jsonPath("$.status", is(successApiResponse1.getStatus().toString())))
        ;
    }


    @Test
    @DisplayName("District isn't successfully updated.")
    void whenDistrictIdIsNotValid_thenNotUpdateDistrict() throws Exception {
        // given
        String RESPONSE_MESSAGE = "District isn't successfully updated.";
        failApiResponse1 = new ApiResponse();
        failApiResponse1.setApiResponse(RESPONSE_MESSAGE,districtDto1, HttpStatus.ERROR);

        failEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(failApiResponse1);

        // when
        when(districtConverter.DistrictDtoToEntity(districtDto1)).thenReturn(district_entity1);
        when(districtService.updateDistrict(district_entity1, district_entity1.getId())).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null,responseMessage.successfully_updated("District"), responseMessage.not_updated("District"))).thenReturn(failEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.updateDistrict(district_entity1.getId(), districtDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(failEntityResponse1);

        resultActions = mockMvc.perform(put("/districts/{id}", district_entity1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(districtDto1)));

        resultActions.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(failApiResponse1.getStatus().toString())))
        ;
    }

    // district - delete
    @Test
    @DisplayName("District is successfully deleted.")
    void whenDistrictIdIsValid_thenDeleteDistrict() throws Exception {
        // given
        String RESPONSE_MESSAGE = "District is successfully deleted.";
        successApiResponse1 = new ApiResponse();
        successApiResponse1.setApiResponse(RESPONSE_MESSAGE, districtDto1, HttpStatus.SUCCESS);

        successEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(successApiResponse1);

        // when
        when(districtService.deleteDistrict(district_entity1.getId())).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(districtDto1);
        when(apiResponseConverter.DtoToResponse(districtDto1,responseMessage.successfully_deleted("District"), responseMessage.not_deleted("District"))).thenReturn(successEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.deleteDistrict(district_entity1.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(successEntityResponse1);

        resultActions = mockMvc.perform(delete("/districts/{id}/", district_entity1.getId()));

        resultActions.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.name", is(district_entity1.getName())))
                .andExpect(jsonPath("$.status", is(successApiResponse1.getStatus().toString())))
        ;
    }

    @Test
    @DisplayName("District isn't successfully deleted.")
    void whenDistrictIdIsNotValid_thenNotDeleteDistrict() throws Exception {
        // given
        String RESPONSE_MESSAGE = "District isn't successfully deleted.";
        failApiResponse1 = new ApiResponse();
        failApiResponse1.setApiResponse(RESPONSE_MESSAGE, districtDto1, HttpStatus.ERROR);

        failEntityResponse1 = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(failApiResponse1);

        // when
        when(districtService.deleteDistrict(district_entity1.getId())).thenReturn(district_entity1);
        when(districtConverter.DistrictEntityToDto(district_entity1)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_deleted("District"), responseMessage.not_deleted("District"))).thenReturn(failEntityResponse1);

        // then
        ResponseEntity<ApiResponse> outputResponse = districtController.deleteDistrict(district_entity1.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(failEntityResponse1);

        resultActions = mockMvc.perform(delete("/districts/{id}", district_entity1.getId()));

        resultActions.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(failApiResponse1.getStatus().toString())))
        ;
    }
}