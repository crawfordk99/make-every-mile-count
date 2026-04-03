package controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.CalculateResponse;
import model.VehicleRequest;
import service.impl.CostPerMileCalculator;

@RestController
public class WebController {
    private final CostPerMileCalculator _costCalculator;

    public WebController(CostPerMileCalculator costCalculator) {
        this._costCalculator = costCalculator;
    }

    @PostMapping("/api/calculate")
    public CalculateResponse calculateCostPerMileApi(@RequestBody VehicleRequest request) throws Exception {
        
        CalculateResponse response = _costCalculator.calculateCostPerMile(request, null);

        return response;
    }
}
