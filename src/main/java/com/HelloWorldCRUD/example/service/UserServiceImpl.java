package com.HelloWorldCRUD.example.service;

import com.HelloWorldCRUD.example.entity.User;
import com.HelloWorldCRUD.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private User user;

    @Autowired
    private UserRepository repository;

    public User saveUser(User user){
        User new_user = new User();
        if(Objects.isNull(repository.findByEmail(user.getEmail())) ) {
            if (user.getId() > 0 ) new_user.setId(user.getId());
            if (user.getFirstName() != null) new_user.setFirstName(user.getFirstName());
            if (user.getLastName() != null) new_user.setLastName(user.getLastName());
            if (user.getEmail() != null) new_user.setEmail(user.getEmail());
            if (user.getMobileNo() != null) new_user.setMobileNo(user.getMobileNo());
            return repository.save(new_user);
        } else return null;

    }

    public List<User> saveUsers(List<User> users){
        List<User> new_users = new ArrayList<>();
        new_users.addAll(users);
        return repository.saveAll(new_users);
    }

    public List<User> getUsers(){
        return repository.findAll(Sort.by(Sort.Direction.ASC, "firstName"));
       // return repository.findAll();
    }

    public User getUserById(long id){
        return repository.findById(id).orElse(null);
    }

    public List<User> getUserByFirstName(String fname){
        List<User> users = repository.findByFirstName(fname);
        return (users.size()==0)?null:users;
    }
//
    public List<User> getUserByLastName(String lname){
        List<User> users = repository.findByLastName(lname);
        return (users.size()==0)?null:users;
    }

    public User getUserByEmail(String name){
        return repository.findByEmail(name);
    }

    @Override
    public User deleteUser(long id) {
        User temp_user = repository.findById(id).orElse(null);
        if(Objects.nonNull(temp_user)){
            repository.delete(temp_user);
        }
        return temp_user;
    }

    @Override
    public User updateUser(User user, long id) {
        User temp_user = repository.findById(id).orElse(null);
        if(temp_user != null){
            if(user.getFirstName() != null) temp_user.setFirstName(user.getFirstName());
            if(user.getLastName() != null) temp_user.setLastName(user.getLastName());
            if(user.getEmail() != null) temp_user.setEmail(user.getEmail());
            /*if(user.getMobileNo() != null) temp_user.setMobileNo(user.getMobileNo());*/
            return repository.save(temp_user);
        } else return temp_user;
    }


    public User deactivateUser(long id) {
        User user = repository.findById(id).orElse(null);
        if(user != null){
            /*user.setIsActive(false);*/
            repository.save(user);
        }
        return user;
    }

    @Override
    public List<User> getActiveUsers() {
        return repository.findByIsActiveTrue();
    }
}
