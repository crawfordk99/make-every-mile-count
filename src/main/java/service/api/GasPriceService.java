package service.api;

public interface GasPriceService {
    double getPrice(String region, String fuelType) throws Exception;
}
