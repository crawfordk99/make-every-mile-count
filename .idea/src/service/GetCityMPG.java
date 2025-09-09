import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.io.FileInputStream;

public class GetCityMPG
{

    public double getCityMpg(String make, String model, String year) throws Exception
    {
        // Since I want the user to always at least give these three parameters
        // I won't worry about giving optional parameters for now.
        String url = "https://api.api-ninjas.com/v1/cars?model=" + model +
                "&year=" + year +
                "&make=" + make;
        Properties prop = new Properties();
        prop.load(new FileInputStream("config.properties"));

        String apiKey = prop.getProperty("NINJA_API_KEY");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URL(url))
                .header("Accept", "application/json")
                .header("X-API-KEY", apiKey)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        return 0.00;
    }
}