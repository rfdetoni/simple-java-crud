package com.test.simplecrud.repositories;

import com.test.simplecrud.entities.User;
import com.test.simplecrud.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {

    Page<Vehicle> findAll(Specification<Vehicle> spec, Pageable pageable);

    @Query("Select v from Vehicle v where v.id = :vehicleId and v.owner.id =:ownerId")
    Optional<Vehicle> findByIdAndOwner(UUID vehicleId, UUID ownerId);

    @Query("Select count(v) >0 from Vehicle v where v.vehicleIdentifier = :identifier and v.owner = :owner and v.sold =:isSold")
    boolean existsVehicleByVehicleIdentifierAndOwnerAndIsSold(String identifier, User owner, boolean isSold);


}
