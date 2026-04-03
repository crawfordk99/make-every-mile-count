package model;

import java.util.List;

import entity.VehicleEntity;

public class VehicleDisplay {
    private List<VehicleEntity> vehicles;

    public VehicleDisplay(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }
    
    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }
}
