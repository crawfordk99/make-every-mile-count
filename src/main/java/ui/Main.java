package ui;

import model.FuelCosts;
import model.Region;
import model.Vehicle;
import service.GetAverageGasPrice;
import service.GetCityMPG;

public class Main {
    public static void main(String[] args) throws Exception {
        // Vehicle info
        String make = "Ford";
        String model = "Fusion";
        String year = "2016";

        // Fetch city MPG
        GetCityMPG mpgService = new GetCityMPG();
        double mpg = mpgService.getMpg(make, model, year);
        System.out.println("Vehicle city MPG: " + mpg);

        // Fetch regional gas price (hardcoded region, default to Regular gasoline)
        GetAverageGasPrice gasPriceService = new GetAverageGasPrice(Region.US_NATIONAL);
        double gasPrice = gasPriceService.getPrice();
        System.out.println("Current gas price: $" + String.format("%.2f", gasPrice) + "/gal\n");

        // Create vehicle and calculate cost per mile
        Vehicle v = new Vehicle(make, model, year);
        v.setCityMpg(mpg);

        FuelCosts fc = new FuelCosts(gasPrice);
        double costPerMile = fc.costPerMile(v);
        System.out.println("Cost per mile: $" + String.format("%.4f", costPerMile));

        // Example: Try a different region or gasoline type (optional)
        // GetAverageGasPrice premiumService = new GetAverageGasPrice(Region.NEW_YORK, "EPMPU");
        // double premiumPrice = premiumService.getPrice();
        // System.out.println("Premium gas price in NY: $" + String.format("%.2f", premiumPrice));
    }
}
