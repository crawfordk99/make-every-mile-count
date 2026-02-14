package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.api.CityMpgService;

@RestController
public class MPGController {
    private final CityMpgService _cityMpgService;

    public MPGController(CityMpgService cityMpgService) {
        this._cityMpgService = cityMpgService;
    }

    @GetMapping("/mpg")
    public double getMpg(@RequestParam String make, @RequestParam String model, @RequestParam String year, @RequestParam(required = false) String subModel) throws Exception {
        if (subModel != null && !subModel.isEmpty()) {
            return _cityMpgService.getMpg(make, model, year, subModel);
        } else {
            return _cityMpgService.getMpg(make, model, year);
        }   
    }
}
