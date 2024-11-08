package com.test.simplecrud.repositories;

import com.test.simplecrud.entities.User;
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
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    Optional<User> findByEmail(String email );

    User getReferenceById(UUID id);

    @Query("select count(u) > 0 from User u where u.email =:email")
    boolean existsByEmail(String email);
}
