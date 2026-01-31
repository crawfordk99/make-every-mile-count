package model;

/**
 * Represents recurring maintenance costs such as oil changes.
 * Fields use the project convention of leading underscore for private fields.
 */
public class MaintenanceCosts {
    public static final double DEFAULT_OIL_CHANGE_COST = 50.00;
    public static final int DEFAULT_MILES_PER_OIL_CHANGE = 5000;

    private double _oilChangeCost;
    private int _milesPerOilChange;

    public MaintenanceCosts() {
        this(DEFAULT_OIL_CHANGE_COST, DEFAULT_MILES_PER_OIL_CHANGE);
    }

    public MaintenanceCosts(double oilChangeCost, int milesPerOilChange) {
        this._oilChangeCost = oilChangeCost;
        this._milesPerOilChange = milesPerOilChange;
    }

    public double getOilChangeCost() { return _oilChangeCost; }
    public int getMilesPerOilChange() { return _milesPerOilChange; }

    public void setOilChangeCost(double oilChangeCost) { this._oilChangeCost = oilChangeCost; }
    public void setMilesPerOilChange(int milesPerOilChange) { this._milesPerOilChange = milesPerOilChange; }

    /**
     * Returns the per-mile cost of oil changes: oilChangeCost / milesPerOilChange.
     * Returns 0.0 if milesPerOilChange is not positive to guard against division by zero.
     */
    public double oilChangeCostPerMile() {
        if (this._milesPerOilChange <= 0) return 0.0;
        return this._oilChangeCost / this._milesPerOilChange;
    }

    /**
     * Helper: returns the cost for a given number of miles (e.g., cost for N miles)
     */
    public double costForMiles(int miles) {
        if (miles <= 0) return 0.0;
        return oilChangeCostPerMile() * miles;
    }
}
