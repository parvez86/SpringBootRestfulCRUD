package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // Post-user
    @Test
    @Sql("/db/user1.sql")
    void whenUserIsValid_thenUserShouldPersist(){

        assertEquals(userRepository.findAll().size(), 2);

        User user1 = User.builder().
                id(3L).firstName("S").
                lastName("H").
                email("sp86@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        userRepository.save(user1);
        assertEquals(userRepository.findAll().size(), 3);
        assertEquals(userRepository.findAll().contains(user1), true);

        User user2 = User.builder().
                id(4L).firstName("H").
                lastName("R").
                email("hr645@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        userRepository.save(user2);
        assertEquals(userRepository.findAll().size(), 4);
        assertEquals(userRepository.findAll().containsAll(List.of(user1, user2)), true);
    }

    // get-user
    @DisplayName("Return user when user id is valid.")
    @Test
    @Sql("/db/user1.sql")
    void whenUserIsFound_thenUserShouldReturn(){
        Long userId = 1L;
        User user = userRepository.findById(userId).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getFirstName()).isEqualTo("S");
    }

    @DisplayName("Return null when user id is invalid. ")
    @Test
    @Sql("/db/user1.sql")
    void whenUserIsNotFound_thenUserShouldNotReturn(){
        Long userId = 3L;
        assertThat(userRepository.findById(userId)).isEmpty();
    }

    // get-all
    @DisplayName("Return all users successfully.")
    @Test
    @Sql("/db/user1.sql")
    void whenUsersAreFound_thenUsersShouldReturn(){
        User user2 = User.builder().
                id(2L).firstName("H").
                lastName("R").
                email("hr64@gmail.com").
                mobileNo("01922983288").
                isActive(false).
                build();

        assertEquals(userRepository.findAll().size(),2);
        assertThat(userRepository.findAll().contains(user2)).isTrue();
    }

    @DisplayName("Return empty when no user is found. ")
    @Test
    @Sql("/db/user1.sql")
    void whenUserAreNotFound_thenUserShouldReturnNull (){
        userRepository.deleteAll();
        assertThat(userRepository.findAll().isEmpty()).isTrue();
    }

    @DisplayName("Return user when user email is valid. ")
    @Test
    @Sql("/db/user1.sql")
    void whenEmailIsValid_thenUserShouldReturn() {
        String email = "sp64@gmail.com";
        User user = userRepository.findByEmail(email);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @DisplayName("Return user when user email is not valid. ")
    @Test
    @Sql("/db/user1.sql")
    void whenEmailIsNotValid_thenUserShouldNotReturn() {
        String email = "hr674@gmail.com";
        User user = userRepository.findByEmail(email);

        assertThat(user).isNull();
    }

    @DisplayName("Return User when user first name is valid.")
    @Test
    @Sql("/db/user1.sql")
    void whenFirstNameIsValid_thenUserShouldReturn() {
        String first_name = "S";
        List<User> users= userRepository.findByFirstName(first_name);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }


    @DisplayName("Return null when user first name is not valid.")
    @Test
    @Sql("/db/user1.sql")
    void whenFirstNameIsNotValid_thenUserShouldNotReturn() {
        String first_name = "P";

        List<User> users= userRepository.findByFirstName(first_name);

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }

    @DisplayName("Return User when user last name is valid.")
    @Test
    @Sql("/db/user1.sql")
    void whenLastNameIsValid_thenUserShouldReturn() {
        String last_name = "P";
        List<User> users= userRepository.findByLastName(last_name);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }

    @DisplayName("Return null when user last name is not valid.")
    @Test
    @Sql("/db/user1.sql")
    void whenLastNameIsNotValid_thenUserShouldNotReturn() {
        String last_name = "S";

        List<User> users= userRepository.findByLastName(last_name);

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }

    @DisplayName("Return all active users successfully.")
    @Test
    @Sql("/db/user1.sql")
    void whenActiveUsersAreFound_thenUsersShouldReturn() {
        List<User> users= userRepository.findByIsActiveTrue();

        assertThat(users.isEmpty()).isFalse();
        assertThat(users.size()).isEqualTo(1);
    }

    @DisplayName("Return null when no user is active.")
    @Test
    @Sql("/db/user1.sql")
    void whenActiveUserIsNotFound_thenUsersShouldNotReturn() {
        userRepository.deleteAll();
        List<User> users= userRepository.findByIsActiveTrue();

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }
}