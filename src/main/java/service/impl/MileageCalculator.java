package service.impl;

import org.springframework.stereotype.Service;

import model.FuelCosts;
import model.MaintenanceCosts;
import model.Vehicle;
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

    public double calculateCostPerMile(String make, String model, String year, String submodel, String region, String fuelType) throws Exception {
        double mpg = _mpgService.getMpg(make, model, year, submodel);
        if (mpg == 0.0) return 0.0;

        double gasPrice = _gasService.getPrice(region, fuelType);
        if (gasPrice == 0.0) return 0.0;

        Vehicle v = new Vehicle(make, model, year, submodel);
        v.setCityMpg(mpg);
        FuelCosts fc = new FuelCosts(gasPrice);
        return fc.costPerMile(v);
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
