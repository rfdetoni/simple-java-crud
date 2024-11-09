package com.test.simplecrud.services;

import com.test.simplecrud.entities.Role;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.enums.RoleName;
import com.test.simplecrud.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository repository;

    public void createRole(User user, RoleName roleName) {
        var role = Role.builder()
                .name(roleName)
                .user(user)
                .build();
        save(role);

    }

    private Role save(Role role) {
        return repository.save(role);
    }

}
