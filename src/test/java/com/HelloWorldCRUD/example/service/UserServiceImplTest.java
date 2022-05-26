package com.HelloWorldCRUD.example.service;

import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.entity.User;
import com.HelloWorldCRUD.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserDto userDto;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder().
                id(1L).firstName("S").
                lastName("P").
                email("sp64@gmail.com").
                mobileNo("01518721828").
                isActive(true).
                build();

        user2 = User.builder().
                id(2L).firstName("H").
                lastName("R").
                email("hr64@gmail.com").
                mobileNo("01518721828").
                isActive(false).
                build();
    }

    // user - post
    @Test
    @DisplayName("User successfully created.")
    void whenUserIsValid_thenUserShouldSaveSuccessfully() {
        when(repository.findByEmail(any())).thenReturn(null);
        when(repository.save(any())).thenReturn(user1);

        User saved_user = service.saveUser(user1);
        assertThat(saved_user).isEqualTo(user1);
    }


//    @Test
//    void whenUserExistingEmailIsGiven_thenShouldThrowsException(){
//        given(repository.findByEmail(user.getEmail())).willReturn(user);
//
//        assertThrows(EntityNotFoundException.class, () -> {service.saveUser(user);});
//updateUser
//        verify(repository, never()).save(any(User.class));
//    }

    @Test
    @DisplayName("User not successfully created.")
    void whenUserExistingEmailIsGiven_thenNoUserShouldSave(){
        when(repository.findByEmail(any())).thenReturn(user1);
        User saved_user = service.saveUser(user1);
        assertThat(saved_user).isNull();
    }

    // user - get
    @Test
    @DisplayName("User are successfully found.")
    void whenUsersAreFound_thenUsersShouldReturn() {
        when(repository.findAll(Sort.by(Sort.Direction.ASC, "firstName"))).thenReturn(List.of(user1, user2));
        List<User> users = service.getUsers();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.containsAll(List.of(user1, user2))).isTrue();
    }

    @Test
    @DisplayName("User aren't successfully found.")
    void whenUsersAreNotFound_thenEmptyShouldReturn() {
        when(repository.findAll(Sort.by(Sort.Direction.ASC, "firstName"))).thenReturn(List.of());
        List<User> users = service.getUsers();
        assertThat(users).isNull();
    }

    // user - get by id
    @Test
    @DisplayName("User is successfully found.")
    void whenUserIdIsValid_thenUsersShouldReturn() {
        long user_id = 1L;
        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        User filtered_user= service.getUserById(user_id);
        assertThat(filtered_user.getId()).isEqualTo(user_id);
        assertThat(filtered_user).isEqualTo(user1);
    }

    @Test
    @DisplayName("User isn't successfully found.")
    void whenUserIdIsNotValid_thenNullsShouldReturn() {
        long user_id = 1L;
        when(repository.findById(user1.getId())).thenReturn(Optional.empty());
        User filtered_user= service.getUserById(user_id);
        assertThat(filtered_user).isNull();
    }

    // user - get by first name
    @Test
    @DisplayName("User successfully found by first name.")
    void whenFirstNameIsValid_thenUserShouldReturn() {
        String user_first_name = "S";
        when(repository.findByFirstName(user1.getFirstName())).thenReturn(List.of(user1));

        List<User> users = service.getUserByFirstName(user_first_name);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.contains(user1)).isTrue();
    }

    @Test
    @DisplayName("User isn't successfully created by first name.")
    void whenFirstNameIsNotValid_thenNullShouldReturn() {
        String user_first_name = "P";
        when(repository.findByFirstName(any())).thenReturn(List.of());

        List<User> users = service.getUserByFirstName(user_first_name);
        assertThat(users).isNull();
    }

    @Test
    @DisplayName("User is successfully found by last name.")
    void whenLastNameIsValid_thenUserShouldReturn() {
        String user_last_name = "P";
        when(repository.findByLastName(user1.getLastName())).thenReturn(List.of(user1));

        List<User> users = service.getUserByLastName(user_last_name);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.contains(user1)).isTrue();
    }

    @Test
    @DisplayName("User isn't successfully found by last name.")
    void whenLastNameIsNotValid_thenNullShouldReturn() {
        String user_last_name = "S";
        when(repository.findByLastName(any())).thenReturn(List.of());

        List<User> users = service.getUserByLastName(user_last_name);
        assertThat(users).isNull();
    }

    @Test
    @DisplayName("User is successfully found by email.")
    void whenEmailIsValid_thenUserShouldReturn() {
        String user_email = "sp64@gmail.com";
        when(repository.findByEmail(any())).thenReturn(user1);

        User user = service.getUserByEmail(user_email);
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(user1);
    }

    @Test
    @DisplayName("User isn't successfully found by email.")
    void whenEmailIsNotValid_thenNullShouldReturn() {
        String user_email = "sp64@gmail.com";
        when(repository.findByEmail(any())).thenReturn(null);

        User user = service.getUserByEmail(user_email);
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("User is successfully deleted.")
    void whenUserIdIsValid_thenUserShouldBeDeleted() {
        long user_id = 1L;
        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        doNothing().when(repository).delete(any());
        User filtered_user= service.deleteUser(user_id);
        assertThat(filtered_user.getId()).isEqualTo(user_id);
        assertThat(filtered_user).isEqualTo(user1);
    }

    @Test@DisplayName("User isn't successfully deleted.")

    void whenUserIdIsNotValid_thenNoUserShouldBeDeleted() {
        long user_id = 1L;
        when(repository.findById(user1.getId())).thenReturn(Optional.empty());
        User filtered_user= service.deleteUser(user_id);
        assertThat(filtered_user).isNull();
    }



    @Test
    @DisplayName("User is successfully updated.")
    void whenUserIdIsValid_thenUpdateUser() {
        when(repository.findById(any())).thenReturn(Optional.of(user1));
        when(repository.save(any())).thenReturn(user1);

        User updated_user = service.updateUser(user1, user1.getId());
        assertThat(updated_user).isNotNull();
        assertThat(updated_user).isEqualTo(user1);
    }

    @Test
    @DisplayName("User isn't successfully updated.")
    void whenUserIdIsNotValid_thenNotUpdateUser() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        User updated_user = service.updateUser(user1, user1.getId());
        assertThat(updated_user).isNull();
    }

    @Test
    @DisplayName("User is successfully deactivated.")
    void whenUserIdIsValid_thenDeactivateUser() {
        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(repository.save(any())).thenReturn(user1);

        User user = service.deactivateUser(user1.getId());
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(user1);
    }

    @Test
    @DisplayName("User doesn't successfully deactivated.")
    void whenUserIdIsNotValid_thenNotDeactivateUser() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        User updated_user = service.deactivateUser(user1.getId());
        assertThat(updated_user).isNull();
    }

    @Test@DisplayName("Active Users are successfully found.")

    void whenActiveUserFound_thenActiveUsersShouldReturn() {
        when(repository.findByIsActiveTrue()).thenReturn(List.of(user1));

        List<User> user = service.getActiveUsers();
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(List.of(user1));
    }

    @Test
    @DisplayName("No active user found")
    void whenNoActiveUserFound_thenNullShouldReturn() {
        when(repository.findByIsActiveTrue()).thenReturn(List.of());
        List<User> updated_user = service.getActiveUsers();
        assertThat(updated_user).isNull();
    }
}