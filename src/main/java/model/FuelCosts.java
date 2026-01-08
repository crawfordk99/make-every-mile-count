package model;

public class FuelCosts
{
    private double _averageGasPrice;

    public FuelCosts(double averageGasPrice)
    {
        this._averageGasPrice = averageGasPrice;
    }

    public double costPerMile(Vehicle vehicle)
    {
        // Guard against division by zero; caller should validate city MPG
        double cityMpg = vehicle.getCityMpg();
        if (cityMpg == 0.0) return 0.0;
        return this._averageGasPrice / cityMpg;
    }
}
