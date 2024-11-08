package com.test.simplecrud.dtos.requests;

import com.test.simplecrud.constants.ValidatorMessages;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.entities.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewVehicleDTO(
        @NotNull @NotBlank(message = ValidatorMessages.IDENTIFIER_CANNOT_BE_NULL) String vehicleIdentifier,
        @NotNull @NotBlank(message = ValidatorMessages.MODEL_CANNOT_BE_NULL) String model,
        @NotNull @NotBlank(message = ValidatorMessages.COLOR_CANNOT_BE_NULL) String color,
        @NotNull @NotBlank (message = ValidatorMessages.MANUFACTURER_CANNOT_BE_NULL)String manufacturer,
        @NotNull(message = ValidatorMessages.FABRICATION_YEAR_CANNOT_BE_NULL) int fabricationYear,
        @NotNull(message = ValidatorMessages.MODEL_YEAR_CANNOT_BE_NULL) int modelYear,
        LocalDate acquisitionDate
) {

    public Vehicle toEntity(User owner){
        return Vehicle.builder()
                .vehicleIdentifier(vehicleIdentifier)
                .model(model)
                .color(color)
                .manufacturer(manufacturer)
                .fabricationYear(fabricationYear)
                .modelYear(modelYear)
                .acquisitionDate(acquisitionDate)
                .owner( owner )
                .sold( false )
                .build();
    }
}
