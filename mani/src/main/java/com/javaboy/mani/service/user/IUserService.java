package com.javaboy.mani.service.user;

import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.model.User;
import com.javaboy.mani.request.CreateUserRequest;
import com.javaboy.mani.request.UserUpdateRequest;

public interface IUserService {
    UserDto createUser(CreateUserRequest request);
    UserDto updateUser(UserUpdateRequest request, Long id);
    UserDto getUserById(Long id);
    void deleteUserById(Long id);


}
