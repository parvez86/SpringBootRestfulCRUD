package com.HelloWorldCRUD.example.integrationtest.controller;
import com.HelloWorldCRUD.example.converter.DistrictConverter;
import com.HelloWorldCRUD.example.dto.DistrictDto;
import com.HelloWorldCRUD.example.entity.District;
import com.HelloWorldCRUD.example.repository.DistrictRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DistrictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DistrictConverter districtConverter;

    private final ObjectMapper objectMapper= new ObjectMapper();

    private DistrictDto districtDto1, districtDto2, districtDto3;

    @BeforeEach
    public void setup(){
        districtDto1 = new DistrictDto("Sirajganj", 15, "Rajshahi");
        districtDto2 = new DistrictDto("Narail", 22, "Zassore");
        districtDto3 = new DistrictDto("Bogura", 18, "Rajshahi");
    }
    @Test
    @DisplayName("District successfully saved.")
    void whenDistrictDtoIsValid_thenSaveDistrict() throws Exception {
        districtRepository.deleteAll();
        String RESPONSE_MESSAGE = "District successfully created.";

        mockMvc.perform(post("/districts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(districtDto3)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                ;
        District district = districtRepository.findByName("Bogura");
        assertThat(districtConverter.DistrictEntityToDto(district)).isEqualTo(districtDto3);
    }

    @Test
    @DisplayName("District not saved.")
    void whenDistrictDtoIsNotValid_thenNotSaveDistrict() throws Exception {
        districtRepository.deleteAll();
        String RESPONSE_MESSAGE = " entity validation error occurred.";
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));
        mockMvc.perform(post("/districts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(districtDto1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("District successfully found.")
    void whenDistrictIdIsValid_thenReturnDistrict() throws Exception {
        districtRepository.deleteAll();
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));

        int id = districtRepository.findByName(districtDto1.getName()).getId();
        String RESPONSE_MESSAGE = "District successfully found.";
        mockMvc.perform(get("/districts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        assertThat(districtRepository.findAll()).isNotNull();
        assertThat(districtRepository.findAll().get(0).getName()).isEqualTo(districtDto1.getName());
    }

    @Test
    @DisplayName("District not found.")
    void whenDistrictIdIsNotValid_thenNotReturnDistrict() throws Exception {
        districtRepository.deleteAll();
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));
        String RESPONSE_MESSAGE = "District not found.";
        mockMvc.perform(get("/districts/{id}", 2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        District district = districtRepository.findById(2).orElse(null);
        assertThat(districtConverter.DistrictEntityToDto(district)).isNull();
    }

    @Test
    @DisplayName("All Districts are successfully found.")
    void whenDistrictsAreFound_thenReturnAll() throws Exception {
        districtRepository.deleteAll();
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto2));
        String RESPONSE_MESSAGE = "Districts successfully found.";
        mockMvc.perform(get("/districts/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        assertThat(districtRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Districts are not found.")
    void whenDistrictsAreNotFound_thenReturnEmpty() throws Exception {
        districtRepository.deleteAll();
        String RESPONSE_MESSAGE = "District not found.";
        mockMvc.perform(get("/districts/"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        assertThat(districtRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("District successfully updated.")
    void whenDistrictIsValid_thenUpdateDistrict() throws Exception {
        districtRepository.deleteAll();
        String RESPONSE_MESSAGE = "District successfully updated.";
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));

        int id = districtRepository.findByName(districtDto1.getName()).getId();
        mockMvc.perform(put("/districts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(districtDto2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        District district = districtRepository.findById(id).orElse(null);
        assertThat(districtConverter.DistrictEntityToDto(district)).isEqualTo(districtDto2);
    }

    @Test
    @DisplayName("District not successfully updated.")
    void whenDistrictIsInvalid_thenUpdateDistrict() throws Exception {
        districtRepository.deleteAll();
        String RESPONSE_MESSAGE = "District not updated.";
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));
        int id = districtRepository.findByName(districtDto1.getName()).getId()+1;
        mockMvc.perform(put("/districts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(districtDto2)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        District district = districtRepository.findById(id).orElse(null);
        assertThat(districtConverter.DistrictEntityToDto(district)).isNull();
    }
    @Test
    @DisplayName("District successfully deleted.")
    void whenDistrictIdIsValid_thenDeleteDistrict() throws Exception {
        districtRepository.deleteAll();
        districtRepository.save(districtConverter.DistrictDtoToEntity(districtDto1));
        int id = districtRepository.findByName(districtDto1.getName()).getId();
        String RESPONSE_MESSAGE = "District successfully deleted.";
        mockMvc.perform(delete("/districts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        District district = districtRepository.findById(id).orElse(null);
        assertThat(districtConverter.DistrictEntityToDto(district)).isNull();
    }
    @Test
    @DisplayName("District not successfully deleted.")
    void whenDistrictIdIsNotValid_thenNotDeleteDistrict() throws Exception {
        districtRepository.deleteAll();
        String RESPONSE_MESSAGE = "District not deleted.";
//        int id = districtRepository.findByName(districtDto1.getName()).getId()+1;
        mockMvc.perform(delete("/districts/{id}", 1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        District district = districtRepository.findById(1).orElse(null);
        assertThat(districtConverter.DistrictEntityToDto(district)).isNull();
    }
}