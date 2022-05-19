package com.HelloWorldCRUD.example.service;

import com.HelloWorldCRUD.example.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> saveUsers(List<User> users);
    List<User> getUsers();
    User getUserById(long  id);
    List<User> getUserByFirstName(String name);
    List<User> getUserByLastName(String name);
    User getUserByEmail(String email);
    User deleteUser(long id);
    User updateUser(User user, long id);
    User deactivateUser(long id);
    List<User> getActiveUsers();
}
