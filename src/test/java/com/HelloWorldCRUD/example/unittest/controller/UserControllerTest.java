package com.HelloWorldCRUD.example.unittest.controller;

import com.HelloWorldCRUD.example.controller.UserController;
import com.HelloWorldCRUD.example.converter.ApiResponseConverter;
import com.HelloWorldCRUD.example.converter.UserConverter;
import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.entity.HttpStatus;
import com.HelloWorldCRUD.example.entity.User;
import com.HelloWorldCRUD.example.service.UserServiceImpl;
import com.HelloWorldCRUD.example.util.ApiResponse;
import com.HelloWorldCRUD.example.util.ApiResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private ApiResponseConverter apiResponseConverter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;


    @Mock
    @Autowired
    private ApiResponseMessage responseMessage;

    @Autowired
    private MockMvc mockMvc;

    private User user1, user2;
    private List<User> userList;
    private UserDto userDto1, userDto2;
    private List<UserDto> userDtoList;
    private ObjectMapper objectMapper = new ObjectMapper();


    private
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user1 = new User(1L, "S", "P", "sp86@gmail.com", "01518721828", true);
        user2 = new User(2L, "H", "R", "hr86@gmail.com", "01398721828", false);
        userList = List.of(user1, user2);

        userDto1 = new UserDto("S", "P", "sp86@gmail.com");
        userDto2 = new UserDto("H", "R", "hr86@gmail.com");
        userDtoList = List.of(userDto1, userDto2);
    }

    @DisplayName("User is successfully saved.")
    @Test
    void whenUserIsValid_thenSaveUser() throws Exception {
        String RESPONSE_MESSAGE = "User is successfully created.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDto1, HttpStatus.SUCCESS);
        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);


        when(userConverter.UserDtoToEntity(userDto1)).thenReturn(user1);
        when(userService.saveUser(user1)).thenReturn(user1);
        when(userConverter.UserEntityToDTO(user1)).thenReturn(userDto1);
        when(apiResponseConverter.DtoToResponse(userDto1, responseMessage.successfully_created("User"), responseMessage.not_created("User"))).thenReturn(responseEntity);


        ResponseEntity<ApiResponse> outputResponse = userController.saveUser(userDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(post("/users").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(userDto1)));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())));
        ;

    }

    @DisplayName("User isn't successfully saved")
    @Test
    void whenUserIsNotValid_thenShouldNotSaveUser() throws Exception {

        String RESPONSE_MESSAGE = "User isn't successfully created.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userConverter.UserDtoToEntity(userDto1)).thenReturn(user1);
        when(userService.saveUser(any())).thenReturn(null);
        when(userConverter.UserEntityToDTO((User) null)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_created("User"), responseMessage.not_created("User"))).thenReturn(responseEntity);


        ResponseEntity<ApiResponse> outputResponse = userController.saveUser(userDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(post("/users").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(userDto1)));

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())));
        ;

    }

    @DisplayName("Users are successfully found")
    @Test
    void whenUsersAreFound_thenReturnUsers() throws Exception {

        String RESPONSE_MESSAGE = "Users are successfully found.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDtoList, HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.getUsers()).thenReturn(userList);
        when(userConverter.UserEntityToDTO(userList)).thenReturn(userDtoList);
        when(apiResponseConverter.DtoToResponse(userDtoList, responseMessage.successfully_found("Users"), responseMessage.not_found("Users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUsers();
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users"));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.object[1].email", is(userDto2.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users aren't successfully found")
    @Test
    void whenUsersAreNotFound_thenReturnNull() throws Exception {

        String RESPONSE_MESSAGE = "Users aren't successfully found.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.getUsers()).thenReturn(List.of());
        when(userConverter.UserEntityToDTO(List.of())).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_found("Users"), responseMessage.not_found("Users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUsers();
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users"));

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }


    @DisplayName("User is successfully found")
    @Test
    void whenUserIdIsValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "User is successfully found.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDto1, HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.getUserById(user1.getId())).thenReturn(user1);
        when(userConverter.UserEntityToDTO(user1)).thenReturn(userDto1);
        when(apiResponseConverter.DtoToResponse(userDto1, responseMessage.successfully_found("User"), responseMessage.not_found("User"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserById(user1.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/{id}", user1.getId()));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User isn't successfully found")
    @Test
    void whenUsersIdIsNotValid_thenReturnNull() throws Exception {

        String RESPONSE_MESSAGE = "User isn't successfully found.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.getUserById(user2.getId())).thenReturn(null);
        when(userConverter.UserEntityToDTO((User) null)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_found("User"), responseMessage.not_found("User"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserById(user2.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/{id}", user2.getId()));

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users are successfully found by first name")
    @Test
    void whenUserFirstNameIsValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "Users are successfully found by first name.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, List.of(userDto1), HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.getUserByFirstName(user1.getFirstName())).thenReturn(List.of(user1));
        when(userConverter.UserEntityToDTO(List.of(user1))).thenReturn(List.of(userDto1));
        when(apiResponseConverter.DtoToResponse(List.of(userDto1),
                responseMessage.successfully_found("users"),
                responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserByFirstName(userDto1.getFirstName());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/fname")
                .param("fname",  userDto1.getFirstName())
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users aren't successfully found by first name")
    @Test
    void whenUserFirstNameIsNotValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "Users aren't successfully found by first name.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.getUserByFirstName(user1.getLastName())).thenReturn(List.of());
        when(userConverter.UserEntityToDTO(List.of())).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_found("users"), responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserByFirstName(userDto1.getLastName());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/fname")
                .param("fname",  userDto1.getFirstName())
        );

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users are successfully found by last name")
    @Test
    void whenUserLastNameIsValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "Users are successfully found by last name.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, List.of(userDto1), HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.getUserByLastName(user1.getLastName())).thenReturn(List.of(user1));
        when(userConverter.UserEntityToDTO(List.of(user1))).thenReturn(List.of(userDto1));
        when(apiResponseConverter.DtoToResponse(List.of(userDto1),
                responseMessage.successfully_found("users"),
                responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserByLastName(userDto1.getLastName());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/lname")
                .param("lname",  userDto1.getLastName())
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users aren't successfully found by last name")
    @Test
    void whenUserLastNameIsNotValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "Users aren't successfully found by last name.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.getUserByLastName(user1.getFirstName())).thenReturn(List.of());
        when(userConverter.UserEntityToDTO(List.of())).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_found("users"), responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserByLastName(userDto1.getFirstName());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/lname")
                .param("lname",  userDto1.getLastName())
        );

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User is successfully updated")
    @Test
    void whenUserIdIsValid_thenUpdateUser() throws Exception {
        String RESPONSE_MESSAGE = "User is successfully updated.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDto1, HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userConverter.UserDtoToEntity(userDto1)).thenReturn(user1);
        when(userService.updateUser(user1, user1.getId())).thenReturn(user1);
        when(userConverter.UserEntityToDTO(user1)).thenReturn(userDto1);
        when(apiResponseConverter.DtoToResponse(userDto1, responseMessage.successfully_updated("User"), responseMessage.not_updated("User"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.updateUser(user1.getId(), userDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(put("/users/{id}", user1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto1))
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User isn't successfully updated")
    @Test
    void whenUseridIsNotValid_thenNotUpdateUser() throws Exception {

        String RESPONSE_MESSAGE = "User isn't successfully updated.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userConverter.UserDtoToEntity(userDto1)).thenReturn(user1);
        when(userService.updateUser(user1, user1.getId())).thenReturn(null);
        when(userConverter.UserEntityToDTO((User) null)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_updated("User"), responseMessage.not_updated("User"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.updateUser(user1.getId(), userDto1);
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(put("/users/{id}", user2.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(userDto1))
        );

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users is successfully deleted")
    @Test
    void whenUserIdIsValid_thenDeleteUser() throws Exception {
        String RESPONSE_MESSAGE = "User is successfully deleted.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDto1, HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.deleteUser(user1.getId())).thenReturn(user1);
        when(userConverter.UserEntityToDTO(user1)).thenReturn(userDto1);
        when(apiResponseConverter.DtoToResponse(userDto1, responseMessage.successfully_deleted("User"), responseMessage.not_deleted("User"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.deleteUser(user1.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(delete("/users/{id}", user1.getId()));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User isn't successfully deleted")
    @Test
    void whenUsersIdIsNotValid_thenNotDeleteUser() throws Exception {

        String RESPONSE_MESSAGE = "User isn't successfully deleted.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.deleteUser(user2.getId())).thenReturn(null);
        when(userConverter.UserEntityToDTO((User) null)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_deleted("User"), responseMessage.not_deleted("User"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.deleteUser(user2.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(delete("/users/{id}", user2.getId()));

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Users is successfully found  by email")
    @Test
    void whenUserEmailIsValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "User is successfully found by email.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDto2, HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);
        when(userConverter.UserEntityToDTO(user2)).thenReturn(userDto2);
        when(apiResponseConverter.DtoToResponse(userDto2,
                responseMessage.successfully_found("users"),
                responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserByEmail(userDto2.getEmail());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/email")
                .param("email",  userDto2.getEmail())
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.email", is(userDto2.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User isn't successfully found by email")
    @Test
    void whenUserEmailIsNotValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "User isn't successfully found by email.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.getUserByEmail(user2.getEmail())).thenReturn(null);
        when(userConverter.UserEntityToDTO((User) null)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null, responseMessage.successfully_found("user"), responseMessage.not_found("user"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getUserByEmail(userDto2.getEmail());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/email")
                .param("email",  userDto1.getFirstName())
        );

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User is successfully deactivated")
    @Test
    void whenUserIdIsValid_thenDeactivateUser() throws Exception {
        String RESPONSE_MESSAGE = "User is successfully deactivated.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, userDto2, HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.deactivateUser(user2.getId())).thenReturn(user2);
        when(userConverter.UserEntityToDTO(user2)).thenReturn(userDto2);
        when(apiResponseConverter.DtoToResponse(userDto2,
                responseMessage.deactivated("user"),
                responseMessage.not_deactivated("user"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.deactivateUser(user2.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);
        ResultActions resultActions = mockMvc.perform(put("/users/deactivate/{id}", user2.getId())
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object.email", is(userDto2.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("User isn't successfully deactivated")
    @Test
    void whenUserIdIsNotValid_thenNotDeactivateUser() throws Exception {
        String RESPONSE_MESSAGE = "User isn't successfully deactivated.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.deactivateUser(user2.getId())).thenReturn(null);
        when(userConverter.UserEntityToDTO((User) null)).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null,
                responseMessage.deactivated("user"),
                responseMessage.not_deactivated("user"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.deactivateUser(user2.getId());
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);
        ResultActions resultActions = mockMvc.perform(put("/users/deactivate/{id}", user2.getId())
        );

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Active users are successfully found")
    @Test
    void whenActiveUsersAreFound_thenGetActiveUser() throws Exception {
        String RESPONSE_MESSAGE = "Active users are successfully found.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, List.of(userDto1), HttpStatus.SUCCESS);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(apiResponse);

        when(userService.getActiveUsers()).thenReturn(List.of(user1));
        when(userConverter.UserEntityToDTO(List.of(user1))).thenReturn(List.of(userDto1));
        when(apiResponseConverter.DtoToResponse(List.of(userDto1),
                responseMessage.successfully_found("users"),
                responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getActiveUser();
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/active/")
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.object[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }

    @DisplayName("Active users aren't successfully found")
    @Test
    void whenActiveUsersAreNotFound_thenReturnNull() throws Exception {
        String RESPONSE_MESSAGE = "Active users aren't successfully found.";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(RESPONSE_MESSAGE, null, HttpStatus.ERROR);

        ResponseEntity<ApiResponse> responseEntity = ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);

        when(userService.getActiveUsers()).thenReturn(List.of());
        when(userConverter.UserEntityToDTO(List.of())).thenReturn(null);
        when(apiResponseConverter.DtoToResponse(null,
                responseMessage.successfully_found("users"),
                responseMessage.not_found("users"))).thenReturn(responseEntity);

        ResponseEntity<ApiResponse> outputResponse = userController.getActiveUser();
        assertThat(outputResponse).isNotNull();
        assertThat(outputResponse).isEqualTo(responseEntity);

        ResultActions resultActions = mockMvc.perform(get("/users/active/")
        );

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
                .andExpect(jsonPath("$.status", is(apiResponse.getStatus().toString())))
        ;
    }
}