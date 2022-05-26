package com.HelloWorldCRUD.example.converter;

import com.HelloWorldCRUD.example.dto.DistrictDto;
import com.HelloWorldCRUD.example.entity.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DistrictConverter {
    @Autowired
    District district;

    @Autowired
    DistrictDto districtDto;

    public District DistrictDtoToEntity(DistrictDto dto){
        if(dto == null) return null;
        district = new District();
        if(dto.getName() != null) district.setName(dto.getName());
        if(dto.getCode() > 0 && dto.getCode()< 65) district.setDist_code(dto.getCode());
        if(dto.getDivision() != null) district.setDivision(dto.getDivision());
        return district;
    }

    public List<District> DistrictDtoToEntity(List<DistrictDto> dtos){
        if(dtos.isEmpty()) return null;
        return dtos.stream().map(districtDto -> DistrictDtoToEntity(districtDto)).collect(Collectors.toList());
    }

    public DistrictDto DistrictEntityToDto(District district){
        if(district == null) return null;
        DistrictDto districtDto = new DistrictDto();
        if(district.getName() != null) districtDto.setName(district.getName());
        if(district.getDist_code() > 0 && district.getDist_code()< 65) districtDto.setCode(district.getDist_code());
        if(district.getDivision() != null) districtDto.setDivision(district.getDivision());
        return districtDto;
    }

    public List<DistrictDto> DistrictEntityToDto(List<District> districts){
        if(districts.isEmpty()) return null;
        return districts.stream().map(district -> DistrictEntityToDto(district)).collect(Collectors.toList());
    }
}
