package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }


    @Test
    @DisplayName("User successfully created")
    public void createUserSuccess(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("test1234");
        System.out.println(password);
        User newUser = new User(1L, "S", "P", "test@gmail.com", password, "10920129129", true);
        userRepository.save(newUser);
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    @Disabled
    void findByEmail() {
    }

    @Test
    @Disabled
    void findByFirstName() {
    }

    @Test
    @Disabled
    void findByLastName() {
    }

    @Test
    @Disabled
    void findByIsActiveTrue() {
    }
}