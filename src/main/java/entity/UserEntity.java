package entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "Users")
public class UserEntity implements UserDetails {
    
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

    public void setUserId(Long userId) {
        this.user_id = userId;
    }

    public Long getUserId() {
        return user_id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    @Override
    public String getPassword() {
        return passwordHash;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Standard UserDetails overrides (return true for simplicity now)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // @Override
    // public java.util.Date getCreatedAt() {
    //     return createdAt;
    // }

    

    
}
