package com.test.simplecrud.controllers.vehicle;

import com.test.simplecrud.dtos.requests.NewVehicleDTO;
import com.test.simplecrud.dtos.requests.UpdateVehicleDTO;
import com.test.simplecrud.dtos.responses.VehicleDto;
import com.test.simplecrud.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService service;

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping()
    public ResponseEntity<UUID> newVehicle(@RequestBody @Valid NewVehicleDTO dto){
        return service.create(dto);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(@RequestBody @Valid UpdateVehicleDTO dto, @PathVariable UUID vehicleId){
        return service.update(vehicleId, dto);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID vehicleId){
        service.delete(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable UUID vehicleId){
        return service.getVehicle(vehicleId);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/all/{page}/{size}")
    public ResponseEntity<Page<VehicleDto>> getAllVehicles(@RequestBody(required = false) VehicleDto filter,
                                                           @PathVariable @Size(min= 0) int page,
                                                           @PathVariable @Size(min=1) int size){
        return service.findAll(page, size, filter);
    }

}
