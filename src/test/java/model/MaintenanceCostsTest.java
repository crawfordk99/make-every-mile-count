package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MaintenanceCostsTest {

    @Test
    void oilChangeCostPerMile_basicCalculation() {
        MaintenanceCosts m = new MaintenanceCosts(40.0, 5000);
        double expected = 40.0 / 5000.0;
        assertEquals(expected, m.oilChangeCostPerMile(), 1e-12);
    }

    @Test
    void oilChangeCostPerMile_zeroIntervalReturnsZero() {
        MaintenanceCosts m = new MaintenanceCosts(40.0, 0);
        assertEquals(0.0, m.oilChangeCostPerMile(), 1e-12);

        m.setMilesPerOilChange(-100);
        assertEquals(0.0, m.oilChangeCostPerMile(), 1e-12);
    }

    @Test
    void costForMiles_computesCorrectly() {
        MaintenanceCosts m = new MaintenanceCosts(60.0, 6000); // $60 / 6000 miles = $0.01/mi
        assertEquals(0.01 * 100, m.costForMiles(100), 1e-12);
    }
}
