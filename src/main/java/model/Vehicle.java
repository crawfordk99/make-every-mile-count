package model;

public class Vehicle
{
    private String _make;
    private String _model;
    private String _year;
    private String _subModel;
    private double _cityMpg = 0.0;

    public Vehicle(String make, String model, String year, String subModel)
    {
        _make = make;
        _model = model;
        _year = year;
        _subModel = subModel;
    }

    public String getMake() { return _make; }
    public String getModel() { return _model; }
    public String getYear() { return _year; }
    public String getSubModel() { return _subModel; }

    public double getCityMpg()
    {
        return _cityMpg;
    }

    public void setCityMpg(double mpg)
    {
        this._cityMpg = mpg;
    }
}
