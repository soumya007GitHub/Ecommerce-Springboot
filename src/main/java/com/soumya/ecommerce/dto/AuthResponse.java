package com.soumya.ecommerce.dto;

import com.soumya.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UUID userId;

    private String fullName;

    private String email;

    private Role role;

    private String token;

    private String tokenType;

    private long expiresInMs;
}
