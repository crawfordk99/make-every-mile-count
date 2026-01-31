package ui;

import java.util.Scanner;

import model.Region;

/**
 * Command-line interface for Make Every Mile Count.
 * Prompts user for vehicle info, region, and gasoline type.
 */
public class CLI
{
    private Scanner scanner;

    public CLI()
    {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Start the interactive CLI.
     */
    public void start() throws Exception
    {
        System.out.println("=== Make Every Mile Count ===");
        System.out.println("Calculate your cost per mile based on vehicle MPG and regional gas prices.\n");

        // Get vehicle info
        String make = promptForMake();
        String model = promptForModel();
        String year = promptForYear();
        String subModel = promptForSubModel();

        // Get region
        Region region = promptForRegion();

        // Get gasoline type
        String gasolineType = promptForGasolineType();

        // Maintenance costs option
        boolean includeMaintenance = promptForIncludeMaintenance();
        model.MaintenanceCosts maintenance = null;
        if (includeMaintenance) {
            maintenance = promptForMaintenanceValues();
        }

        System.out.println("\nFetching data...\n");

        // Calculate and display results
        calculateAndDisplay(make, model, year, subModel, region, gasolineType, maintenance);

        // Ask if user wants to try another calculation
        if (promptContinue())
        {
            start();
        }
        else
        {
            System.out.println("Thank you for using Make Every Mile Count!");
            scanner.close();
        }
    }

    /**
     * Prompt user for vehicle make.
     */
    private String promptForMake()
    {
        System.out.print("Enter vehicle make (e.g., Ford, Toyota, Nissan): ");
        return scanner.nextLine().trim();
    }

    /**
     * Prompt user for vehicle model.
     */
    private String promptForModel()
    {
        System.out.print("Enter vehicle model (e.g., Fusion, Camry, Versa): ");
        return scanner.nextLine().trim();
    }

    /**
     * Prompt user for vehicle year.
     */
    private String promptForYear()
    {
        while (true)
        {
            System.out.print("Enter vehicle year (2015-2020): ");
            String year = scanner.nextLine().trim();
            try
            {
                int yearInt = Integer.parseInt(year);
                if (yearInt >= 2015 && yearInt <= 2023)
                {
                    return year;
                }
                else
                {
                    System.out.println("Please enter a year between 2015 and 2023.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid year. Please enter a 4-digit number.");
            }
        }
    }

    private String promptForSubModel()
    {
        System.out.print("Enter vehicle sub-model (optional, press Enter to skip): ");
        return scanner.nextLine().trim();
    }

    /**
     * Prompt user for region.
     */
    private Region promptForRegion()
    {
        System.out.println("\nSelect region:");
        System.out.println("1. U.S. National Average (default)");
        System.out.println("2. PADD 1 (East Coast)");
        System.out.println("3. PADD 2 (Midwest)");
        System.out.println("4. PADD 3 (Gulf Coast)");
        System.out.println("5. PADD 4 (Rocky Mountain)");
        System.out.println("6. PADD 5 (West Coast)");
        System.out.println("7. New York");
        System.out.println("8. Texas");
        System.out.println("9. California");
        System.out.println("10. Colorado");

        while (true)
        {
            System.out.print("Enter region number (1-10, default=1): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
            {
                return Region.US_NATIONAL;
            }

            try
            {
                int choice = Integer.parseInt(input);
                switch (choice)
                {
                    case 1: return Region.US_NATIONAL;
                    case 2: return Region.PADD_1;
                    case 3: return Region.PADD_2;
                    case 4: return Region.PADD_3;
                    case 5: return Region.PADD_4;
                    case 6: return Region.PADD_5;
                    case 7: return Region.NEW_YORK;
                    case 8: return Region.TEXAS;
                    case 9: return Region.CALIFORNIA;
                    case 10: return Region.COLORADO;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Prompt user for gasoline type.
     */
    private String promptForGasolineType()
    {
        System.out.println("\nSelect gasoline type:");
        System.out.println("1. Regular (default)");
        System.out.println("2. Medium");
        System.out.println("3. Premium");

        while (true)
        {
            System.out.print("Enter gasoline type (1-3, default=1): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
            {
                return "EPMR"; // Regular
            }

            try
            {
                int choice = Integer.parseInt(input);
                switch (choice)
                {
                    case 1: return "EPMR"; // Regular
                    case 2: return "EPMM"; // Medium
                    case 3: return "EPMP"; // Premium
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Calculate and display results.
     */
    private void calculateAndDisplay(String make, String model, String year, String subModel, Region region, String gasolineType, model.MaintenanceCosts maintenance) throws Exception
    {
        try
        {
            // Fetch city MPG
            service.api.CityMpgService mpgService = new service.impl.GetCityMPG();
            double mpg = mpgService.getMpg(make, model, year, subModel);

            if (mpg == 0.0)
            {
                System.out.println("ERROR: Could not find MPG data for " + year + " " + make + " " + model);
                if (subModel != null && !subModel.isEmpty())
                {
                    System.out.println("        (Submodel: " + subModel + ")");
                }
                System.out.println("(Note: Data may only be available for 2015-2020 models)");
                return;
            }

            System.out.println("✓ Vehicle city MPG: " + mpg);

            // Fetch regional gas price
            service.api.GasPriceService gasPriceService = new service.impl.GetAverageGasPrice(region, gasolineType);
            double gasPrice = gasPriceService.getPrice();

            if (gasPrice == 0.0)
            {
                System.out.println("ERROR: Could not fetch gas price data for " + region.getDisplayName());
                return;
            }

            System.out.println("✓ Current gas price (" + getGasolineTypeName(gasolineType) + ") in " + region.getDisplayName() + ": $" + String.format("%.2f", gasPrice) + "/gal");

            // Create vehicle and calculate gas cost per mile
            model.Vehicle v = new model.Vehicle(make, model, year, subModel);
            v.setCityMpg(mpg);
            model.FuelCosts fc = new model.FuelCosts(gasPrice);
            double gasCostPerMile = fc.costPerMile(v);

            double maintenancePerMile = (maintenance != null) ? maintenance.oilChangeCostPerMile() : 0.0;
            double totalPerMile = gasCostPerMile + maintenancePerMile;

            System.out.println("\n=== RESULTS ===");
            System.out.println("Vehicle: " + year + " " + make + " " + model + " " + subModel);
            System.out.println("City MPG: " + mpg);
            System.out.println("Gas Price: $" + String.format("%.2f", gasPrice) + "/gal (" + getGasolineTypeName(gasolineType) + ")");
            System.out.println("Region: " + region.getDisplayName());
    
            System.out.println("Total cost per mile: $" + String.format("%.4f", totalPerMile));
            System.out.println("Total cost per 100 miles: $" + String.format("%.2f", totalPerMile * 100));
            System.out.println();
        }
        catch (Exception e)
        {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get human-readable gasoline type name.
     */
    private String getGasolineTypeName(String code)
    {
        switch (code)
        {
            case "EPMR": return "Regular";
            case "EPMPU": return "Premium";
            case "EPD2DXL0": return "Diesel";
            default: return code;
        }
    }

    /**
     * Ask if user wants to continue.
     */
    private boolean promptContinue()
    {
        System.out.print("Calculate another? (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("yes") || input.equals("y");
    }

    private boolean promptForIncludeMaintenance()
    {
        System.out.print("Include maintenance costs (oil changes)? (y/N): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    private model.MaintenanceCosts promptForMaintenanceValues()
    {
        System.out.println("Default oil change: $" + model.MaintenanceCosts.DEFAULT_OIL_CHANGE_COST + " every " + model.MaintenanceCosts.DEFAULT_MILES_PER_OIL_CHANGE + " miles.");
        System.out.print("Use defaults? (Enter to accept, N to customize): ");
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.isEmpty() || input.equals("y") || input.equals("yes")) {
            return new model.MaintenanceCosts();
        }

        double cost;
        int miles;
        while (true) {
            try {
                System.out.print("Enter oil change cost in dollars (e.g., 45.00): ");
                String costStr = scanner.nextLine().trim();
                cost = Double.parseDouble(costStr);
                if (cost < 0) { System.out.println("Please enter a positive cost."); continue; }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        while (true) {
            try {
                System.out.print("Enter miles between oil changes (e.g., 5000): ");
                String milesStr = scanner.nextLine().trim();
                miles = Integer.parseInt(milesStr);
                if (miles <= 0) { System.out.println("Please enter a positive integer."); continue; }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }

        return new model.MaintenanceCosts(cost, miles);
    }

    public void close()
    {
        scanner.close();
    }
}
