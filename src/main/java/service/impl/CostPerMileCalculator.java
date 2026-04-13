package service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import entity.UserEntity;
import entity.VehicleEntity;
import model.CalculateResponse;
import model.FuelCosts;
import model.VehicleRequest;
import repository.VehicleRepository;
import service.api.CityMpgService;
import service.api.GasPriceService;

/**
 * Pure calculation helper that composes the service interfaces and model logic.
 * Made test-friendly so we can unit-test logic without calling external APIs.
 */
@Service
public class CostPerMileCalculator {

    private final CityMpgService _mpgService;
    private final GasPriceService _gasService;
    private final VehicleRepository _vehicleRepository;

    

    public CostPerMileCalculator(CityMpgService mpgService, GasPriceService gasService, VehicleRepository vehicleRepo) {
        this._mpgService = mpgService;
        this._gasService = gasService;
        this._vehicleRepository = vehicleRepo;
    }

    /**
     * Calculate cost-per-mile given services and vehicle info.
     * Returns 0.0 when MPG or gas price cannot be obtained.
     * Using waterfall logic: if user provides MPG and/or Gas Price, those are used;
     * otherwise, if user is logged in and provides vehicleId, MPG is pulled from the database;
     * lastly, MPG and gas price are pulled from external APIs based on form data.
     */
    public CalculateResponse calculateCostPerMile(VehicleRequest request, UserEntity user) throws Exception {
        double mpg;
        double gasPrice;

        if (request.getManualGasPrice() != null) {
            gasPrice = request.getManualGasPrice();
        } else {
            gasPrice = _gasService.getPrice(request.getRegion(), request.getFuelType());
        }

        if (request.getManualMpg() != null) {
            mpg = request.getManualMpg();
        } else {
            if (user != null && request.getVehicleId() != null) {
                // Logged in: Pull the MPG from the database
                VehicleEntity savedVehicle = _vehicleRepository.findByVehicleIdAndOwnerId(request.getVehicleId(), user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle not found for user"));
                
                mpg = savedVehicle.getCityMpg();
            } else {
                // Guest: Call the external API using form data
                mpg = _mpgService.getMpg(request.getMake(), request.getModel(), request.getYear(), request.getSubModel());
            }
        }

        
        if (mpg == 0.0 || gasPrice == 0.0) return new CalculateResponse(0.0, 0.0, 0.0);

        double costPerMile = new FuelCosts(gasPrice).costPerMile(mpg);

        return new CalculateResponse(mpg, gasPrice, costPerMile);
    }

}
