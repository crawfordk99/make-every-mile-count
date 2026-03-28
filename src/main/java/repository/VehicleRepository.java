package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.UserEntity;
import entity.VehicleEntity;

/**
 * Data access object for Vehicle operations.
 * Handles creation and retrieval of vehicles.
 */

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, String>{
    List<VehicleEntity> findAllByOwnerId(UserEntity owner);
    Optional<VehicleEntity> findByVehicleIdAndOwnerId(Long id, UserEntity owner);
}
