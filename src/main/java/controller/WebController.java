package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.impl.MileageCalculator;

public class WebController {
    private final MileageCalculator _mileageCalculator;

    public WebController(MileageCalculator mileageCalculator) {
        this._mileageCalculator = mileageCalculator;
    }

    @GetMapping("/calculate")
    public double calculateMileage(@RequestParam String make, @RequestParam String model, @RequestParam String year, @RequestParam(required = false) String subModel, @RequestParam String region, @RequestParam String fuelType) throws Exception {
        return _mileageCalculator.calculateCostPerMile(make, model, year, subModel, region, fuelType);
    }
}
