package com.javaboy.mani.mapper;

import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.model.User;

public class UserMapper {

    public static User toEntity(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setId(userDto.getId());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;

        return new UserDto(
            user.getId(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail()
        );
    }
}
