package com.HelloWorldCRUD.example.integrationtest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper= new ObjectMapper();
    private UserDto userDto1, userDto2, userDto3;
    @BeforeEach
    void setUp() {

    }

    @Test
    void saveUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void getUserByFirstName() {
    }

    @Test
    void getUserByLastName() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void deactivateUser() {
    }

    @Test
    void getActiveUser() {
    }
}