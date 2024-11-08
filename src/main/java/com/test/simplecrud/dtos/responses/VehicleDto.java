package com.test.simplecrud.dtos.responses;

import com.test.simplecrud.entities.Vehicle;

import java.time.LocalDate;
import java.util.UUID;

public record VehicleDto(
        UUID id,
        String vehicleIdentifier,
        String model,
        String color,
        String manufacturer,
        Integer fabricationYear,
        Integer modelYear,
        LocalDate acquisitionDate,
        Boolean sold
) {

    public VehicleDto(Vehicle vehicle) {
        this(vehicle.getId(),
                vehicle.getVehicleIdentifier(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getManufacturer(),
                vehicle.getFabricationYear(),
                vehicle.getModelYear(),
                vehicle.getAcquisitionDate(),
                vehicle.getSold());
    }
}