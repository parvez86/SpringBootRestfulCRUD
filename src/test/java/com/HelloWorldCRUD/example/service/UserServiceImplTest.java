package com.HelloWorldCRUD.example.service;

import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.entity.User;
import com.HelloWorldCRUD.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.notIn;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserDto userDto;

    private User user;

//    @BeforeEach
//    void setUp() {
//        user = User.builder().
//                id(1L).firstName("S").
//                lastName("P").
//                email("sp64@gmail.com").
//                mobileNo("01518721828").
//                isActive(true).
//                build();
//    }

    @Test
    @Sql("user1.sql")
    void whenUserIsValid_thenUserShouldSaveSuccessfully() {
        User  user = User.builder().
                id(1L).firstName("S").
                lastName("P").
                email("sp64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();
//        given(repository.save(user)).willReturn(user);
        User saved_user = service.saveUser(user);
//        assertThat(saved_user).isNotNull();
        assertThat(saved_user).isEqualTo(user);
    }

//    @Test
//    void whenUserExistingEmailIsGiven_thenShouldThrowsException(){
//        given(repository.findByEmail(user.getEmail())).willReturn(user);
//
//        assertThrows(EntityNotFoundException.class, () -> {service.saveUser(user);});
//
//        verify(repository, never()).save(any(User.class));
//    }

    @Test
    void whenUserExistingEmailIsGiven_thenNoUserShouldSave(){
        given(repository.findByEmail(user.getEmail()) ).willReturn(user);

        if(repository.findByEmail(user.getEmail()) == null){
            repository.save(user);
        }

        assertThrows(EntityNotFoundException.class, () -> {service.saveUser(user);});

        verify(repository, never()).save(any(User.class));
    }

    @Test
    @Disabled
    void saveUsers() {
    }

    @Test
    @Disabled
    void getUsers() {
    }

    @Test
    @Disabled
    void getUserById() {
    }

    @Test
    @Disabled
    void getUserByFirstName() {
    }

    @Test
    @Disabled
    void getUserByLastName() {
    }

    @Test
    @Disabled
    void getUserByEmail() {
    }

    @Test
    @Disabled
    void deleteUser() {
    }

    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void deactivateUser() {
    }

    @Test
    @Disabled
    void getActiveUsers() {
    }
}