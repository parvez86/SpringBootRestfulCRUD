package com.HelloWorldCRUD.example.controller;

import com.HelloWorldCRUD.example.converter.ApiResponseConverter;
import com.HelloWorldCRUD.example.converter.UserConverter;
import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.service.UserServiceImpl;
import com.HelloWorldCRUD.example.util.response.ApiResponse;
import com.HelloWorldCRUD.example.util.response.ApiResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private UserConverter converter;

    @Autowired
    private ApiResponseConverter responseConverter;
    @Autowired
    private ApiResponseMessage responseMessage;

    @PostMapping("/users/")
//    public ResponseEntity<ApiResponse> saveUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){
    public ResponseEntity<ApiResponse> saveUser(@Valid @RequestBody UserDto userDto){
//        if(bindingResult.hasErrors()){
//            ApiResponse apiResponse = new ApiResponse();
//            apiResponse.setApiResponse(bindingResult.getFieldError().getField()+": "+bindingResult.getFieldError().getDefaultMessage(), userDto, HttpStatus.ERROR);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
//        }
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.saveUser(converter.UserDtoToEntity(userDto))), responseMessage.successfully_created("user"),responseMessage.not_created("user"));
    }
//
//    @PostMapping("/users")
//    public List<User> saveUsers(@RequestBody List<User> users){
//        return service.saveUsers(users);
//    }

    @GetMapping("/users/")
    public ResponseEntity<ApiResponse> getUsers(){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.getUsers()),responseMessage.successfully_found("users"),responseMessage.not_found("users"));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") long id){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.getUserById(id)),responseMessage.successfully_found("user"), responseMessage.not_found("user"));
    }

    @GetMapping("/users/email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email){
        System.out.println(email);
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.getUserByEmail(email)), responseMessage.successfully_found("user"), responseMessage.not_found("user"));
    }

    @GetMapping("/users/fname")
    public ResponseEntity<ApiResponse> getUserByFirstName(@RequestParam String fname){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.getUserByFirstName(fname)), responseMessage.successfully_found("users"), responseMessage.not_found("users"));
    }

    @GetMapping("/users/lname")
    public ResponseEntity<ApiResponse> getUserByLastName(@RequestParam String lname){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.getUserByLastName(lname)),responseMessage.successfully_found("users"), responseMessage.not_found("users"));
    }

    @PutMapping("users/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("id") long id,
                                  @Valid @RequestBody UserDto userDto){
    return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.updateUser(converter.UserDtoToEntity(userDto), id)), responseMessage.successfully_updated("users"), responseMessage.not_updated("users"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") long id){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.deleteUser(id)), responseMessage.successfully_deleted("users"), responseMessage.not_deleted("users"));
    }

    @PutMapping("/users/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateUser( @PathVariable("id") long id){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.deactivateUser(id)), responseMessage.deactivated("user"), responseMessage.not_deactivated("user"));
    }

    @GetMapping("users/active/")
    public ResponseEntity<ApiResponse> getActiveUser(){
        return responseConverter.DtoToResponse(converter.UserEntityToDTO(service.getActiveUsers()), responseMessage.successfully_found("users"), responseMessage.not_found("user"));
    }

}
