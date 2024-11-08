package com.test.simplecrud.dtos.requests;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateVehicleDTO(
        UUID newOwner,
        String color,
        String vehicleIdentifier,
        Boolean sold,
        LocalDate acquisitionDate) {
}
