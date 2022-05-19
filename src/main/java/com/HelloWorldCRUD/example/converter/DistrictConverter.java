package com.HelloWorldCRUD.example.converter;

import com.HelloWorldCRUD.example.dto.DistrictDto;
import com.HelloWorldCRUD.example.entity.District;
import com.HelloWorldCRUD.example.entity.HttpStatus;
import com.HelloWorldCRUD.example.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DistrictConverter {
    @Autowired
    District district;

    @Autowired
    DistrictDto districtDto;

    public District DistrictDtoToEntity(DistrictDto dto){
        district = new District();
        if(dto == null) return null;
        if(dto.getName() != null) district.setName(dto.getName());
        if(dto.getCode() > 0 && dto.getCode()< 65) district.setDist_code(dto.getCode());
        if(dto.getDivision() != null) district.setDivision(dto.getDivision());
        return district;
    }

    public List<District> DistrictDtoToEntity(List<DistrictDto> dtos){
        return dtos.stream().map(districtDto -> DistrictDtoToEntity(districtDto)).collect(Collectors.toList());
    }

    public DistrictDto DistrictEntityToDto(District district){
        DistrictDto districtDto = new DistrictDto();
        if(district == null) return null;
        if(district.getName() != null) districtDto.setName(district.getName());
        if(district.getDist_code() > 0 && district.getDist_code()< 65) districtDto.setCode(district.getDist_code());
        if(district.getDivision() != null) districtDto.setDivision(district.getDivision());
        return districtDto;
    }

    public List<DistrictDto> DistrictEntityToDto(List<District> districts){
        return districts.stream().map(district -> DistrictEntityToDto(district)).collect(Collectors.toList());
    }
}
