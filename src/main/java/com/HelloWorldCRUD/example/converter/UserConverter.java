package com.HelloWorldCRUD.example.converter;

import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    @Autowired
    private User user;

    @Autowired
    private UserDto userDto;

    public UserDto UserEntityToDTO(User user) {
        if (user == null) return null;
        userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        return userDto;
    }

    public List<UserDto> UserEntityToDTO(List<User> users) {
        if(users==null) return null;
        return users.stream().map(user -> UserEntityToDTO(user)).collect(Collectors.toList());
    }

    public User UserDtoToEntity(UserDto dto) {
        if (dto == null) return null;
        user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public List<User> UserDtoToEntity(List<UserDto> dtos) {
        if(dtos.isEmpty()) return null;
        return dtos.stream().map(dto -> UserDtoToEntity(dto)).collect(Collectors.toList());
    }
}
