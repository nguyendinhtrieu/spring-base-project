package com.tzyel.springbaseproject.client.rest_template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tzyel.springbaseproject.service.ApplicationContextProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RestTemplateBuilder {
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private static final int DEFAULT_TIMEOUT = 3 * 1000;

    private final RestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();
    private String url;
    private Integer timeout;
    private HttpMethod method = HttpMethod.GET;
    private Map<String, String> requestParams = new HashMap<>();
    private Object body;

    private RestTemplateBuilder(RestTemplate restTemplate) {
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate();
            //noinspection NullableProblems
            this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse response) {
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse response) {
                }
            });
        } else {
            this.restTemplate = restTemplate;
        }
        withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    public static RestTemplateBuilder init() {
        return new RestTemplateBuilder(null);
    }

    public static RestTemplateBuilder init(RestTemplate restTemplate) {
        return new RestTemplateBuilder(restTemplate);
    }

    public static RestTemplateBuilder init(String restTemplateBeanName) {
        return new RestTemplateBuilder(ApplicationContextProvider.getBeanFromStatic(restTemplateBeanName, RestTemplate.class));
    }

    public RestTemplateBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RestTemplateBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public RestTemplateBuilder withMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public RestTemplateBuilder withHeader(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    public RestTemplateBuilder withRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public RestTemplateBuilder withRequestParams(String paramName, String value) {
        requestParams.put(paramName, value);
        return this;
    }

    @SneakyThrows
    public RestTemplateBuilder withBody(Object body) {
        this.body = body instanceof String ? body : OBJECT_WRITER.writeValueAsString(body);
        return this;
    }

    /**
     * Executes an HTTP request and returns the response body as a String.
     * <p>
     * This method is specifically designed for handling responses where the body content is expected to be in a String format.
     * It simplifies the handling of HTTP responses when the expected response type is a textual representation.
     * <p>
     * Example Usage:
     * RestTemplateBuilder.init()
     * .withMethod(HttpMethod.GET)
     * .withUrl(url)
     * // Other configurations...
     * .execute();
     *
     * @return ResponseEntity encapsulating the HTTP response with the response body as a String.
     * The response entity contains the String response body along with status information.
     */
    public ResponseEntity<String> execute() {
        buildTimeout();
        URI uri = buildUriWithParams();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, method, entity, String.class);
    }

    /**
     * Executes an HTTP request and returns the response body as the specified type inferred by a Class reference.
     * <p>
     * This method facilitates executing HTTP requests with a straightforward return type inference.
     * It's suitable for scenarios where the response body's type is straightforward and does not involve complex generics.
     * <p>
     * Example Usage:
     * RestTemplateBuilder.init()
     * .withMethod(HttpMethod.GET)
     * .withUrl(url)
     * // Other configurations...
     * .execute(ObjectDto.class);
     *
     * @param <T>          The inferred type of the response body. Use as Class<YourType>.
     * @param responseType The Class reference representing the expected response type.
     *                     Example usage: ObjectDto.class for a response type of ObjectDto.
     * @return ResponseEntity encapsulating the HTTP response with the specified response type.
     */
    public <T> ResponseEntity<T> execute(Class<T> responseType) {
        buildTimeout();
        URI uri = buildUriWithParams();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, method, entity, responseType);
    }

    /**
     * Executes an HTTP request and infers the complex generic response type based on a provided ParameterizedTypeReference.
     * <p>
     * This method is designed for handling complex generic response types, such as lists of custom objects or specific parameterized types.
     * It is particularly useful when using RestTemplateBuilder to construct HTTP requests that require complex generic responses.
     * <p>
     * Example Usage:
     * RestTemplateBuilder.init()
     * .withMethod(HttpMethod.GET)
     * .withUrl(url)
     * // Other configurations...
     * .execute(new ParameterizedTypeReference<List<ObjectDto>>() {});
     *
     * @param <T>           The inferred type of the response body. Use as new ParameterizedTypeReference<YourType>() {}.
     * @param typeReference The ParameterizedTypeReference representing the expected response type.
     *                      Example usage: new ParameterizedTypeReference<List<ObjectDto>>() {} for a List of ObjectDto.
     * @return ResponseEntity encapsulating the HTTP response with the inferred complex generic type.
     * Ensure to specify the desired response type when calling this method.
     */
    public <T> ResponseEntity<T> execute(ParameterizedTypeReference<T> typeReference) {
        buildTimeout();
        URI uri = buildUriWithParams();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, method, entity, typeReference);
    }

    private URI buildUriWithParams() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (requestParams != null && !requestParams.isEmpty()) {
            requestParams.forEach(uriBuilder::queryParam);
        }

        return uriBuilder.build().toUri();
    }

    private void buildTimeout() {
        if (timeout == null) {
            timeout = DEFAULT_TIMEOUT;
        }
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);
    }
}
