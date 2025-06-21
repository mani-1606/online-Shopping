package com.javaboy.mani.controller;

import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.request.CreateUserRequest;
import com.javaboy.mani.request.UserUpdateRequest;
import com.javaboy.mani.response.ApiResponse;
import com.javaboy.mani.service.user.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user/{id}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("User retrieved successfully", userDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found with id: " +
                    id, e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            UserDto userDto = userService.createUser(request);
            return ResponseEntity.status(201).body(new ApiResponse("User created successfully", userDto));
        } catch (EntityExistsException e) {
            return ResponseEntity.status(409).body(new ApiResponse("User already exists", e.getMessage()));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long id) {
        try {
            UserDto userDto = userService.updateUser(request, id);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", userDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found with id: " +
                    id, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found with id: " +
                    id, e.getMessage()));
        }
    }
}