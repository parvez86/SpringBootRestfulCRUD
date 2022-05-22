package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void setUp() {
        User user1 = User.builder().
                id(1L).firstName("S").
                lastName("P").
                email("sp64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        userRepository.save(user1);
    }

    // Post-user
    @Test
    void whenUserIsValid_thenUserShouldPersist(){

        User user1 = User.builder().
                id(1L).firstName("S").
                lastName("P").
                email("sp64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        assertEquals(userRepository.findAll().size(), 1);
        assertEquals(userRepository.findAll().contains(user1), true);

        User user2 = User.builder().
                id(2L).firstName("H").
                lastName("R").
                email("hr64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        userRepository.save(user2);
        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(userRepository.findAll().containsAll(List.of(user1, user2)), true);
    }

    // get-user
    @Test
    void whenUserIsFound_thenUserShouldReturn(){
        Long userId = 1L;
        assertThat(userRepository.findById(userId)).isNotNull();
        assertThat(userRepository.findById(userId).get().getId()).isEqualTo(userId);
    }

    @Test
    void whenUserIsNotFound_thenUserShouldNotReturn(){
        Long userId = 2L;
        assertThat(userRepository.findById(userId)).isEmpty();
    }

    // get-all
    @Test
    void whenUsersAreFound_thenUsersShouldReturn(){
        Long userId = 1L;
        assertThat(userRepository.findAll()).isNotNull();


        assertThat(userRepository.findById(userId).get().getId()).isEqualTo(userId);

        User user2 = User.builder().
                id(2L).firstName("H").
                lastName("R").
                email("hr64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        userRepository.save(user2);
        assertEquals(userRepository.findAll().size(),2);
        assertThat(userRepository.findAll().contains(user2)).isTrue();
    }

    @Test
    void whenUserAreNotFound_thenUserShouldReturnNull (){
        userRepository.deleteAll();
        assertThat(userRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    void whenEmailIsValid_thenUserShouldReturn() {
        String email = "sp64@gmail.com";
        User user = userRepository.findByEmail(email);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    void whenEmailIsNotValid_thenUserShouldNotReturn() {
        String email = "hr64@gmail.com";
        User user = userRepository.findByEmail(email);

        assertThat(user).isNull();
    }

    @Test
    void whenFirstNameIsValid_thenUserShouldReturn() {
        String first_name = "S";
        List<User> users= userRepository.findByFirstName(first_name);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void whenFirstNameIsNotValid_thenUserShouldNotReturn() {
        String first_name = "P";

        List<User> users= userRepository.findByFirstName(first_name);

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    void whenLastNameIsValid_thenUserShouldReturn() {
        String last_name = "P";
        List<User> users= userRepository.findByLastName(last_name);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void whenLastNameIsNotValid_thenUserShouldNotReturn() {
        String last_name = "S";

        List<User> users= userRepository.findByLastName(last_name);

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    void whenActiveUsersAreFound_thenUsersShouldReturn() {
        List<User> users= userRepository.findByIsActiveTrue();

        assertThat(users.isEmpty()).isFalse();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void whenActiveUserIsNotFound_thenUsersShouldNotReturn() {
        userRepository.deleteAll();
        User user2 = User.builder().
                id(2L).firstName("H").
                lastName("R").
                email("hr64@gmail.com").
                mobileNo("01518721828").
                isActive(false).
                build();

        userRepository.save(user2);
        List<User> users= userRepository.findByIsActiveTrue();

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }
}