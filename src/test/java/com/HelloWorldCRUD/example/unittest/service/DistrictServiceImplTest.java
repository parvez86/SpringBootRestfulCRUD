package com.HelloWorldCRUD.example.unittest.service;

import com.HelloWorldCRUD.example.entity.District;
import com.HelloWorldCRUD.example.repository.DistrictRepository;
import com.HelloWorldCRUD.example.service.DistrictServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DistrictServiceImplTest {

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private DistrictServiceImpl districtService;

    private District district1, district2;

    @BeforeEach
    void setUp() {
        district1 = District.builder().
                id(1).
                name("Sirajganj").
                dist_code(15).
                division("Rajshahi").
                build();

        district2 = District.builder().
                id(1).
                name("Narail").
                dist_code(22).
                division("Zashore").
                build();
    }


    // district - post method test
    @Test
    @DisplayName("District successfully created.")
    void whenDistrictIsValid_thenDistrictShouldBeSaved() {
        when(districtRepository.save(any())).thenReturn(district1);
        District district = districtService.saveDistrict(district1);

        assertThat(district).isNotNull();
        assertThat(district).isEqualTo(district1);

    }


    @DisplayName("District not successfully created.")
    @Test
    void whenDistrictIsNotValid_thenDistrictShouldNotBeSaved() {
        when(districtRepository.save(any())).thenReturn(null);
        District district = districtService.saveDistrict(district1);

        assertThat(district).isNull();

    }

    // district - get method test
    @Test
    @DisplayName("Districts are successfully found.")
    void whenDistrictsAreValid_thenDistrictsShouldReturn() {
        when(districtRepository.findAll(Sort.by(Sort.Direction.ASC, "division"))).thenReturn(List.of(district1, district2));

        List<District> districts = districtService.getDistricts();
        assertThat(districts).isNotNull();
        assertThat(districts.size()).isEqualTo(2);
        assertThat(districts.containsAll(List.of(district1,district2))).isTrue();
    }

    @Test
    @DisplayName("Districts aren't successfully found.")
    void whenDistrictsAreNotValid_thenDistrictsShouldNotReturn() {
        when(districtRepository.findAll(Sort.by(Sort.Direction.ASC, "division"))).thenReturn(List.of());

        List<District> districts = districtService.getDistricts();
        assertThat(districts.isEmpty()).isTrue();
    }

    // district - get by id method test
    @Test
    @DisplayName("District is successfully found.")
    void whenDistrictIdIsValid_thenDistrictShouldReturn() {
        when(districtRepository.findById(any())).thenReturn(Optional.of(district1));

        District district = districtService.getDistrict(district1.getId());
        assertThat(district).isNotNull();
        assertThat(district).isEqualTo(district1);
    }

    @Test
    @DisplayName("District isn't successfully sound.")
    void whenDistrictIdIsNotValid_thenDistrictShouldNotReturn() {
        when(districtRepository.findById(any())).thenReturn(Optional.empty());

        District district = districtService.getDistrict(district1.getId());
        assertThat(district).isNull();
    }

    // district - update method test
    @Test
    @DisplayName("District is successfully updated.")
    void whenDistrictIdIsValid_thenUpdateDistrict() {
        when(districtRepository.findById(any())).thenReturn(Optional.of(district1));
        when(districtRepository.save(any())).thenReturn(null);  // not reachable for this case

        District district = districtService.updateDistrict(district1, district1.getId());
        assertThat(district).isNotNull();
        assertThat(district).isEqualTo(district1);
    }

    @Test
    @DisplayName("District doesn't successfully updated.")
    void whenDistrictIdIsNotValid_thenShouldNotUpdateDistrict() {
        when(districtRepository.findById(any())).thenReturn(Optional.empty());
//        when(districtRepository.save(any())).thenReturn(null);  // not reachable for this case

        District district = districtService.updateDistrict(district1, district1.getId());
        assertThat(district).isNull();
    }

    // district- delete method test
    @Test
    @DisplayName("District is successfully deleted.")
    void whenDistrictIdIsValid_thenDeleteDistrict() {
        when(districtRepository.findById(any())).thenReturn(Optional.of(district1));
        doNothing().when(districtRepository).delete(any());

        District district = districtService.deleteDistrict(district1.getId());
        assertThat(district).isNotNull();
        assertThat(district).isEqualTo(district1);
    }

    @Test
    @DisplayName("District doesn't successfully created.")
    void whenDistrictIdIsNotValid_thenShouldNotDeleteDistrict() {
        when(districtRepository.findById(any())).thenReturn(Optional.empty());

        District district = districtService.deleteDistrict(district1.getId());
        assertThat(district).isNull();
    }
}