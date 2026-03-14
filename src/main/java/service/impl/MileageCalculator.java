package service.impl;

import org.springframework.stereotype.Service;

import entity.VehicleEntity;
import model.FuelCosts;
import model.MaintenanceCosts;
import service.api.CityMpgService;
import service.api.GasPriceService;

/**
 * Pure calculation helper that composes the service interfaces and model logic.
 * Made test-friendly so we can unit-test logic without calling external APIs.
 */
@Service
public class MileageCalculator {

    private final CityMpgService _mpgService;
    private final GasPriceService _gasService;


    /**
     * Calculate cost-per-mile given services and vehicle info.
     * Returns 0.0 when MPG or gas price cannot be obtained.
     */

    public MileageCalculator(CityMpgService mpgService, GasPriceService gasService) {
        this._mpgService = mpgService;
        this._gasService = gasService;
    }

    /** 
     * Overload for when caller is not logged in and doesn't have a VehicleEntity; this will be used by the WebController
     */
    public double calculateCostPerMile(String make, String model, String year, String submodel, String region, String fuelType) throws Exception {
        double mpg = _mpgService.getMpg(make, model, year, submodel);
        if (mpg == 0.0) return 0.0;

        double gasPrice = _gasService.getPrice(region, fuelType);
        if (gasPrice == 0.0) return 0.0;

        VehicleEntity v = new VehicleEntity(make, model, year, submodel, mpg, null);
        FuelCosts fc = new FuelCosts(gasPrice);
        return fc.costPerMile(v);
    }
    /** 
     * Overload for when caller already has city MPG and just wants to calculate cost-per-mile; 
     * this will be used by the WebController when calculating costs, to avoid redundant API calls
        */
    public double calculateCostPerMile(double cityMpg, String region, String fuelType) throws Exception {
        double mpg = cityMpg;
        if (mpg == 0.0) return 0.0;

        double gasPrice = _gasService.getPrice(region, fuelType);
        if (gasPrice == 0.0) return 0.0;

    
        FuelCosts fc = new FuelCosts(gasPrice);
        return fc.costPerMile(mpg);
    }

    /**
     * Calculate cost-per-mile including optional maintenance costs (e.g., oil changes).
     * If `maintenance` is null, returns gas-only cost-per-mile.
     */
    public double calculateCostPerMile(String make, String model, String year, String submodel, String region, String fuelType, 
                                              MaintenanceCosts maintenance) throws Exception {
        double base = calculateCostPerMile(make, model, year, submodel, region, fuelType);
        if (base == 0.0) return 0.0;
        if (maintenance == null) return base;
        return base + maintenance.oilChangeCostPerMile();
    }
}
