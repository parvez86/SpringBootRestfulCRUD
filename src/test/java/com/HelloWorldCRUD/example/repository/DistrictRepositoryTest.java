package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.District;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DistrictRepositoryTest {

    @Autowired
    private DistrictRepository districtRepository;

    @BeforeEach
    void setUp() {
        District district = District.builder().
                id(1).
                name("Sirajganj").
                dist_code(15).
                division("Rajshahi").
                build();
        districtRepository.save(district);
    }

    // get-district
    @Test
    void whenDistrictIdIsValid_thenDistrictShouldReturn(){
        int dist_id = 1;
        District district = districtRepository.findById(dist_id).orElse(null);
        assertThat(district).isNotNull();
        assertThat(district.getName()).isEqualTo("Sirajganj");
    }

    @Test
    void whenDistrictIdIsNotValid_thenDistrictShouldNotReturn(){
        int dist_id = 2;
        District district = districtRepository.findById(dist_id).orElse(null);
        assertThat(district).isNull();
    }

    // get-all
    @Test
    void whenDistrictsAreFound_thenDistrictsShouldReturn(){
        List<District> district = districtRepository.findAll();
        assertThat(district).isNotNull();
        assertThat(district.size()).isEqualTo(1);
    }

    @Test
    void whenDistrictIsNotFound_thenDistrictShouldNotReturn(){
        districtRepository.deleteAll();
        List<District> district = districtRepository.findAll();
        assertThat(district.isEmpty()).isTrue();
        assertThat(district.size()).isEqualTo(0);
    }
}