package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.UserDTO;
import com.coffeeshop.management.model.User;
import com.coffeeshop.management.repository.UserRepository;
import com.coffeeshop.management.service.UserService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import com.coffeeshop.management.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validateUserDTO(userDTO);
        
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userDTO.getEmail() != null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userDTO.getPhoneNumber() != null && userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setRole(User.Role.valueOf(userDTO.getRole().toUpperCase()));
        user.setPasswordHash(PasswordUtil.encodePassword(userDTO.getPasswordHash()));
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) throws ResourceNotFoundException {
        validateUserDTO(userDTO);
        
        return userRepository.findById(id).map(existingUser -> {
            if (!existingUser.getUsername().equals(userDTO.getUsername())) {
                throw new IllegalArgumentException("Username cannot be changed");
            }
            
            // Check for duplicate email
            if (userDTO.getEmail() != null && !userDTO.getEmail().equals(existingUser.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            
            // Check for duplicate phone number
            if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().equals(existingUser.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
                throw new IllegalArgumentException("Phone number already exists");
            }
            
            BeanUtils.copyProperties(userDTO, existingUser, "userId", "createdAt", "passwordHash");
            
            // Only update password if a new one is provided
            if (userDTO.getPasswordHash() != null && !userDTO.getPasswordHash().isEmpty()) {
                existingUser.setPasswordHash(PasswordUtil.encodePassword(userDTO.getPasswordHash()));
            }
            
            existingUser.setRole(User.Role.valueOf(userDTO.getRole().toUpperCase()));
            
            User updatedUser = userRepository.save(existingUser);
            return convertToDto(updatedUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(Integer id) throws ResourceNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) throws ResourceNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByRole(String role) {
        return userRepository.findByRole(User.Role.valueOf(role.toUpperCase())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRole(user.getRole().name());
        return userDTO;
    }

    private void validateUserDTO(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (userDTO.getPasswordHash() == null || userDTO.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (userDTO.getFullName() == null || userDTO.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (userDTO.getRole() == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }
}