package com.tzyel.springbaseproject.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Value("${spring.flyway.enabled}")
    private boolean flywayEnabled;
    @Value("${spring.flyway.url}")
    private String flywayUrl;
    @Value("${spring.flyway.user}")
    private String flywayUser;
    @Value("${spring.flyway.password}")
    private String flywayPassword;
    @Value("${spring.flyway.locations}")
    private String flywayLocations;
    @Value("${spring.flyway.default-schema}")
    private String flywayDefaultSchema;

    @Bean
    public Flyway flyway(org.flywaydb.core.api.configuration.Configuration configuration) {
        return new Flyway(configuration);
    }

    @Bean
    public org.flywaydb.core.api.configuration.Configuration flywayConfiguration() {
        ClassicConfiguration classicConfiguration = new ClassicConfiguration();
        classicConfiguration.setUrl(flywayUrl);
        classicConfiguration.setUser(flywayUser);
        classicConfiguration.setPassword(flywayPassword);
        classicConfiguration.setLocations(new Location(flywayLocations));
        classicConfiguration.setDefaultSchema(flywayDefaultSchema);
        return classicConfiguration;
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            if (!flywayEnabled && flyway.info().pending().length > 0) {
                throw new RuntimeException("Database schema requires migration. Please enable Flyway migration.");
            }
            flyway.migrate();
        };
    }

    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway, FlywayMigrationStrategy migrationStrategy) {
        return new FlywayMigrationInitializer(flyway, migrationStrategy);
    }
}
