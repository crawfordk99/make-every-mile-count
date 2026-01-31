package service.impl;

import model.FuelCosts;
import model.MaintenanceCosts;
import model.Vehicle;
import service.api.CityMpgService;
import service.api.GasPriceService;

/**
 * Pure calculation helper that composes the service interfaces and model logic.
 * Made test-friendly so we can unit-test logic without calling external APIs.
 */
public class MileageCalculator {

    /**
     * Calculate cost-per-mile given services and vehicle info.
     * Returns 0.0 when MPG or gas price cannot be obtained.
     */
    public static double calculateCostPerMile(CityMpgService mpgService, GasPriceService gasService,
                                              String make, String model, String year, String submodel) throws Exception {
        double mpg = mpgService.getMpg(make, model, year, submodel);
        if (mpg == 0.0) return 0.0;

        double gasPrice = gasService.getPrice();
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
    public static double calculateCostPerMile(CityMpgService mpgService, GasPriceService gasService,
                                              String make, String model, String year, String submodel,
                                              MaintenanceCosts maintenance) throws Exception {
        double base = calculateCostPerMile(mpgService, gasService, make, model, year, submodel);
        if (base == 0.0) return 0.0;
        if (maintenance == null) return base;
        return base + maintenance.oilChangeCostPerMile();
    }
}
