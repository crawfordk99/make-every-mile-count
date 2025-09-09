

public class FuelCosts
{
    private double _averageGasPrice;

    public FuelCosts(double averageGasPrice)
    {
        this._averageGasPrice = averageGasPrice;
    }

    public int costPerMile(Vehicle vehicle)
    {
        return this._averageGasPrice / vehicle.getCityMpg();
    }
}

