package com.test.simplecrud.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.simplecrud.entities.model.BasicEntityData;
import com.test.simplecrud.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "roles" )
@EntityListeners( AuditingEntityListener.class )
@JsonIgnoreProperties( { "hibernateLazyInitializer", "handler" } )
public class Role extends BasicEntityData{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
