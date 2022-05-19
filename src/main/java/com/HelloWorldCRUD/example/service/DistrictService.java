package com.HelloWorldCRUD.example.service;

import com.HelloWorldCRUD.example.entity.District;

import java.util.List;

public interface DistrictService {
    District saveDistrict(District district);

    List<District> getDistricts();

    District getDistrict(int id);

    District updateDistrict(District district, int id);

    District deleteDistrict(int id);
}
