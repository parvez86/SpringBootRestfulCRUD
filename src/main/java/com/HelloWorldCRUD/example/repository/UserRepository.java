package com.HelloWorldCRUD.example.repository;

import com.HelloWorldCRUD.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAll();
    List<User> findByFirstName(String fname);
    List<User> findByLastName(String lname);
    List<User> findByIsActiveTrue();
}
