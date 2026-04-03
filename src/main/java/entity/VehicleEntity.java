package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity ownerId;

    public VehicleEntity() {
        // Default constructor for JPA
    }

    public VehicleEntity(String make, String model, String year, String subModel, double cityMpg, UserEntity ownerId) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.subModel = subModel;
        this.cityMpg = cityMpg;
        this.ownerId = ownerId;
    }   

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Override
    public String getMake() {
        return make;   
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getModel() {
        return model;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String getYear() {
        return year;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    @Override
    public String getSubModel() {
        return subModel;
    }

    public void setCityMpg(double cityMpg) {
        this.cityMpg = cityMpg;
    }

    @Override
    public double getCityMpg() {
        return cityMpg;
    }


    public void setOwnerId(UserEntity ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public UserEntity getOwnerId() {
        return ownerId;
    }

}
