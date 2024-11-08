package com.test.simplecrud.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.simplecrud.entities.model.BasicEntityData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "vehicles" )
@EntityListeners( AuditingEntityListener.class )
@JsonIgnoreProperties( { "hibernateLazyInitializer", "handler" } )
public class Vehicle extends BasicEntityData {
    @NotNull
    private String vehicleIdentifier;

    @NotNull
    private String model;

    @NotNull
    private String color;

    @NotNull
    private String manufacturer;

    @NotNull
    private Integer fabricationYear;

    @NotNull
    private Integer modelYear;

    private LocalDate acquisitionDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean sold;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

}
