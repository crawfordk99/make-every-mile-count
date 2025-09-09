public class Vehicle
{
    private string _make;
    private string _model;
    private string _year;

    public Vehicle(string make, string model, string year)
    {
        _make = make;
        _model = model;
        _year = year;
    }

    public double getCityMpg()
    {
        return 0.00;
    }
}