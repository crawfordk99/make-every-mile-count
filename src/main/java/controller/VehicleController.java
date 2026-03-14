package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entity.UserEntity;
import entity.VehicleEntity;
import repository.VehicleRepository;
import service.impl.GetCityMPG;

@RestController
public class VehicleController {
    private final VehicleRepository _vehicleRepository;
    private final GetCityMPG _getCityMPG;
    
    public VehicleController(VehicleRepository vehicleRepository, GetCityMPG getCityMPG) {
        this._vehicleRepository = vehicleRepository;
        this._getCityMPG = getCityMPG;
    }

    @PostMapping("/api/vehicle/save")
    public ResponseEntity<String> saveVehicle(@AuthenticationPrincipal UserEntity currentUser, @RequestBody java.util.Map<String, String> request) throws Exception{
        if (currentUser == null) { // This should never happen if Spring Security is configured correctly, but we check just in case
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String make = request.get("make");
        String model = request.get("model");
        String year = request.get("year");
        String subModel = request.get("subModel");
        

        double cityMpg = _getCityMPG.getMpg(make, model, year, subModel);
        if (cityMpg == 0.0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid vehicle data or MPG not found");
        }

        VehicleEntity vehicleEntity = new VehicleEntity(make, model, year, subModel, cityMpg, currentUser.getUserId());
        _vehicleRepository.save(vehicleEntity);


        return ResponseEntity.ok("Vehicle saved successfully");
    }
}
