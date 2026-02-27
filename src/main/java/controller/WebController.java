package controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.CalculateResponse;
import service.impl.GetAverageGasPrice;
import service.impl.GetCityMPG;
import service.impl.MileageCalculator;

@RestController
public class WebController {
    private final MileageCalculator _mileageCalculator;
    private final GetCityMPG _getCityMPG;
    private final GetAverageGasPrice _getAverageGasPrice;

    public WebController(MileageCalculator mileageCalculator, GetCityMPG getCityMPG, GetAverageGasPrice getAverageGasPrice) {
        this._mileageCalculator = mileageCalculator;
        this._getCityMPG = getCityMPG;
        this._getAverageGasPrice = getAverageGasPrice;
    }

    @PostMapping("/api/calculate")
    public CalculateResponse calculateCostPerMileApi(@RequestBody java.util.Map<String, String> request) throws Exception {
        String make = request.get("make");
        String model = request.get("model");
        String year = request.get("year");
        String subModel = request.get("subModel");
        String region = request.get("region");
        String fuelType = request.get("fuelType");

        double cityMpg = _getCityMPG.getMpg(make, model, year, subModel);
        double gasPrice = _getAverageGasPrice.getPrice(region, fuelType);
        double costPerMile = _mileageCalculator.calculateCostPerMile(make, model, year, subModel, region, fuelType);

        return new CalculateResponse(cityMpg, gasPrice, costPerMile);
    }
}
