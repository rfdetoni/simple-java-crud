package com.test.simplecrud.services;

import com.test.simplecrud.dtos.requests.AuthDTO;
import com.test.simplecrud.dtos.requests.SignupDTO;
import com.test.simplecrud.dtos.responses.OwnerDto;
import com.test.simplecrud.dtos.responses.TokenDTO;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.enums.RoleName;
import com.test.simplecrud.exceptions.Errors;
import com.test.simplecrud.exceptions.NotAllowedException;
import com.test.simplecrud.exceptions.NotFoundException;
import com.test.simplecrud.repositories.UserRepository;
import com.test.simplecrud.specification.UserSpecification;
import com.test.simplecrud.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UserRepository repository;
    private final RoleService roleService;

    public UUID signup(SignupDTO dto) {
        validateifExistsByEmail(dto.email());
        var user = save( dto.toEntity() );
        createRole(user, RoleName.ROLE_ADMINISTRATOR);
        return user.getId();
    }

    public TokenDTO login(AuthDTO dto){
        var token = new UsernamePasswordAuthenticationToken( dto.username(), dto.password() );
        var auth = authenticationManager.authenticate( token );
        var jwtToken = tokenService
                .generateToken( (User) auth.getPrincipal()) ;

        return new TokenDTO( jwtToken );
    }

    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Errors.USER_NOT_FOUND));
    }

    public User getLoggedInUserData(){
        return repository.findByEmail( SessionUtils.getLoggedInUsername() ).orElseThrow(() -> new NotFoundException(Errors.USER_NOT_FOUND));
    }

    public Page<OwnerDto> findByFilter(String filter, int page, int size) {
        var spec = UserSpecification.filter(filter);
        var result = findAllBySpecAndPageable(spec, PageRequest.of( page, size ) );
        return result.map(OwnerDto::new);
    }

    private void createRole(User user, RoleName roleName){
        roleService.createRole(user, roleName);
    }

    private void validateifExistsByEmail(String email){
        var exists = repository.existsByEmail(email);
        if(exists) throw new NotAllowedException(Errors.USER_ALREADY_REGISTERED);
    }

    private User save(User user){
        return repository.save(user);
    }

    private Page<User> findAllBySpecAndPageable(Specification<User> spec, PageRequest pageable) {
        return repository.findAll(spec, pageable);
    }


}
