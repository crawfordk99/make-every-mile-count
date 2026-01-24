package service;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Region;

public class GetAverageGasPrice
{
    private Region _region;
    private String _gasolineType; // e.g., "EPMR" for Regular, "EPMPU" for Premium
    private static final String EIA_BASE_URL = "https://api.eia.gov/v2/petroleum/pri/gnd/data/";

    public GetAverageGasPrice()
    {
        this(Region.US_NATIONAL, "EPMR"); // Default: US National, Regular Gasoline
    }

    public GetAverageGasPrice(Region region)
    {
        this(region, "EPMR"); // Default to Regular Gasoline
    }

    public GetAverageGasPrice(Region region, String gasolineType)
    {
        this._region = region;
        this._gasolineType = gasolineType;
    }

    /**
     * Fetch the latest gas price for the configured region and gasoline type.
     * @return price in $/gallon, or 0.0 if fetch fails
     */
    public double getPrice() throws Exception
    {
        String apiKey = loadApiKey();
        if (apiKey == null || apiKey.isEmpty())
        {
            System.err.println("ERROR: EIA_API_KEY not found in config.properties or environment");
            return 0.0;
        }

        // Build query parameters for recent weekly data
        // LocalDate endDate = LocalDate.now();
        // LocalDate startDate = endDate.minusDays(30); // Look back 30 days
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Build URL with proper encoding
        StringBuilder urlBuilder = new StringBuilder(EIA_BASE_URL);
        urlBuilder.append("?frequency=weekly");
        urlBuilder.append("&data[0]=value");
        urlBuilder.append("&facets[duoarea][0]=").append(_region.getDuoAreaCode());
        urlBuilder.append("&facets[product][0]=").append(URLEncoder.encode(_gasolineType, StandardCharsets.UTF_8));
        urlBuilder.append("&sort[0][column]=period");
        urlBuilder.append("&sort[0][direction]=desc");
        urlBuilder.append("&length=100");
        urlBuilder.append("&api_key=").append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

        String url = urlBuilder.toString();
        System.out.println("Calling EIA API...");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
        {
            System.err.println("ERROR: EIA API returned status " + response.statusCode());
            System.err.println("Response: " + response.body().substring(0, Math.min(200, response.body().length())));
            return 0.0;
        }

        return parsePrice(response.body());
    }

    /**
     * Parse JSON response and extract the latest gas price.
     */
    private double parsePrice(String jsonResponse) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);

        JsonNode dataArray = root.path("response").path("data");

        if (!dataArray.isArray() || dataArray.size() == 0)
        {
            System.err.println("ERROR: No data found in EIA response");
            return 0.0;
        }

        // Get the first (most recent) entry
        JsonNode latestEntry = dataArray.get(0);
        JsonNode valueNode = latestEntry.path("value");

        if (valueNode.isNull())
        {
            System.err.println("ERROR: Missing price value in EIA response");
            return 0.0;
        }

        double price = valueNode.asDouble();
        String period = latestEntry.path("period").asText();
        System.out.println("Fetched gas price for " + _region.getDisplayName() + 
                           " (" + _gasolineType + ") on " + period + ": $" + price);

        return price;
    }

    /**
     * Load API key from config.properties or environment variable.
     */
    private String loadApiKey()
    {
        // Try environment variable first
        String envKey = System.getenv("EIA_API_KEY");
        if (envKey != null && !envKey.isEmpty())
        {
            return envKey.replaceAll("\"", "").trim();
        }

        // Fall back to config.properties
        try
        {
            Properties prop = new Properties();
            prop.load(new FileInputStream("config.properties"));
            String key = prop.getProperty("EIA_API_KEY");
            if (key != null)
            {
                return key.replaceAll("\"", "").trim();
            }
        }
        catch (Exception e)
        {
            System.err.println("WARNING: Could not load config.properties: " + e.getMessage());
        }
        return null;
    }
}