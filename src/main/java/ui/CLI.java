package ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import model.Region;
import repository.UserRepository;
import repository.VehicleRepository;

/**
 * Command-line interface for Make Every Mile Count.
 * Prompts user for vehicle info, region, and gasoline type.
 */
public class CLI
{
    private Scanner scanner;
    private UserRepository userRepository;
    private VehicleRepository vehicleRepository;
    private String currentUserId = null;

    public CLI()
    {
        this.scanner = new Scanner(System.in);
        
        // Try to load DB credentials from config.properties first, then fall back to env vars
        String dbUrl = null;
        String dbUser = null;
        String dbPassword = null;
        
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            fis.close();
            
            dbUrl = props.getProperty("DB_URL");
            dbUser = props.getProperty("DB_USER");
            dbPassword = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            // config.properties not found, try environment variables
        }
        
        // Fall back to environment variables if config.properties didn't provide values
        if (dbUrl == null) {
            dbUrl = System.getenv("DB_URL");
        }
        if (dbUser == null) {
            dbUser = System.getenv("DB_USER");
        }
        if (dbPassword == null) {
            dbPassword = System.getenv("DB_PASSWORD");
        }
        
        if (dbUrl != null && dbUser != null && dbPassword != null) {
            // Remove quotes if present (from properties file)
            dbPassword = dbPassword.replaceAll("^'|'$", "");
            this.userRepository = new UserRepository(dbUrl, dbUser, dbPassword);
            this.vehicleRepository = new VehicleRepository(dbUrl, dbUser, dbPassword);
        }
    }

    /**
     * Start the interactive CLI.
     */
    public void start() throws Exception
    {
        System.out.println("=== Make Every Mile Count ===");
        System.out.println("Calculate your cost per mile based on vehicle MPG and regional gas prices.\n");

        // Optional: ask if user wants to create account or login
        if (userRepository != null && vehicleRepository != null) {
            promptForAccount();
        }

        String make = "";
        String model = "";
        String year = "";
        String subModel = "";

        if (currentUserId != null) {
            List<model.Vehicle> vehicles = vehicleRepository.getVehiclesByUserId(Integer.parseInt(currentUserId));
            if (vehicles.isEmpty()) {
                System.out.println("No saved vehicles found. Please enter vehicle information.\n");
                make = promptForMake();
                model = promptForModel();
                year = promptForYear();
                subModel = promptForSubModel();
            } else  {
                System.out.println("Saved vehicles:");
                for (int i = 0; i < vehicles.size(); i++) {
                    model.Vehicle v = vehicles.get(i);
                    System.out.println((i + 1) + ". " + v.getYear() + " " + v.getMake() + " " + v.getModel() + " " + v.getSubModel());
                }
                System.out.print("Select a vehicle by number, or press Enter to input a new one: ");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    try {
                        int choice = Integer.parseInt(input);
                        if (choice >= 1 && choice <= vehicles.size()) {
                            model.Vehicle selected = vehicles.get(choice - 1);
                            make = selected.getMake();
                            model = selected.getModel();
                            year = selected.getYear();
                            subModel = selected.getSubModel();
                            System.out.println("Selected vehicle: " + year + " " + make + " " + model + " " + subModel);
                        } else {
                            System.out.println("Invalid choice. Proceeding to manual input.\n");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Proceeding to manual input.\n");
                    }
                }
            }
        }else {
            // Get vehicle info from user input
            make = promptForMake();
            model = promptForModel();
            year = promptForYear();
            subModel = promptForSubModel();
        }

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

        // Retrieve saved vehicles for duplicate check and MPG lookup
        List<model.Vehicle> savedVehicles = new java.util.ArrayList<>();
        if (currentUserId != null) {
            savedVehicles = vehicleRepository.getVehiclesByUserId(Integer.parseInt(currentUserId));
        }

        System.out.println("\nFetching data...\n");

        // Calculate and display results
        calculateAndDisplay(make, model, year, subModel, region, gasolineType, maintenance, savedVehicles);

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
    private void calculateAndDisplay(String make, String model, String year, String subModel, Region region, String gasolineType, model.MaintenanceCosts maintenance, List<model.Vehicle> savedVehicles) throws Exception
    {
        try
        {
            // Check if this vehicle is already saved and has an MPG value
            double mpg = 0.0;
            boolean isAlreadySaved = false;
            
            for (model.Vehicle saved : savedVehicles) {
                if (saved.getMake().equalsIgnoreCase(make) && 
                    saved.getModel().equalsIgnoreCase(model) && 
                    saved.getYear().equals(year) && 
                    saved.getSubModel().equalsIgnoreCase(subModel)) {
                    mpg = saved.getCityMpg();
                    isAlreadySaved = true;
                    System.out.println("✓ Found saved vehicle with MPG: " + mpg);
                    break;
                }
            }
            
            // If not saved or no MPG data, fetch from API
            if (mpg == 0.0) {
                service.api.CityMpgService mpgService = new service.impl.GetCityMPG();
                mpg = mpgService.getMpg(make, model, year, subModel);

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

            // If user is logged in and vehicle is not already saved, offer to save it
            if (currentUserId != null && savedVehicles != null) {
                saveVehicleIfDesired(v, savedVehicles);
            }
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

    /**
     * Prompt user for account setup: login, create account, or skip.
     */
    private void promptForAccount() {
        System.out.println("Save your vehicles for future reference? (optional)");
        System.out.print("1. Create account\n2. Login\n 3. Skip (Press Enter)\nEnter choice (1-2, default=2): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty() || input.equals("3")) {
            System.out.println("Proceeding without account.\n");
            return;
        }
        
        if (input.equals("2")) {
            signIn();
        } 

        if (input.equals("1")) {
            createAccount();
        } else {
            System.out.println("Invalid choice.\n");
        }
    }

    /**
     * Prompt user to create a new account.
     */
    private void createAccount() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        // Check if email already exists
        model.User existingUser = userRepository.getUserByEmail(email);
        if (existingUser != null) {
            System.out.println("Account already exists for this email. Proceeding without saving.\n");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Hash password (simple example - in production, use proper hashing)
        String passwordHash = hashPassword(password);

        int userId = userRepository.createUser(email, passwordHash);
        if (userId > 0) {
            currentUserId = String.valueOf(userId);
            System.out.println("Account created successfully!\n");
        } else {
            System.out.println("Failed to create account. Proceeding without saving.\n");
        }
    }

    private void signIn() {
        System.out.print("Enter email: ");
        String inputEmail = scanner.nextLine().trim();

        // Check if email already exists
        model.User existingUser = userRepository.getUserByEmail(inputEmail);
        if (existingUser != null) {
            while (true) {
                System.out.print("Enter password: ");
                String inputPassword = scanner.nextLine().trim();
                String inputHash = hashPassword(inputPassword);
                if (inputHash.equals(existingUser.getPasswordHash())) {
                    currentUserId = existingUser.getUserId();
                    System.out.println("Signed in successfully!\n");
                    
                    return;
                } else {
                    System.out.println("Incorrect password. Try again.");
                }
            }
        } else {
            System.out.println("No account found for this email. Please create an account first.\n");
        }

    }

    /**
     * Simple password hashing (in production, use bcrypt or similar).
     */
    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    /**
     * Offer to save the current vehicle to the database.
     */
    private void saveVehicleIfDesired(model.Vehicle vehicle, List<model.Vehicle> savedVehicles) {
        // Check if vehicle already exists
        for (model.Vehicle saved : savedVehicles) {
            if (saved.getMake().equalsIgnoreCase(vehicle.getMake()) && 
                saved.getModel().equalsIgnoreCase(vehicle.getModel()) && 
                saved.getYear().equals(vehicle.getYear()) && 
                saved.getSubModel().equalsIgnoreCase(vehicle.getSubModel())) {
                System.out.println("This vehicle is already saved in your account.\n");
                return;
            }
        }
        
        System.out.print("Save this vehicle to your account? (y/N): ");
        String input = scanner.nextLine().trim().toLowerCase();
        
        if (input.equals("y") || input.equals("yes")) {
            try {
                int vehicleId = vehicleRepository.saveVehicle(Integer.parseInt(currentUserId), vehicle);
                if (vehicleId > 0) {
                    System.out.println("✓ Vehicle saved successfully!\n");
                } else {
                    System.out.println("Failed to save vehicle.\n");
                }
            } catch (Exception e) {
                System.err.println("Error saving vehicle: " + e.getMessage());
            }
        }
    }
}
