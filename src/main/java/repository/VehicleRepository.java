package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Vehicle;

/**
 * Data access object for Vehicle operations.
 * Handles creation and retrieval of vehicles.
 */
public class VehicleRepository {
    private String _dbUrl;
    private String _dbUser;
    private String _dbPassword;

    public VehicleRepository(String dbUrl, String dbUser, String dbPassword) {
        this._dbUrl = dbUrl;
        this._dbUser = dbUser;
        this._dbPassword = dbPassword;
    }

    /**
     * Save a vehicle to the database.
     * @param userId the owner's user ID
     * @param vehicle the Vehicle object to save
     * @return the vehicle's ID, or -1 if save failed
     */
    public int saveVehicle(int userId, Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (owner_id, make, model, year, submodel, city_mpg) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DriverManager.getConnection(_dbUrl, _dbUser, _dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, vehicle.getMake());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getYear());
            stmt.setString(5, vehicle.getSubModel());
            stmt.setDouble(6, vehicle.getCityMpg());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error saving vehicle: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieve all vehicles for a user.
     * @param ownerId the owner's user ID
     * @return a list of Vehicle objects
     */
    public List<Vehicle> getVehiclesByUserId(int ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT make, model, year, submodel, city_mpg FROM vehicles WHERE owner_id = ?";

        try (Connection conn = DriverManager.getConnection(_dbUrl, _dbUser, _dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getString("year"),
                    rs.getString("submodel")
                );
                vehicle.setCityMpg(rs.getDouble("city_mpg"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving vehicles: " + e.getMessage());
        }
        return vehicles;
    }
}
