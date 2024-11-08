package com.test.simplecrud.controllers.users;

import com.test.simplecrud.dtos.responses.OwnerDto;
import com.test.simplecrud.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner")
@Slf4j
public class OwnerController {
    private final UserService userService;

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{page}/{size}")
    public Page<OwnerDto> getByFilter(@RequestParam(required = false) String filter,
                                      @PathVariable int page,
                                      @PathVariable int size) {
        return userService.findByFilter(filter, page, size);
    }
}
