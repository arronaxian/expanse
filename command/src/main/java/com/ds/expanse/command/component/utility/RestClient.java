package com.ds.expanse.command.component.utility;

import lombok.Getter;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class RestClient<T> {
    private final static Logger log = LogManager.getLogger(RestClient.class);

    public final static String SCHEME = "http";
    public final static String HOST = "localhost";

    private HttpHeaders headers = new HttpHeaders();
    @Getter private ResponseEntity response;

    private Map<String,Object> uriParameters = new HashMap<>();

    private String resolvedURI = "<not set>";

    @Getter private boolean isValidStatus = false;

    private UriComponentsBuilder builder;

    RestClient() {
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    RestClient(int port, String path) {
        this();

        builder = UriComponentsBuilder.fromPath(path)
                .port(port)
                .host(HOST)
                .scheme(SCHEME);
    }

    /**
     * Creates a RestClient for the port and path.
     * @param port The port of the external resource.
     * @param path The path to the external resource.
     * @return
     */
    public static RestClient create(int port, String path) {
        return new RestClient(port, path);
    }

    public final boolean put(int ... validStatusCodes) {
        HttpEntity<T> entity = new HttpEntity<>(headers);

        // PUT
        final URI uri = builder.buildAndExpand(uriParameters).toUri();
        resolvedURI = uri.toString();

        log.info("RestClient.put {}", resolvedURI);

        response = new RestTemplate().exchange(uri, HttpMethod.PUT, entity, String.class);

        isValidStatus = isResponseValid(validStatusCodes);

        return isValidStatus;
    }

    public final boolean post(T postHttpEntity, int ... validStatusCodes) {
        HttpEntity<T> entity = new HttpEntity<>(postHttpEntity, headers);

        URI uri = builder.build().toUri();
        resolvedURI = uri.toString();

        response = new RestTemplate().exchange(uri, HttpMethod.POST, entity, String.class);

        isValidStatus = isResponseValid(validStatusCodes);

        log.info("RestClient.post {}", this::toString);

        return isValidStatus;
    }

    /**
     * Gets the requested entity class.
     * @param responseEntityClass The expected response entity class.
     * @param validStatusCodes The set of valid outcome codes.
     * @return The Optional wrapped entity response.
     */
    public final Optional<T> get(Class responseEntityClass, int ... validStatusCodes) {
        final HttpEntity<T> entity = new HttpEntity<>(headers);

        final URI uri = builder.buildAndExpand(uriParameters).toUri();
        resolvedURI = uri.toString();

        response = new RestTemplate().exchange(uri, HttpMethod.GET, entity, responseEntityClass);

        isValidStatus = isResponseValid(validStatusCodes) && response.hasBody();

        if ( log.isInfoEnabled() ) log.info("RestClient.get {}", resolvedURI);

        return isValidStatus ? Optional.ofNullable((T)response.getBody()) : Optional.empty();
    }

    /**
     * Adds a new URI parameter where path is /path1/{name}/path2 becomes /path1/foobar/path2
     *
     * @param name The {name} to substitute with value
     * @param value The value for the path element.
     */
    public void addURIParameters(String name, Object value) {
        uriParameters.put(name, value);
    }

    /**
     * Adds a query parameter "?name1=value1&name2=value2&...nameN=valueN'
     * @param name The name of the query parameter
     * @param value The value of the query parameter
     */
    public void addQueryParameter(String name, Object value) {
        builder.queryParam(name, value);
    }

    public void addHeader(String name, @Nullable String value) {
        headers.add(name, value);
    }

    public Optional<T> getBody() {
        return Optional.ofNullable((T)response.getBody());
    }

    public int getStatusCode() {
        if ( response == null ) {
            return 0;
        } else {
            return response.getStatusCode().value();
        }
    }

    public String toString() {
        return "RestClient :" + resolvedURI + " outcome :" + getStatusCode() + " success :" + isValidStatus;
    }

    /**
     * Is tehe response valid.
     * @param validStatusCodes The valid outcome codes.
     * @return True if valid, otherwise false.
     */
    private boolean isResponseValid(int[] validStatusCodes) {
        return isResponseValid(validStatusCodes, false);
    }

    /**
     * Is the response valid for the provided outcome codes.
     * @param validStatusCodes an array of valid outcome codes.
     * @return True if valid, otherwise false.
     */
    private boolean isResponseValid(int[] validStatusCodes, boolean expectBody) {
        if ( expectBody && !response.hasBody() ) {
            return false;
        }

        if ( validStatusCodes != null ) {
            // Validate the response codes.
            final int responseStatusCode = response.getStatusCodeValue();
            for (int validStatusCode : validStatusCodes) {
                if (responseStatusCode == validStatusCode) {
                    return true;
                }
            }

            log.warn("RestClient.isResponseValid invalid response outcome code {}",responseStatusCode);
        }


        return false;
    }
}
