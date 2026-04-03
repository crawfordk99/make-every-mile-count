package controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entity.UserEntity;
import entity.VehicleEntity;
import model.CalculateResponse;
import model.VehicleDisplay;
import model.VehicleRequest;
import repository.VehicleRepository;
import service.impl.CostPerMileCalculator;
import service.impl.VehicleService;

@RestController
public class VehicleController {
    private final VehicleRepository _vehicleRepository;
    private final VehicleService _vehicleService;
    private final CostPerMileCalculator _costCalculator;   
    
    public VehicleController(VehicleRepository vehicleRepository, VehicleService vehicleService, CostPerMileCalculator costCalculator) {
        this._costCalculator = costCalculator;
        this._vehicleRepository = vehicleRepository;
        this._vehicleService = vehicleService;

    }

    @GetMapping("/api/vehicles")
    public ResponseEntity<?> getVehicles(@AuthenticationPrincipal UserEntity currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        VehicleDisplay displayVehicles = new VehicleDisplay(_vehicleService.getVehiclesForUser(currentUser));

        List<VehicleEntity> vehicles = displayVehicles.getVehicles();

        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/api/vehicle/save-calculate")
    public ResponseEntity<?> saveVehicle(@AuthenticationPrincipal UserEntity currentUser, @RequestBody VehicleRequest request) throws Exception{
        // if (currentUser == null) {
        //     System.out.println("DEBUG: User is NULL - Session not persisting!");
        //     return ResponseEntity.status(401).body("User not logged in");
        // }
        // System.out.println("DEBUG: Logged in as: " + currentUser.getUsername());
        
        if (request.getVehicleId() == null) {
            VehicleEntity savedVehicle = _vehicleService.saveNewVehicle(request, currentUser);
        _vehicleRepository.save(savedVehicle);
        request.setVehicleId(savedVehicle.getVehicleId()); // Set the generated ID back to the request for calculation
        } 
        
        CalculateResponse calculateResponse = _costCalculator.calculateCostPerMile(request, currentUser);
        return ResponseEntity.ok(calculateResponse);
    }
}
