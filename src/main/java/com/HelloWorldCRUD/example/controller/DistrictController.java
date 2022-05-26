package com.HelloWorldCRUD.example.controller;


import com.HelloWorldCRUD.example.converter.ApiResponseConverter;
import com.HelloWorldCRUD.example.converter.DistrictConverter;
import com.HelloWorldCRUD.example.dto.DistrictDto;
import com.HelloWorldCRUD.example.service.DistrictServiceImpl;
import com.HelloWorldCRUD.example.util.ApiResponse;
import com.HelloWorldCRUD.example.util.ApiResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class DistrictController {
    @Autowired
    private DistrictServiceImpl service;

    @Autowired
    private DistrictConverter converter;

    @Autowired
    private ApiResponseConverter responseConverter;

    @Autowired
    private ApiResponseMessage responseMessage;

    @PostMapping("/districts/")
    public ResponseEntity<ApiResponse> saveDistrict(@Valid @RequestBody DistrictDto dto){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.saveDistrict(converter.DistrictDtoToEntity(dto))), responseMessage.successfully_created("District"), responseMessage.not_created("District"));
    }

    @GetMapping("/districts/")
    public ResponseEntity<ApiResponse> getDistricts(){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.getDistricts()),
                responseMessage.successfully_found("Districts"),
                responseMessage.not_found("District"));
    }

    @GetMapping("/districts/{id}")
    public ResponseEntity<ApiResponse> getDistrict(@PathVariable("id") int id){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.getDistrict(id)), responseMessage.successfully_found("District"), responseMessage.not_found("District"));
    }

    @PutMapping("/districts/{id}")
    public ResponseEntity<ApiResponse> updateDistrict(@PathVariable("id") int id,
                                      @Valid @RequestBody DistrictDto dto){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.updateDistrict(converter.DistrictDtoToEntity(dto), id)), responseMessage.successfully_updated("District"), responseMessage.not_updated("District"));
    }

    @DeleteMapping("/districts/{id}")
    public ResponseEntity<ApiResponse> deleteDistrict(@PathVariable ("id") int id){
        return responseConverter.DtoToResponse(converter.DistrictEntityToDto(service.deleteDistrict(id)), responseMessage.successfully_deleted("District"), responseMessage.not_deleted("District"));
    }
}

