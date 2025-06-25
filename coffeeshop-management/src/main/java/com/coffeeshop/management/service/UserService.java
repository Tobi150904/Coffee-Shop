package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.UserDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Integer id) throws ResourceNotFoundException;
    UserDTO updateUser(Integer id, UserDTO userDTO) throws ResourceNotFoundException;
    void deleteUser(Integer id) throws ResourceNotFoundException;
    UserDTO getUserByUsername(String username) throws ResourceNotFoundException;
    List<UserDTO> getUsersByRole(String role);
}