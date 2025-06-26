package com.coffeeshop.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String username;
    private String fullName;
    private String role;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}