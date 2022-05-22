package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                email("hr64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        userRepository.save(user2);
        assertEquals(userRepository.findAll().size(), 4);
        assertEquals(userRepository.findAll().containsAll(List.of(user1, user2)), true);
    }

    // get-user
    @Test
    @Sql("/db/user1.sql")
    void whenUserIsFound_thenUserShouldReturn(){
        Long userId = 1L;
        User user = userRepository.findById(userId).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getFirstName()).isEqualTo("S");
    }

    @Test
    @Sql("/db/user1.sql")
    void whenUserIsNotFound_thenUserShouldNotReturn(){
        Long userId = 3L;
        assertThat(userRepository.findById(userId)).isEmpty();
    }

    // get-all
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

    @Test
    @Sql("/db/user1.sql")
    void whenUserAreNotFound_thenUserShouldReturnNull (){
        userRepository.deleteAll();
        assertThat(userRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @Sql("/db/user1.sql")
    void whenEmailIsValid_thenUserShouldReturn() {
        String email = "sp64@gmail.com";
        User user = userRepository.findByEmail(email);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @Sql("/db/user1.sql")
    void whenEmailIsNotValid_thenUserShouldNotReturn() {
        String email = "hr674@gmail.com";
        User user = userRepository.findByEmail(email);

        assertThat(user).isNull();
    }

    @Test
    @Sql("/db/user1.sql")
    void whenFirstNameIsValid_thenUserShouldReturn() {
        String first_name = "S";
        List<User> users= userRepository.findByFirstName(first_name);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @Sql("/db/user1.sql")
    void whenFirstNameIsNotValid_thenUserShouldNotReturn() {
        String first_name = "P";

        List<User> users= userRepository.findByFirstName(first_name);

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    @Sql("/db/user1.sql")
    void whenLastNameIsValid_thenUserShouldReturn() {
        String last_name = "P";
        List<User> users= userRepository.findByLastName(last_name);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @Sql("/db/user1.sql")
    void whenLastNameIsNotValid_thenUserShouldNotReturn() {
        String last_name = "S";

        List<User> users= userRepository.findByLastName(last_name);

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    @Sql("/db/user1.sql")
    void whenActiveUsersAreFound_thenUsersShouldReturn() {
        List<User> users= userRepository.findByIsActiveTrue();

        assertThat(users.isEmpty()).isFalse();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @Sql("/db/user1.sql")
    void whenActiveUserIsNotFound_thenUsersShouldNotReturn() {
        userRepository.deleteAll();
        List<User> users= userRepository.findByIsActiveTrue();

        assertThat(users.isEmpty()).isTrue();
        assertThat(users.size()).isEqualTo(0);
    }
}