package ui;

import model.FuelCosts;
import model.Vehicle;
import service.GetCityMPG;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test with 2016 Ford Fusion (has multiple submodels with different MPG values)
        String make = "Ford";
        String model = "Fusion";
        String year = "2016";

        GetCityMPG svc = new GetCityMPG();

        // Test 1: Get default (first result)
        double mpg = svc.getMpg(make, model, year);
        System.out.println("Test 1 - Default (first result): " + mpg + " city MPG");

        // Test 2: Get specific submodel
        double hybridMpg = svc.getMpg(make, model, year, "S Hybrid");
        System.out.println("Test 2 - S Hybrid submodel: " + hybridMpg + " city MPG");

        Vehicle v = new Vehicle(make, model, year);
        v.setCityMpg(mpg);

        FuelCosts fc = new FuelCosts(3.50);
        System.out.println("\nCost per mile (at $3.50/gal): $" + String.format("%.4f", fc.costPerMile(v)));
    }
}
