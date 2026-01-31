package service.api;

public interface CityMpgService {
    double getMpg(String make, String model, String year) throws Exception;
    double getMpg(String make, String model, String year, String submodel) throws Exception;
}
