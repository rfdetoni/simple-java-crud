package com.test.simplecrud.services;

import com.test.simplecrud.dtos.requests.NewVehicleDTO;
import com.test.simplecrud.dtos.requests.UpdateVehicleDTO;
import com.test.simplecrud.dtos.responses.VehicleDto;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.entities.Vehicle;
import com.test.simplecrud.exceptions.BadRequestException;
import com.test.simplecrud.exceptions.NotAllowedException;
import com.test.simplecrud.exceptions.NotFoundException;
import com.test.simplecrud.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnVehicleId_WhenSuccessful() {
        NewVehicleDTO dto = mock(NewVehicleDTO.class);
        Vehicle vehicle = mock(Vehicle.class);
        User owner = mock(User.class);
        UUID vehicleId = UUID.randomUUID();

        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(dto.toEntity(owner)).thenReturn(vehicle);
        when(vehicle.getId()).thenReturn(vehicleId);
        when(repository.save(vehicle)).thenReturn(vehicle);

        ResponseEntity<UUID> response = vehicleService.create(dto);

        assertEquals(vehicleId, response.getBody());
        verify(repository).save(vehicle);
    }

    @Test
    void update_ShouldReturnUpdatedVehicle_WhenSuccessful() {
        UUID vehicleId = UUID.randomUUID();
        UpdateVehicleDTO dto = mock(UpdateVehicleDTO.class);
        Vehicle vehicle = mock(Vehicle.class);
        Vehicle updatedVehicle = mock(Vehicle.class);

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(repository.save(any(Vehicle.class))).thenReturn(updatedVehicle);
        User owner = mock(User.class);
        when(vehicle.getOwner()).thenReturn(owner);
        when(userService.getLoggedInUserData()).thenReturn(owner);
        ResponseEntity<VehicleDto> response = vehicleService.update(vehicleId, dto);

        assertNotNull(response.getBody());
        verify(repository).save(any(Vehicle.class));
    }

    @Test
    void delete_ShouldCallDeleteById_WhenSuccessful() {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = mock(Vehicle.class);
        User owner = mock(User.class);
        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(vehicle.getOwner()).thenReturn(owner);
        when(userService.getLoggedInUserData()).thenReturn(owner);

        vehicleService.delete(vehicleId);
        verify(repository).deleteById(vehicleId);
    }

    @Test
    void findAll_ShouldReturnPageOfVehicles() {
        int page = 0;
        int size = 5;
        VehicleDto filter = mock(VehicleDto.class);
        User owner = mock(User.class);
        Vehicle vehicle = mock(Vehicle.class);
        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle));

        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        ResponseEntity<Page<VehicleDto>> response = vehicleService.findAll(page, size, filter);

        assertNotNull(response.getBody());
        verify(repository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void getVehicle_ShouldReturnVehicle_WhenFound() {
        UUID vehicleId = UUID.randomUUID();
        User owner = mock(User.class);
        Vehicle vehicle = mock(Vehicle.class);

        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(repository.findByIdAndOwner(vehicleId, owner.getId())).thenReturn(Optional.of(vehicle));

        ResponseEntity<VehicleDto> response = vehicleService.getVehicle(vehicleId);

        assertNotNull(response.getBody());
        verify(repository).findByIdAndOwner(vehicleId, owner.getId());
    }

    @Test
    void getVehicle_ShouldThrowNotFoundException_WhenVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        User owner = mock(User.class);

        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(repository.findByIdAndOwner(vehicleId, owner.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.getVehicle(vehicleId));
    }

    @Test
    void create_ShouldThrowNotAllowedException_WhenVehicleAlreadyExists() {
        NewVehicleDTO dto = mock(NewVehicleDTO.class);
        User owner = mock(User.class);
        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(dto.vehicleIdentifier()).thenReturn("existingIdentifier");
        when(repository.existsVehicleByVehicleIdentifierAndOwnerAndIsSold("existingIdentifier", owner, false)).thenReturn(Boolean.valueOf(true));

        assertThrows(NotAllowedException.class, () -> vehicleService.create(dto));
        verify(repository, never()).save(any(Vehicle.class));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        UpdateVehicleDTO dto = mock(UpdateVehicleDTO.class);
        when(repository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.update(vehicleId, dto));
        verify(repository, never()).save(any(Vehicle.class));
    }

    @Test
    void update_ShouldThrowNotAllowedException_WhenUserIsNotOwner() {
        UUID vehicleId = UUID.randomUUID();
        UpdateVehicleDTO dto = mock(UpdateVehicleDTO.class);
        Vehicle vehicle = mock(Vehicle.class);
        User owner = mock(User.class);
        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(vehicle.getOwner()).thenReturn(new User());

        assertThrows(NotAllowedException.class, () -> vehicleService.update(vehicleId, dto));
        verify(repository, never()).save(any(Vehicle.class));
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        when(repository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.delete(vehicleId));
        verify(repository, never()).deleteById(vehicleId);
    }

    @Test
    void delete_ShouldThrowNotAllowedException_WhenUserIsNotOwner() {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = mock(Vehicle.class);
        User owner = mock(User.class);
        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(vehicle.getOwner()).thenReturn(new User());

        assertThrows(NotAllowedException.class, () -> vehicleService.delete(vehicleId));
        verify(repository, never()).deleteById(vehicleId);
    }

    @Test
    void findAll_ShouldHandleEmptyResults() {
        int page = 0;
        int size = 5;
        VehicleDto filter = mock(VehicleDto.class);
        User owner = mock(User.class);
        Page<Vehicle> emptyPage = new PageImpl<>(List.of());
        Specification<Vehicle> spec = mock(Specification.class);
        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(emptyPage);

        ResponseEntity<Page<VehicleDto>> response = vehicleService.findAll(page, size, filter);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getVehicle_ShouldThrowNotAllowedException_WhenUserIsNotOwner() {
        UUID vehicleId = UUID.randomUUID();
        User owner = mock(User.class);

        when(userService.getLoggedInUserData()).thenReturn(owner);
        when(repository.findByIdAndOwner(vehicleId, owner.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.getVehicle(vehicleId));
    }

    @Test
    void create_ShouldHandleNullDTO() {
        assertThrows(BadRequestException.class, () -> vehicleService.create(null));
    }

    @Test
    void update_ShouldHandleNullDTO() {
        UUID vehicleId = UUID.randomUUID();

        assertThrows(BadRequestException.class, () -> vehicleService.update(vehicleId, null));
    }

    @Test
    void findAll_ShouldHandleNegativePageAndSize() {
        VehicleDto filter = mock(VehicleDto.class);

        assertThrows(IllegalArgumentException.class, () -> vehicleService.findAll(-1, -1, filter));
    }
}



