package com.tzyel.springbaseproject.util;

import com.tzyel.springbaseproject.service.ApplicationContextProvider;
import com.tzyel.springbaseproject.service.JwtService;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating authentication tokens for testing purposes.
 */
public class AuthenticationTestUtil {

    /**
     * Retrieves the JwtService instance from the application context.
     *
     * @return JwtService instance.
     */
    private static JwtService getJwtService() {
        return ApplicationContextProvider.getBeanFromStatic(JwtService.class);
    }

    /**
     * Generates a JWT Bearer token for a member user with predefined info.
     *
     * @return Bearer token for a member user.
     */
    public static String generateMemberToken() {
        return getJwtService()
                .tokenCreator(getUserClaims("sbp-member@gmail.com", "09876543210"), "member");
    }

    /**
     * Generates a JWT Bearer token for a regular user with predefined info.
     *
     * @return Bearer token for a regular user.
     */
    public static String generateUserAuthorizationToken() {
        return getJwtService()
                .tokenCreator(getUserClaims("sbp-user@gmail.com", "09876543210"), "user");
    }

    /**
     * Generates a JWT Bearer token for an admin user with predefined info.
     *
     * @return Bearer token for an admin user.
     */
    public static String generateAdminAuthorizationToken() {
        return getJwtService()
                .tokenCreator(getUserClaims("sbp-admin@gmail.com", "09876543210"), "admin");
    }

    /**
     * Creates a map of user claims based on the provided email and phone.
     *
     * @param email The user's email.
     * @param phone The user's phone number.
     * @return Map of user claims.
     */
    private static Map<String, Object> getUserClaims(String email, String phone) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("phone", phone);
        return claims;
    }
}
