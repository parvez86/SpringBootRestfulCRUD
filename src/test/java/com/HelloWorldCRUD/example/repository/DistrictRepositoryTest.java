package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.District;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(SpringExtension.class)
@DataJpaTest
class DistrictRepositoryTest {

    @Autowired
    private DistrictRepository districtRepository;

    // get-district
    @Test
    @Sql("/db/district1.sql")
    void whenDistrictIdIsValid_thenDistrictShouldReturn(){
        int dist_id = 1;
        District district = districtRepository.findById(dist_id).orElse(null);
        assertThat(district).isNotNull();
        assertThat(district.getName()).isEqualTo("Sirajganj");
    }

    @Test
    @Sql("/db/district1.sql")
    void whenDistrictIdIsNotValid_thenDistrictShouldNotReturn(){
        int dist_id = 3;
        District district = districtRepository.findById(dist_id).orElse(null);
        assertThat(district).isNull();
    }

    // get-all
    @Test
    @Sql("/db/district1.sql")
    void whenDistrictsAreFound_thenDistrictsShouldReturn(){
        List<District> district = districtRepository.findAll();
        assertThat(district).isNotNull();
        assertThat(district.size()).isEqualTo(2);
    }

    @Test
    @Sql("/db/district1.sql")
    void whenDistrictIsNotFound_thenDistrictShouldNotReturn(){
        districtRepository.deleteAll();
        List<District> district = districtRepository.findAll();
        assertThat(district.isEmpty()).isTrue();
        assertThat(district.size()).isEqualTo(0);
    }
}