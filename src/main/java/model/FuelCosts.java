package model;

public class FuelCosts
{
    private double _averageGasPrice;

    public FuelCosts(double averageGasPrice)
    {
        this._averageGasPrice = averageGasPrice;
    }

    // Calculate cost per mile using the vehicle's city MPG
    public double costPerMile(Vehicle vehicle)
    {
        // Guard against division by zero; caller should validate city MPG
        double cityMpg = vehicle.getCityMpg();
        if (cityMpg == 0.0) return 0.0;
        return _averageGasPrice / cityMpg;
    }

    // If caller already has city MPG, they can use this overload to avoid creating a Vehicle object
    public double costPerMile(double cityMpg)
    {
        // Guard against division by zero; caller should validate city MPG
        if (cityMpg == 0.0) return 0.0;
        return _averageGasPrice / cityMpg;
    }
}

