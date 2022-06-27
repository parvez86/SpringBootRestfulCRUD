package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    District findByName(String name);
}

