package com.test.simplecrud.dtos.requests;

import com.test.simplecrud.entities.Role;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.enums.RoleName;
import com.test.simplecrud.utils.CryptoUtils;

import java.time.Instant;
import java.util.List;

public record SignupDTO(String name, String email, String password, String phone) {

    public User toEntity(){
        return User.builder()
                .email( email )
                .name( name )
                .phone( phone )
                .password( CryptoUtils.encryptPassword( password ) )
                .roles(List.of(Role.builder().name(RoleName.ROLE_ADMINISTRATOR).build()))
                .lastAccess( Instant.now() )
                .enable(true)
                .build();

    }
}
