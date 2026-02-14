package config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient(); // This way we don't have to create new HttpClient instances in every API service
    }
}
