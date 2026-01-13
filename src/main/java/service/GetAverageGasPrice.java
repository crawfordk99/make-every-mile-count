package service;

import java.util.List;


public class GetAverageGasPrice
{
    private String _region;
    private List<String> _regions ['Waukegan', 'Houston', 'New York', 'Los Angeles', 'Salt Lake City', 'Miami', 'Omaha'];
    
    public GetAverageGasPrice()
    {
    }
    
    public GetAverageGasPrice(String region)
    {
        this._region = region;
    }

    public double getRegionAverageGasPrice()
    {


        // Placeholder: returns a fixed average gas price until real service is wired in
        return 3.79;
    }

    // Placeholder: returns a fixed average gas price until real service is wired in
    public double fetch()
    {
        return 3.59;
    }
}