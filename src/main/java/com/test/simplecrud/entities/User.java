package com.test.simplecrud.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.simplecrud.entities.model.BasicEntityData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "users" )
@EntityListeners( AuditingEntityListener.class )
@JsonIgnoreProperties( { "hibernateLazyInitializer", "handler" } )
public class User extends BasicEntityData  implements UserDetails {
    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    private String accessToken;

    @NotNull
    @Column(columnDefinition = "boolean default true")
    private boolean enable = true;

    private String permission;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Role> roles;

    private Instant lastAccess;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vehicle> vehicles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
