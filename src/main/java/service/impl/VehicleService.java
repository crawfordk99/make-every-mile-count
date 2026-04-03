package service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entity.UserEntity;
import entity.VehicleEntity;
import model.VehicleRequest;
import repository.VehicleRepository;
import service.api.CityMpgService;

@Service
public class VehicleService 
{
    private final CityMpgService _cityMpgService;
    private final VehicleRepository _vehicleRepository;

    public VehicleService(CityMpgService cityMpgService, VehicleRepository vehicleRepository) {
        this._cityMpgService = cityMpgService;
        this._vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> getVehiclesForUser(UserEntity user) {
        return _vehicleRepository.findAllByOwnerId(user);
    }

    public VehicleEntity saveNewVehicle(VehicleRequest request, UserEntity user) throws Exception{
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setSubModel(request.getSubModel());
        double mpg = _cityMpgService.getMpg(vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getSubModel());

        if (mpg == 0.0) {
            throw new Exception("Unable to retrieve MPG for the provided vehicle details.");
        }
        
        vehicle.setCityMpg(mpg);
        vehicle.setOwnerId(user);
        return vehicle; // Return the saved vehicle entity (with ID if generated)
    }
}

