package com.javaboy.mani.service.user;

import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.model.User;
import com.javaboy.mani.repository.UserRepository;
import com.javaboy.mani.request.CreateUserRequest;
import com.javaboy.mani.request.UserUpdateRequest;
import com.javaboy.mani.util.UserMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepo;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(req -> !userRepo.existsByEmail(req.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstname(req.getFirstname());
                    user.setLastname(req.getLastname());
                    user.setEmail(req.getEmail());
                    return userRepo.save(user);
                })
                .map(UserMapper::toDto) // Convert User to UserDto
                .orElseThrow(() -> new EntityExistsException("User with email " + request.getEmail() + " already exists."));
    }

    @Override
    public UserDto updateUser(UserUpdateRequest request, Long id) {
        return userRepo.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstname(request.getFirstname());
                    existingUser.setLastname(request.getLastname());
                    return userRepo.save(existingUser);
                })
                .map(UserMapper::toDto) // Convert User to UserDto
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepo.findById(id)
                .map(UserMapper::toDto) // Convert User to UserDto
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepo.findById(id)
                .ifPresentOrElse(userRepo::delete, () -> {
                    throw new EntityNotFoundException("User not found with id: " + id);
                });
    }
}