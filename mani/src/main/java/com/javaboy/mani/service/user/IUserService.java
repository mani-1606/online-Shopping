package com.javaboy.mani.service.user;

import com.javaboy.mani.model.User;
import com.javaboy.mani.request.CreateUserRequest;
import com.javaboy.mani.request.UserUpdateRequest;

public interface IUserService {
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long id);
    User getUserById(Long id);
    void deleteUserById(Long id);

}
