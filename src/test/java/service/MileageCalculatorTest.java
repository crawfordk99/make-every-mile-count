package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import model.MaintenanceCosts;
import service.api.CityMpgService;
import service.api.GasPriceService;
import service.impl.MileageCalculator;

class MileageCalculatorTest {

    @Test
    void calculateCostPerMile_success() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg("Toyota", "Camry", "2018", null)).thenReturn(25.0);
        when(gas.getPrice("region", "fuelType")).thenReturn(3.50);

        MileageCalculator calc = new MileageCalculator(mpg, gas);
        double cost = calc.calculateCostPerMile("Toyota", "Camry", "2018", null, "region", "fuelType");
        assertEquals(3.50 / 25.0, cost, 1e-9);

        verify(mpg).getMpg("Toyota", "Camry", "2018", null);
        verify(gas).getPrice("region", "fuelType");
    }

    @Test
    void calculateCostPerMile_missingMpg_returnsZero() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg(anyString(), anyString(), anyString(), any())).thenReturn(0.0);
        // gas price should not be called when mpg is missing, but stub anyway
        when(gas.getPrice(anyString(), anyString())).thenReturn(3.50);

        MileageCalculator calc2 = new MileageCalculator(mpg, gas);
        double cost = calc2.calculateCostPerMile("Ford", "Fiesta", "2016", null, "r", "f");
        assertEquals(0.0, cost, 1e-9);

        verify(mpg).getMpg(anyString(), anyString(), anyString(), any());
        verify(gas, never()).getPrice(anyString(), anyString());
    }

    @Test
    void calculateCostPerMile_missingGasPrice_returnsZero() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg(anyString(), anyString(), anyString(), any())).thenReturn(20.0);
        when(gas.getPrice(anyString(), anyString())).thenReturn(0.0);

        MileageCalculator calc3 = new MileageCalculator(mpg, gas);
        double cost = calc3.calculateCostPerMile("Honda", "Civic", "2017", null, "region", "fuelType");
        assertEquals(0.0, cost, 1e-9);

        verify(mpg).getMpg(anyString(), anyString(), anyString(), any());
        verify(gas).getPrice(anyString(), anyString());
    }

    @Test
    void calculateCostPerMile_withMaintenance_included() throws Exception {
        CityMpgService mpg = mock(CityMpgService.class);
        GasPriceService gas = mock(GasPriceService.class);

        when(mpg.getMpg("Toyota", "Camry", "2018", null)).thenReturn(25.0);
        when(gas.getPrice("region", "fuelType")).thenReturn(3.50);

        // $40 oil change every 5000 miles => 40/5000 = 0.008 per mile
        MaintenanceCosts maintenance = new MaintenanceCosts(40.0, 5000);

        MileageCalculator calc4 = new MileageCalculator(mpg, gas);
        double cost = calc4.calculateCostPerMile("Toyota", "Camry", "2018", null, "region", "fuelType", maintenance);
        assertEquals(3.50 / 25.0 + (40.0 / 5000.0), cost, 1e-9);

        verify(mpg).getMpg("Toyota", "Camry", "2018", null);
        verify(gas).getPrice("region", "fuelType");
    }
}
