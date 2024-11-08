package com.test.simplecrud.controllers.users;

import com.test.simplecrud.dtos.requests.SignupDTO;
import com.test.simplecrud.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/signup")
public class SignupController {

    private final UserService svc;

    @PostMapping()
    public UUID signup( @Valid @RequestBody SignupDTO dto ){
        return svc.signup( dto );
    }
}
