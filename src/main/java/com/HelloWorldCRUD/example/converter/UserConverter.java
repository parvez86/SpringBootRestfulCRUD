package com.HelloWorldCRUD.example.converter;

import com.HelloWorldCRUD.example.dto.UserDto;
import com.HelloWorldCRUD.example.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public UserDto UserEntityToDTO(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        return dto;
    }

    public List<UserDto> UserEntityToDTO(List<User> users) {
        return users.stream().map(user -> UserEntityToDTO(user)).collect(Collectors.toList());
    }

    public User UserDtoToEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public List<User> UserDtoToEntity(List<UserDto> dtos) {
        return dtos.stream().map(dto -> UserDtoToEntity(dto)).collect(Collectors.toList());
    }
}
