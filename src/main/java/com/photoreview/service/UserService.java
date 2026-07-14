package com.photoreview.service;

import com.photoreview.dto.ProfileUpdateDto;
import com.photoreview.dto.UserRegistrationDto;
import com.photoreview.model.Role;
import com.photoreview.model.User;
import com.photoreview.repository.RoleRepository;
import com.photoreview.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("An account already exists with email: " + dto.getEmail());
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setDisplayName(dto.getDisplayName());
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateProfile(User user, ProfileUpdateDto dto) {
        user.setDisplayName(dto.getDisplayName());
        user.setBio(dto.getBio());

        // Process password updates if requested
        if (dto.getCurrentPassword() != null && !dto.getCurrentPassword().isEmpty()) {
            if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect.");
            }
            if (dto.getNewPassword() == null || dto.getNewPassword().length() < 6) {
                throw new IllegalArgumentException("New password must be at least 6 characters.");
            }
            if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                throw new IllegalArgumentException("New passwords do not match.");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        return userRepository.save(user);
    }

    public void changeUserStatus(Long userId, boolean enabled) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(enabled);
            userRepository.save(user);
        });
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
