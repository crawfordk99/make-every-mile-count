package model;

public class CalculateResponse {
    private double cityMpg;
    private double gasPrice;
    private double costPerMile;

    public CalculateResponse(double cityMpg, double gasPrice, double costPerMile) {
        this.cityMpg = cityMpg;
        this.gasPrice = gasPrice;
        this.costPerMile = costPerMile;
    }

    public double getCityMpg() {
        return cityMpg;
    }

    public void setCityMpg(double cityMpg) {
        this.cityMpg = cityMpg;
    }

    public double getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(double gasPrice) {
        this.gasPrice = gasPrice;
    }

    public double getCostPerMile() {
        return costPerMile;
    }

    public void setCostPerMile(double costPerMile) {
        this.costPerMile = costPerMile;
    }
}
