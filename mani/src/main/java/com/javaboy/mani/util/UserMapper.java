package com.javaboy.mani.util;

import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
    }
}
