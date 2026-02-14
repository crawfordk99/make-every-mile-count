package config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    @Bean
    Connection databaseConnection(@Value("${db.url}") String dbUrl,
                                  @Value("${db.user}") String dbUser,
                                  @Value("${db.password}") String dbPassword) throws Exception {
        // If no DB URL is provided (e.g. running without DB), skip creating the bean
        if (dbUrl == null || dbUrl.isBlank()) {
            log.info("DB URL is not set; skipping database Connection bean creation.");
            return null;
        }

        // Configure and return your database connection here
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
    // @Bean
    // PreparedStatement preparedStatement(Connection connection) {
    //     // Configure and return your prepared statement here
    //     return connection.prepareStatement(sql); // Replace with actual SQL query
    // }
}
