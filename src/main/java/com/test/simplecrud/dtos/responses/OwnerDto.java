package com.test.simplecrud.dtos.responses;

import com.test.simplecrud.entities.User;

import java.util.UUID;

public record OwnerDto(
        UUID id,
        String name,
        String email,
        String phone
) {
    public OwnerDto(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

}
