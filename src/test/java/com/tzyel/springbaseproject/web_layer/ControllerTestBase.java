package com.tzyel.springbaseproject.web_layer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzyel.springbaseproject.config.authentication.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Base class for controller tests with common utility methods.
 */

@AutoConfigureMockMvc
public class ControllerTestBase extends WebLayerTestBase {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected MockMvc mockMvc;

    /**
     * Converts an object to its JSON representation.
     *
     * @param object The object to be converted to JSON.
     * @return JSON representation of the object as a String.
     * @throws IOException If there is an issue converting the object to JSON.
     */
    protected String mapToJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Extracts the response content from MvcResult and converts it into an object of the specified type.
     *
     * @param mvcResult       The result of the MVC request.
     * @param targetClassType The class type of the target object.
     * @param <T>             The type of the target object.
     * @return An object of the specified type.
     * @throws IOException If there is an issue reading the response content.
     */
    protected <T> T getResponseObject(MvcResult mvcResult, Class<T> targetClassType) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, targetClassType);
    }

    /**
     * Extracts the response content from MvcResult and converts it into an object of the specified type.
     *
     * @param mvcResult     The result of the MVC request.
     * @param typeReference A Jackson TypeReference representing the target type.
     * @param <T>           The type of the target object.
     * @return An object of the specified type.
     * @throws IOException If there is an issue reading the response content.
     */
    protected <T> T getResponseObject(MvcResult mvcResult, TypeReference<T> typeReference) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, typeReference);
    }

    /**
     * Extracts the response content from MvcResult and converts it into a list of the specified type.
     *
     * @param mvcResult        The result of the MVC request.
     * @param elementClassType The class type of the elements in the list.
     * @param <T>              The type of the elements in the list.
     * @return A list of objects of the specified type.
     * @throws IOException If there is an issue reading the response content.
     */
    protected <T> List<T> getResponseList(MvcResult mvcResult, Class<T> elementClassType) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, elementClassType));
    }

    /**
     * Creates a mock {@link Principal} object for a member user.
     * This method is specifically designed for use with standalone MockMvc testing.
     *
     * @return Principal for a member user.
     */
    protected Principal memberPrincipal() {
        return new UsernamePasswordAuthenticationToken(
                createAppUserDetails("member", "MEMBER"),
                null,
                null
        );
    }

    /**
     * Creates a mock {@link Principal} object for a regular user.
     * This method is specifically designed for use with standalone MockMvc testing.
     *
     * @return Principal for a regular user.
     */
    protected Principal userPrincipal() {
        return new UsernamePasswordAuthenticationToken(
                createAppUserDetails("user", "USER"),
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }

    /**
     * Creates a mock {@link Principal} object for an admin user.
     * This method is specifically designed for use with standalone MockMvc testing.
     *
     * @return Principal for an admin user.
     */
    protected Principal adminPrincipal() {
        return new UsernamePasswordAuthenticationToken(
                createAppUserDetails("admin", "ADMIN"),
                null,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN")
        );
    }

    private AppUserDetails createAppUserDetails(String username, String role) {
        return new AppUserDetails(
                username,
                "$2a$10$jXN83qi9EG8qPsw8AiJ7TOTWl/q/d5HW8B/hjhQF5Gqz97z4OLyii",
                getAuthorities(role)
        );
    }

    private List<GrantedAuthority> getAuthorities(String role) {
        return AuthorityUtils.createAuthorityList("ROLE_" + role);
    }
}
