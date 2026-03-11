package entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import model.User;


@Entity
@Table(name = "Users")
public class UserEntity implements User{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "ownerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleEntity> vehicles = new ArrayList<>();

    public UserEntity() {
        // Default constructor for JPA
    }

    public UserEntity(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        // this.createdAt = new java.util.Date();
    }

    @Override
    public void setUserId(Long userId) {
        this.user_id = userId;
    }

    public Long getUserId() {
        return user_id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    // @Override
    // public java.util.Date getCreatedAt() {
    //     return createdAt;
    // }

    

    
}
