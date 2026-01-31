package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import service.api.CityMpgService;
import service.api.GasPriceService;
import service.impl.MileageCalculator;

class MileageCalculatorTest {

    @Test
    void calculateCostPerMile_success() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg("Toyota", "Camry", "2018", null)).thenReturn(25.0);
        when(gas.getPrice()).thenReturn(3.50);

        double cost = MileageCalculator.calculateCostPerMile(mpg, gas, "Toyota", "Camry", "2018", null);
        assertEquals(3.50 / 25.0, cost, 1e-9);

        verify(mpg).getMpg("Toyota", "Camry", "2018", null);
        verify(gas).getPrice();
    }

    @Test
    void calculateCostPerMile_missingMpg_returnsZero() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg(anyString(), anyString(), anyString(), any())).thenReturn(0.0);
        // gas price should not be called when mpg is missing, but stub anyway
        when(gas.getPrice()).thenReturn(3.50);

        double cost = MileageCalculator.calculateCostPerMile(mpg, gas, "Ford", "Fiesta", "2016", null);
        assertEquals(0.0, cost, 1e-9);

        verify(mpg).getMpg("Ford", "Fiesta", "2016", null);
        verify(gas, never()).getPrice();
    }

    @Test
    void calculateCostPerMile_missingGasPrice_returnsZero() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg(anyString(), anyString(), anyString(), any())).thenReturn(20.0);
        when(gas.getPrice()).thenReturn(0.0);

        double cost = MileageCalculator.calculateCostPerMile(mpg, gas, "Honda", "Civic", "2017", null);
        assertEquals(0.0, cost, 1e-9);

        verify(mpg).getMpg("Honda", "Civic", "2017", null);
        verify(gas).getPrice();
    }

    @Test
    void calculateCostPerMile_withMaintenance_included() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg("Toyota", "Camry", "2018", null)).thenReturn(25.0);
        when(gas.getPrice()).thenReturn(3.50);

        // $40 oil change every 5000 miles => 40/5000 = 0.008 per mile
        model.MaintenanceCosts maintenance = new model.MaintenanceCosts(40.0, 5000);

        double cost = MileageCalculator.calculateCostPerMile(mpg, gas, "Toyota", "Camry", "2018", null, maintenance);
        assertEquals(3.50 / 25.0 + (40.0 / 5000.0), cost, 1e-9);

        verify(mpg).getMpg("Toyota", "Camry", "2018", null);
        verify(gas).getPrice();
    }
}
