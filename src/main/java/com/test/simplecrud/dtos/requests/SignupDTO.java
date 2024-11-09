package com.test.simplecrud.dtos.requests;

import com.test.simplecrud.constants.ValidatorMessages;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.utils.CryptoUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupDTO(
        @NotNull @NotBlank(message = ValidatorMessages.NAME_CANNOT_BE_EMPTY)
        String name,
        @NotNull @NotBlank(message = ValidatorMessages.MAIL_CANNOT_BE_EMPTY)
        String email,
        @NotNull @NotBlank(message = ValidatorMessages.PASSWORD_CANNOT_BE_EMPTY)
        String password,
        @NotNull @NotBlank(message = ValidatorMessages.PHONE_CANNOT_BE_EMPTY)
        String phone) {

    public User toEntity(){
        return User.builder()
                .email( email )
                .name( name )
                .phone( phone )
                .password( CryptoUtils.encryptPassword( password ) )
                .enable(true)
                .build();

    }
}
