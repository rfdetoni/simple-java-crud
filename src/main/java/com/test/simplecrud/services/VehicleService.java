package com.test.simplecrud.services;

import com.test.simplecrud.dtos.requests.NewVehicleDTO;
import com.test.simplecrud.dtos.requests.UpdateVehicleDTO;
import com.test.simplecrud.dtos.responses.VehicleDto;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.entities.Vehicle;
import com.test.simplecrud.exceptions.Errors;
import com.test.simplecrud.exceptions.NotAllowedException;
import com.test.simplecrud.exceptions.NotFoundException;
import com.test.simplecrud.repositories.VehicleRepository;
import com.test.simplecrud.specification.VehicleSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {
    private final UserService userService;
    private final VehicleRepository repository;

    public ResponseEntity<UUID> create(NewVehicleDTO dto) {
        var owner = getOwner();
        validateIfExistsByIdentifierAndOwner(dto.vehicleIdentifier(), owner);
        var vehicle = dto.toEntity(owner);
        return ResponseEntity.ok(create(vehicle).getId());
    }

    public ResponseEntity<VehicleDto> update(UUID id, UpdateVehicleDTO dto) {
        var vehicle = findById(id);
        validateOwnership(vehicle);
        var updatedVehicle = save(mapToVehicle(vehicle, dto));
        return ResponseEntity.ok(new VehicleDto(updatedVehicle));
    }

    public void delete(UUID vehicleId) {
        var vehicle = findById(vehicleId);
        validateOwnership(vehicle);
        deleteById(vehicleId);
    }

    public ResponseEntity<Page<VehicleDto>> findAll(int page, int size, VehicleDto filter) {
        var spec = VehicleSpecification.buildVehicleFilter(filter, getOwner());
        var vehicles = findAllBySpecAndPageable(spec, PageRequest.of( page, size ) );
        return ResponseEntity.ok(vehicles.map(VehicleDto::new));
    }

    private Page<Vehicle> findAllBySpecAndPageable(Specification<Vehicle> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    public ResponseEntity<VehicleDto> getVehicle(UUID vehicleId) {
        var owner = getOwner();
        var vehicle = findByIdAndOwner(vehicleId, owner.getId());
        return ResponseEntity.ok(new VehicleDto(vehicle));
    }

    private Vehicle create(Vehicle vehicle) {
        return save(vehicle);
    }

    private Vehicle mapToVehicle(Vehicle vehicle, UpdateVehicleDTO dto) {
        if (!isNull(dto.color())) {
            vehicle.setColor(dto.color());
        }
        if (!isNull(dto.vehicleIdentifier())) {
            vehicle.setVehicleIdentifier(dto.vehicleIdentifier());
        }

        if (!isNull(dto.newOwner()) && !dto.newOwner().equals(vehicle.getOwner().getId())) {
            transferVehicle(vehicle, findOwnerById(dto.newOwner()));
            vehicle.setSold(true);
            return vehicle;
        }

        if (!isNull(dto.sold())) {
            vehicle.setSold(dto.sold());
        }

        return vehicle;
    }

    private void transferVehicle(Vehicle vehicle, User newOwner) {
        var newVehicle = Vehicle.builder()
                .vehicleIdentifier(vehicle.getVehicleIdentifier())
                .model(vehicle.getModel())
                .owner(newOwner)
                .color(vehicle.getColor())
                .sold(vehicle.getSold())
                .manufacturer(vehicle.getManufacturer())
                .sold( false )
                .fabricationYear(vehicle.getFabricationYear())
                .modelYear(vehicle.getModelYear())
                .build();

        create(newVehicle);
    }

    private User findOwnerById(UUID id) {
        return userService.findById(id);
    }

    private void validateIfExistsByIdentifierAndOwner(String identifier, User owner) {
        var exists = repository.existsVehicleByVehicleIdentifierAndOwnerAndIsSold(identifier, owner, false);
        if(exists) throw new NotAllowedException(Errors.VEHICLE_ALREADY_REGISTERED);
    }

    private void validateOwnership(Vehicle vehicle) {
        if( !vehicle.getOwner().equals(getOwner()) )
            throw new NotAllowedException(Errors.OWNERSHIP_VALIDATION_FAILED);
    }

    private Vehicle findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Errors.VEHICLE_NOT_FOUND));
    }

    private Vehicle save(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    private User getOwner(){
        return userService.getLoggedInUserData();
    }

    private void deleteById(UUID vehicleId) {
        repository.deleteById(vehicleId);
    }

    private Vehicle findByIdAndOwner(UUID vehicleId, UUID ownerId) {
        return repository.findByIdAndOwner(vehicleId, ownerId).orElseThrow(() -> new NotFoundException(Errors.VEHICLE_NOT_FOUND));
    }
}
