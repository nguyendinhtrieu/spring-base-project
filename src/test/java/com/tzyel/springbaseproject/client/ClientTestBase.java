package com.tzyel.springbaseproject.client;

import com.tzyel.springbaseproject.util.AuthenticationTestUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class ClientTestBase {
    @LocalServerPort
    private int localServerPort;

    protected String getServerHost() {
        return "http://localhost:" + localServerPort;
    }

    /**
     * Generates a JWT Bearer token for a member user with predefined email and phone.
     *
     * @return Bearer token for a member user.
     */
    protected String generateMemberAuthorizationToken() {
        return "Bearer " + AuthenticationTestUtil.generateMemberToken();
    }

    /**
     * Generates a JWT Bearer token for a regular user with predefined email and phone.
     *
     * @return Bearer token for a regular user.
     */
    protected String generateUserAuthorizationToken() {
        return "Bearer " + AuthenticationTestUtil.generateUserAuthorizationToken();
    }

    /**
     * Generates a JWT Bearer token for an admin user with predefined email and phone.
     *
     * @return Bearer token for an admin user.
     */
    protected String generateAdminAuthorizationToken() {
        return "Bearer " + AuthenticationTestUtil.generateAdminAuthorizationToken();
    }
}
