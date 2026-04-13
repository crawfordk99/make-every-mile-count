package model;

public class VehicleRequest {
    private String _make;
    private String _model;
    private String _year;
    private String _subModel;
    private String _region;
    private String _fuelType;
    private Long _vehicleId;
    private Double _manualMpg; // Optional field for when the caller already has the MPG and just wants to calculate cost-per-mile
    private Double _manualGasPrice; // Optional field for when the caller already has the gas price and just wants to calculate cost-per-mile

    public VehicleRequest() {}

    public VehicleRequest(String make, String model, String year, String subModel, String region, String fuelType, Long vehicleId, Double manualMpg, Double manualGasPrice) {
        this._make = make;
        this._model = model;
        this._year = year;
        this._subModel = subModel;
        this._region = region;
        this._fuelType = fuelType;
        this._vehicleId = vehicleId;
        this._manualMpg = manualMpg;
        this._manualGasPrice = manualGasPrice;
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

    public Double getManualMpg() {
        return _manualMpg;
    }

    public void setManualMpg(Double manualMpg) {
        this._manualMpg = manualMpg;
    }

    public Double getManualGasPrice() {
        return _manualGasPrice;
    }

    public void setManualGasPrice(Double manualGasPrice) {
        this._manualGasPrice = manualGasPrice;
    }

}
