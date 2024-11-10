package com.test.simplecrud.specification;

import com.test.simplecrud.dtos.responses.VehicleDto;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.entities.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class VehicleSpecification {

    private static final String LIKE = "%";

    public static Specification<Vehicle> buildVehicleFilter(VehicleDto filter, User owner) {
        if(isNull(filter)) {
            return Specification.where(ownerEquals(owner));
        }

        return Specification.where(ownerEquals(owner)
                .and(idEquals(filter.id()))
                .and(vehicleIdentifierLike(filter.vehicleIdentifier()))
                .and(modelLike(filter.model()))
                .and(colorLike(filter.color()))
                .and(manufacturerLike(filter.manufacturer()))
                .and(fabricationYearEquals(filter.fabricationYear()))
                .and(modelYearEquals(filter.modelYear()))
                .and(acquisitionDateEquals(filter.acquisitionDate()))
                .and(isSold(filter.sold()))
        );
    }

    private static String buildLike(String value){
        StringBuilder like = new StringBuilder(LIKE);
        like.append(value).append(LIKE);
        return like.toString();
    }

    private static Specification<Vehicle> idEquals(UUID id) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(id)) return null;
            return criteriaBuilder.equal(root.get("id"), id);
        });
    }

    private static Specification<Vehicle> vehicleIdentifierLike(String vehicleIdentifier) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(vehicleIdentifier)) return null;
            return criteriaBuilder.like(root.get("vehicleIdentifier"), buildLike(vehicleIdentifier));
        });
    }

    private static Specification<Vehicle> modelLike(String model) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(model)) return null;
            return criteriaBuilder.like(root.get("model"),buildLike(model));
        });
    }

    private static Specification<Vehicle> colorLike(String color) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(color)) return null;
            return criteriaBuilder.like(root.get("color"), buildLike(color));
        });
    }

    private static Specification<Vehicle> manufacturerLike(String manufacturer) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(manufacturer)) return null;
            return criteriaBuilder.like(root.get("manufacturer"), buildLike(manufacturer));
        });
    }

    private static Specification<Vehicle> fabricationYearEquals(Integer fabricationYear) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(fabricationYear)) return null;
            return criteriaBuilder.equal(root.get("fabricationYear"), fabricationYear);
        });
    }

    private static Specification<Vehicle> modelYearEquals(Integer modelYear) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(modelYear)) return null;
            return criteriaBuilder.equal(root.get("modelYear"), modelYear);
        });
    }

    private static Specification<Vehicle> acquisitionDateEquals(LocalDate acquisitionDate) {
        return ((root, query, criteriaBuilder) -> {
            if( isNull(acquisitionDate)) return null;
            return criteriaBuilder.equal(root.get("acquisitionDate"), acquisitionDate);
        });
    }

    private static Specification<Vehicle> isSold(Boolean isSold) {
        return ((root, query, criteriaBuilder) -> {
            if(isNull(isSold)) return null;
            return criteriaBuilder.equal(root.get("sold"), isSold);
        });
    }

    private static Specification<Vehicle> ownerEquals(User owner) {
        return (root, query, cb) -> cb.equal(root.get("owner"), owner);
    }

}
