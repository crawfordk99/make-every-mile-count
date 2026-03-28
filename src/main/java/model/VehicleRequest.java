package model;

public class VehicleRequest {
    private String _make;
    private String _model;
    private String _year;
    private String _subModel;
    private String _region;
    private String _fuelType;
    private Long _vehicleId;

    public VehicleRequest() {}

    public VehicleRequest(String make, String model, String year, String subModel, String region, String fuelType) {
        this._make = make;
        this._model = model;
        this._year = year;
        this._subModel = subModel;
        this._region = region;
        this._fuelType = fuelType;
    }

    public String getMake() {
        return _make;
    }

    public void setMake(String make) {
        this._make = make;
    }

    public String getModel() {
        return _model;
    }

    public void setModel(String model) {
        this._model = model;
    }

    public String getYear() {
        return _year;
    }

    public void setYear(String year) {
        this._year = year;
    }

    public String getSubModel() {
        return _subModel;
    }

    public void setSubModel(String subModel) {
        this._subModel = subModel;
    }

    public String getRegion() {
        return _region;
    }

    public void setRegion(String region) {
        this._region = region;
    }

    public String getFuelType() {
        return _fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this._fuelType = fuelType;
    }

    public Long getVehicleId() {
        return _vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this._vehicleId = vehicleId;
    }

}
