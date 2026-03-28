package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entity.UserEntity;
import entity.VehicleEntity;
import model.CalculateResponse;
import model.VehicleRequest;
import repository.VehicleRepository;
import service.impl.MileageCalculator;
import service.impl.VehicleService;

@RestController
public class VehicleController {
    private final VehicleRepository _vehicleRepository;
    private final VehicleService _vehicleService;
    private final MileageCalculator _calculator;   
    
    public VehicleController(VehicleRepository vehicleRepository, VehicleService vehicleService, MileageCalculator mileageCalculator) {
         this._calculator = mileageCalculator;
        this._vehicleRepository = vehicleRepository;
        this._vehicleService = vehicleService;

    }

    @GetMapping("/api/vehicles")
    public ResponseEntity<?> getVehicles(@AuthenticationPrincipal UserEntity currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        return ResponseEntity.ok(currentUser.getVehicles());
    }

    @PostMapping("/api/vehicle/save-calculate")
    public ResponseEntity<?> saveVehicle(@AuthenticationPrincipal UserEntity currentUser, @RequestBody VehicleRequest request) throws Exception{
        if (currentUser == null) {
            System.out.println("DEBUG: User is NULL - Session not persisting!");
            return ResponseEntity.status(401).body("User not logged in");
        }
        System.out.println("DEBUG: Logged in as: " + currentUser.getUsername());
        
       
        VehicleEntity savedVehicle = _vehicleService.saveNewVehicle(request, currentUser);
        _vehicleRepository.save(savedVehicle);
        request.setVehicleId(savedVehicle.getVehicleId()); // Set the generated ID back to the request for calculation
    

        CalculateResponse calculateResponse = _calculator.calculateCostPerMile(request, currentUser);
        return ResponseEntity.ok(calculateResponse);
    }
}
