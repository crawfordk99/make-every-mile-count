package service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.api.CityMpgService;

public class GetCityMPG implements CityMpgService
{
    // Fetch city MPG; fallback to combined MPG if city not available
    // API: https://carapi.app/api/mileages/v2
    // allows for just three parameters: year, make, model
    @Override
    public double getMpg(String make, String model, String year) throws Exception
    {
        return getMpg(make, model, year, null);
    }

    @Override
    public double getMpg(String make, String model, String year, String submodel) throws Exception
    {
        String url = "https://carapi.app/api/mileages/v2?year=" + year +
                "&make=" + make +
                "&model=" + model;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        // Response structure: { "data": [...], "collection": {...} }
        JsonNode dataArray = root.has("data") ? root.get("data") : root;

        // Validate data array
        if (!dataArray.isArray() || dataArray.size() == 0)
        {
            return 0.00;
        }

        JsonNode carNode = null;

        // If submodel specified, find exact match; otherwise use first result
        if (submodel != null && !submodel.isEmpty())
        {
            for (int i = 0; i < dataArray.size(); i++)
            {
                JsonNode node = dataArray.get(i);
                if (node.has("submodel") && submodel.equalsIgnoreCase(node.get("submodel").asText()))
                {
                    carNode = node;
                    break;
                }
            }
        }

        // Fall back to first result if submodel not specified or not found
        if (carNode == null)
        {
            carNode = dataArray.get(0);
        }

        // Try city MPG first, fall back to combined
        if (carNode.has("epa_city_mpg"))
        {
            JsonNode cityMpg = carNode.get("epa_city_mpg");
            if (!cityMpg.isNull())
            {
                return cityMpg.asDouble();
            }
        }

        // Fall back to combined MPG
        if (carNode.has("combined_mpg"))
        {
            JsonNode combinedMpg = carNode.get("combined_mpg");
            if (!combinedMpg.isNull())
            {
                return combinedMpg.asDouble();
            }
        }

        return 0.00;
    }
}
