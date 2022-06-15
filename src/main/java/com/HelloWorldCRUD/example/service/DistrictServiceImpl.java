package com.HelloWorldCRUD.example.service;

import com.HelloWorldCRUD.example.entity.District;
import com.HelloWorldCRUD.example.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DistrictServiceImpl implements DistrictService{

    @Autowired
    private DistrictRepository repository;

    @Override
    public District saveDistrict(District district) {
        District new_district = new District();
        if(Objects.isNull(district)) return null;
        if(district.getName() != null) new_district.setName(district.getName());
        if(district.getDist_code() > 0 && district.getDist_code()< 65) new_district.setDist_code(district.getDist_code());
        if(district.getDivision() != null) new_district.setDivision(district.getDivision());

        return repository.save(new_district);
    }

    @Override
    public List<District> getDistricts() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "division"));
    }

    @Override
    public District getDistrict(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public District updateDistrict(District district, int id) {
        District rep_dist = repository.findById(id).orElse(null);
        if(rep_dist != null){
            if(district.getName() != null) rep_dist.setName(district.getName());
            if(district.getDist_code() > 0 && district.getDist_code()< 65) rep_dist.setDist_code(district.getDist_code());
            if(district.getDivision() != null) rep_dist.setDivision(district.getDivision());
            repository.save(rep_dist);
        }
        return rep_dist;
    }

    @Override
    public District deleteDistrict(int id) {
        District district = repository.findById(id).orElse(null);
        if(district != null) repository.delete(district);
        return district;
    }
}
