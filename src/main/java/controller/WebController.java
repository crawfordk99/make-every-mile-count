package controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.CalculateResponse;
import service.impl.MileageCalculator;
import model.VehicleRequest;

@RestController
public class WebController {
    private final MileageCalculator _mileageCalculator;

    public WebController(MileageCalculator mileageCalculator) {
        this._mileageCalculator = mileageCalculator;
    }

    @PostMapping("/api/calculate")
    public CalculateResponse calculateCostPerMileApi(@RequestBody VehicleRequest request) throws Exception {
        
        CalculateResponse response = _mileageCalculator.calculateCostPerMile(request, null);

        return response;
    }
}
