package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.api.GasPriceService;

@RestController
public class PriceController {
    private final GasPriceService _gasPriceService;

    public PriceController(GasPriceService gasPriceService) {
        this._gasPriceService = gasPriceService;
    }

    @GetMapping("/price")
    public double getPrice(@RequestParam String region, @RequestParam String fuelType) throws Exception {
        return _gasPriceService.getPrice(region, fuelType);
    }

}
