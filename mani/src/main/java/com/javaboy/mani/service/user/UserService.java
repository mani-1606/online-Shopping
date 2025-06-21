package com.javaboy.mani.service.user;

import com.javaboy.mani.model.User;
import com.javaboy.mani.repository.UserRepository;
import com.javaboy.mani.request.CreateUserRequest;
import com.javaboy.mani.request.UserUpdateRequest;
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
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepo.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstname(req.getFirstname());
                    user.setLastname(req.getLastname());
                    user.setEmail(req.getEmail());
                    return userRepo.save(user);
                }).orElseThrow(() -> new EntityExistsException("User with email " + request.getEmail() + " already exists.")
                );
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long id) {
        return userRepo.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstname(request.getFirstname());
                    existingUser.setLastname(request.getLastname());
                    return userRepo.save(existingUser);
                }).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id)
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
