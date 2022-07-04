package com.HelloWorldCRUD.example.integrationtest.controller;

import com.HelloWorldCRUD.example.converter.UserConverter;
import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.entity.User;
import com.HelloWorldCRUD.example.repository.UserRepository;
import com.HelloWorldCRUD.example.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserServiceImpl userService;

    private final ObjectMapper objectMapper= new ObjectMapper();

    private UserDto userDto1, userDto2, userDto3;

    @BeforeEach
    void setUp() {
        userDto1 = new UserDto("S", "P", "sp86@gmail.com");
        userDto2 = new UserDto("H", "R", "hr86@gmail.com");
    }

    @Test
    @DisplayName("User successfully saved")
    void whenUserIdIsValid_thenSaveUser() throws Exception {
        String RESPONSE_MESSAGE = "user successfully created.";
        userRepository.deleteAll();
        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto1)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findByEmail(userDto1.getEmail());
        assertThat(user).isNotNull();
        assertThat(userConverter.UserEntityToDTO(user)).isEqualTo(userDto1);
    }

    @Test
    @DisplayName("User not saved")
    void whenUserIdIsNotValid_theNotSaveUser() throws Exception {
        String RESPONSE_MESSAGE = "user not created.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto1)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("Users are successfully found")
    void whenUsersAreFound_thenReturnUsers() throws Exception {
        String RESPONSE_MESSAGE = "users successfully found.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));
        userService.saveUser(userConverter.UserDtoToEntity(userDto2));

        mockMvc.perform(get("/users/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Users not found")
    void whenUserIsNotFound_thenReturnNull() throws Exception {
        String RESPONSE_MESSAGE = "users not found.";
        userRepository.deleteAll();

        mockMvc.perform(get("/users/"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }
    @Test
    @DisplayName("User successfully found")
    void whenUserIdIsValid_thenReturnUser() throws Exception {
        String RESPONSE_MESSAGE = "user successfully found.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        long id = userRepository.findAll().get(0).getId();
        mockMvc.perform(get("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findById(id).orElse(null);
        assertThat(user).isNotNull();
        assertThat(userConverter.UserEntityToDTO(user)).isEqualTo(userDto1);
    }

    @Test
    @DisplayName("User not found by email")
    void whenUserIdIsNotValid_thenReturnNull() throws Exception {
        userRepository.deleteAll();
        String RESPONSE_MESSAGE = "user not found.";

        mockMvc.perform(get("/users/{id}", 1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findById(1L).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("User successfully found by email")
    void whenUserEmailIsValid_thenReturnUser() throws Exception {
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        String RESPONSE_MESSAGE = "user successfully found.";
        mockMvc.perform(get("/users/email")
                        .param("email", userDto1.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("User not found by email")
    void whenUserEmailIsNotValid_thenReturnNull() throws Exception {
        userRepository.deleteAll();
        String RESPONSE_MESSAGE = "user not found.";
        mockMvc.perform(get("/users/email")
                        .param("email", userDto1.getEmail()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }
    @Test
    @DisplayName("Users successfully found by first name")
    void whenUserFirstNameIsValid_thenReturnUser() throws Exception {
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        String RESPONSE_MESSAGE = "users successfully found.";
        mockMvc.perform(get("/users/fname")
                        .param("fname", userDto1.getFirstName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("Users successfully found by first name")
    void whenUserFirstNameIsInvalid_thenReturnNull() throws Exception {
        userRepository.deleteAll();

        String RESPONSE_MESSAGE = "users not found.";
        mockMvc.perform(get("/users/fname")
                        .param("fname", userDto1.getFirstName()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("Users successfully found by last name")
    void whenUserLastNameIsValid_thenReturnUser() throws Exception {
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        String RESPONSE_MESSAGE = "users successfully found.";
        mockMvc.perform(get("/users/lname")
                        .param("lname", userDto1.getLastName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("Users not found by last name")
    void whenUserLastNameIsInvalid_thenReturnNull() throws Exception {
        userRepository.deleteAll();
        String RESPONSE_MESSAGE = "users not found.";

        mockMvc.perform(get("/users/lname")
                        .param("lname", userDto1.getLastName()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("User successfully updated")
    void whenUserIdIsValid_thenUpdateUser() throws Exception {
        userRepository.deleteAll();
        String RESPONSE_MESSAGE = "user successfully updated.";
        userRepository.save(userConverter.UserDtoToEntity(userDto1));

        long id = userRepository.findByEmail(userDto1.getEmail()).getId();
        mockMvc.perform(put("/users/{id}", id)
                        .content(objectMapper.writeValueAsString(userDto2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        assertThat(userRepository.findById(id).get().getEmail()).isEqualTo(userDto2.getEmail());
    }

    @Test
    @DisplayName("User not updated")
    void whenUserIdIsInvalid_thenNotUpdateUser() throws Exception {
        userRepository.deleteAll();
        String RESPONSE_MESSAGE = "user not updated.";

        long id = 1;
        mockMvc.perform(put("/users/{id}", id)
                        .content(objectMapper.writeValueAsString(userDto2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
        assertThat(userRepository.findById(id).orElse(null)).isNull();
    }

    @Test
    @DisplayName("User successfully deleted")
    void whenUserIdIsValid_thenDeleteUser() throws Exception {
        String RESPONSE_MESSAGE = "user successfully deleted.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        long id = userRepository.findByEmail(userDto1.getEmail()).getId();
        mockMvc.perform(delete("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findById(id).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("User not deleted")
    void whenUserIdIsInvalid_thenNotDeleteUser() throws Exception {
        String RESPONSE_MESSAGE = "user not deleted.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        long id = userRepository.findByEmail(userDto1.getEmail()).getId();
        mockMvc.perform(delete("/users/{id}", id+1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findById(id).orElse(null);
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("User successfully de-activated")
    void whenUserIdIsValid_thenDeactivateUser() throws Exception {
        String RESPONSE_MESSAGE = "user successfully deactivated.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        long id = userRepository.findAll().get(0).getId();
        mockMvc.perform(put("/users/deactivate/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findById(id).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("User not de-activated")
    void whenUserIdIsInvalid_thenNotDeactivateUser() throws Exception {
        String RESPONSE_MESSAGE = "user not deactivated.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        long id = userRepository.findAll().get(0).getId();
        mockMvc.perform(put("/users/deactivate/{id}", id+1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;

        User user = userRepository.findById(id).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("All active users are successfully found")
    void whenActiveUsersAreFound_thenReturnActiveUsers() throws Exception {
        String RESPONSE_MESSAGE = "users successfully found.";
        userRepository.deleteAll();
        userService.saveUser(userConverter.UserDtoToEntity(userDto1));

        mockMvc.perform(get("/users/active/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }

    @Test
    @DisplayName("Active users are not found")
    void whenNoActiveUsersAreFound_thenReturnEmpty() throws Exception {
        String RESPONSE_MESSAGE = "user not found.";
        userRepository.deleteAll();

        mockMvc.perform(get("/users/active/"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(RESPONSE_MESSAGE)))
        ;
    }
}