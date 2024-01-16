package com.tzyel.springbaseproject.integration;

import com.tzyel.springbaseproject.UnitTestConfiguration;
import com.tzyel.springbaseproject.util.AuthenticationTestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.function.Consumer;

/**
 * The {@link IntegrationTestBase} class serves as the base class for integration tests in the Spring Boot application.
 * It provides common configurations and utility methods for setting up the test environment, including a WebTestClient
 * instance and a DatabaseVerifier instance for database verifications.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(classes = UnitTestConfiguration.class)
@SqlGroup({
        @Sql(scripts = {"classpath:integration/sql/setup_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = {"classpath:integration/sql/teardown_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class IntegrationTestBase {
    protected WebTestClient webTestClient;
    @LocalServerPort
    private int localServerPort;
    private DatabaseVerifier databaseVerifier;

    @Autowired
    private DataSource dataSource;

    /**
     * Set up the test environment before each test case.
     */
    @BeforeEach
    public void setup() {
        setupWebTestClient();
    }

    /**
     * Clean up resources after each test case.
     */
    @BeforeEach
    public void after() {
    }

    /**
     * Provides a fluent interface for verifying database records in integration tests.
     *
     * @return The {@link DatabaseVerifier} instance for performing database verifications.
     */
    protected DatabaseVerifier verifyDatabase() {
        setupDatabaseVerifier();
        return databaseVerifier;
    }

    /**
     * Configures HTTP headers with Bearer token for a member user.
     *
     * @return Consumer for configuring HTTP headers.
     */
    protected Consumer<HttpHeaders> withMember() {
        return httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationTestUtil.generateMemberToken());
    }

    /**
     * Configures HTTP headers with Bearer token for a regular user.
     *
     * @return Consumer for configuring HTTP headers.
     */
    protected Consumer<HttpHeaders> withUser() {
        return httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationTestUtil.generateUserAuthorizationToken());
    }

    /**
     * Configures HTTP headers with Bearer token for an admin user.
     *
     * @return Consumer for configuring HTTP headers.
     */
    protected Consumer<HttpHeaders> withAdmin() {
        return httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationTestUtil.generateAdminAuthorizationToken());
    }

    /**
     * Execute SQL scripts to set up initial data in the database.
     */
    @SneakyThrows
    private void setupData() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("integration/sql/setup_data.sql"));
        }
    }

    /**
     * Set up the {@link DatabaseVerifier} instance for performing database verifications.
     */
    private void setupDatabaseVerifier() {
        if (databaseVerifier == null) {
            databaseVerifier = new DatabaseVerifier(dataSource);
        }
    }

    /**
     * Set up the {@link WebTestClient} instance for making web requests in integration tests.
     */
    private void setupWebTestClient() {
        if (webTestClient == null) {
            webTestClient = WebTestClient
                    .bindToServer()
                    .baseUrl(getServerHost())
                    .build();
        }
    }

    /**
     * Get the base URL of the local server for making web requests.
     *
     * @return The base URL in the format {@code http://localhost:{localServerPort}}.
     */
    private String getServerHost() {
        return "http://localhost:" + localServerPort;
    }
}
