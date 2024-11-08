package com.test.simplecrud.controllers.users;

import com.test.simplecrud.dtos.requests.AuthDTO;
import com.test.simplecrud.dtos.responses.TokenDTO;
import com.test.simplecrud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody AuthDTO data ){
       return ResponseEntity.ok( userService.login( data ) );
    }

}
