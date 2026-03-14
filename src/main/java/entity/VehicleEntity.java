package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import model.Vehicle;

@Entity
@Table(name = "Vehicles")
public class VehicleEntity implements Vehicle{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false)
    private String year;

    private String subModel;

    @Column(nullable = false)
    private double cityMpg = 20.0;

    
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private UserEntity ownerId;

    public VehicleEntity() {
        // Default constructor for JPA
    }

    public VehicleEntity(String make, String model, String year, String subModel, double cityMpg, Long ownerId) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.subModel = subModel;
        this.cityMpg = cityMpg;
        this.ownerId = new UserEntity();
        this.ownerId.setUserId(ownerId);
    }   

    public Long getVehicleId() {
        return vehicleId;
    }

    @Override
    public String getMake() {
        return make;   
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String getSubModel() {
        return subModel;
    }

    @Override
    public double getCityMpg() {
        return cityMpg;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId.setUserId(ownerId);
    }

    @Override
    public Long getOwnerId() {
        return ownerId.getUserId();
    }

}
