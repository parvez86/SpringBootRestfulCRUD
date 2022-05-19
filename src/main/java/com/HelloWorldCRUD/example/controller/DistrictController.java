package com.HelloWorldCRUD.example.controller;


import com.HelloWorldCRUD.example.converter.ApiResponseConverter;
import com.HelloWorldCRUD.example.converter.DistrictConverter;
import com.HelloWorldCRUD.example.dto.DistrictDto;
import com.HelloWorldCRUD.example.entity.District;
import com.HelloWorldCRUD.example.entity.HttpStatus;
import com.HelloWorldCRUD.example.repository.DistrictRepository;
import com.HelloWorldCRUD.example.service.DistrictServiceImpl;
import com.HelloWorldCRUD.example.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DistrictController {
    @Autowired
    private District district;

    @Autowired
    private DistrictRepository repository;

    @Autowired
    private DistrictServiceImpl service;

    @Autowired
    private DistrictConverter converter;

    @Autowired
    private ApiResponseConverter responseConverter;

    @PostMapping("/districts/")
    public ResponseEntity<ApiResponse> saveDistrict(@Valid @RequestBody DistrictDto dto){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.saveDistrict(converter.DistrictDtoToEntity(dto))), "District is successfully saved!", "District isn't successfully saved");
    }

    @GetMapping("/districts/")
    public ResponseEntity<ApiResponse> getDistricts(){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.getDistricts()), "Districts are successfully found!", "District not found!");
    }

    @GetMapping("/districts/{id}")
    public ResponseEntity<ApiResponse> getDistrict(@PathVariable("id") int id){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.getDistrict(id)), "District is successfully found!", "District not found!");
    }

    @PutMapping("/districts/{id}")
    public ResponseEntity<ApiResponse> updateDistrict(@PathVariable("id") int id,
                                      @Valid @RequestBody DistrictDto dto){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.updateDistrict(converter.DistrictDtoToEntity(dto), id)), "District is successfully updated!", "District not found!");
    }

    @DeleteMapping("/districts/{id}")
    public ResponseEntity<ApiResponse> deleteDistrict(@PathVariable ("id") int id){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.deleteDistrict(id)), "District is successfully deleted!", "District not found!");
    }
}

