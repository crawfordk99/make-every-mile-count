package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import entity.UserEntity;


/**
 * Data access object for User operations.
 * Handles creation and retrieval of user accounts.
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    
    UserEntity save(UserEntity userEntity);
    
}
