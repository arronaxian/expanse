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

    private Map<String,String> uriParameters = new HashMap<>();
    private Map<String,String> queryParameters = new HashMap<>();

    private final int port;

    private String resolvedURI;

    @Getter private boolean isOk = false;

    public RestClient(int port) {
        this.port = port;

        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    public static RestClient create(int port) {
        return new RestClient(port);
    }

    public final boolean put(String path, int ... validStatusCodes) {
        log.info("RestClient.put {}", path);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path)
                .port(port)
                .host(HOST)
                .scheme(SCHEME);

        HttpEntity<T> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // PUT
        final URI uri = builder.buildAndExpand(uriParameters).toUri();
        resolvedURI = uri.toString();

        response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        isOk = isResponseValid(validStatusCodes);

        return isOk;
    }

    public final boolean post(String path, T postHttpEntity, int ... validStatusCodes) {
        log.info("RestClient.post {}", path);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path)
                .port(port)
                .host(HOST)
                .scheme(SCHEME);

        HttpEntity<T> entity = new HttpEntity<>(postHttpEntity, headers);
        RestTemplate restTemplate = new RestTemplate();

        URI uri = builder.build().toUri();
        resolvedURI = uri.toString();

        response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        isOk = isResponseValid(validStatusCodes);

        return isOk;
    }

    public final Optional<T> get(String path, Class responseEntityClass, int ... validStatusCodes) {
        if ( log.isInfoEnabled() ) log.info("RestClient.get {}", path);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path)
                .port(port)
                .host(HOST)
                .scheme(SCHEME);

        HttpEntity<T> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        final URI uri = builder.buildAndExpand(uriParameters).toUri();
        resolvedURI = uri.toString();

        response = restTemplate.exchange(uri, HttpMethod.GET, entity, responseEntityClass);

        return isResponseValid(validStatusCodes) ? Optional.ofNullable((T)response.getBody()) : Optional.empty();
    }

    public void addURIParameters(String name, String value) {
        uriParameters.put(name, value);
    }

    public void addQueryParameters(String name, String value) { queryParameters.put(name, value); }

    public void addHeader(String name, @Nullable String value) {
        headers.add(name, value);
    }

    public Optional<T> getBody() {
        return Optional.of((T)response.getBody());
    }

    public int getStatusCode() {
        return response.getStatusCode().value();
    }

    public String toString() {
        return "RestClient :" + resolvedURI + " status :" + getStatusCode() + " success :" + isOk;
    }

    /**
     * Is the response valid for the provided status codes.
     * @param validStatusCodes an array of valid status codes.
     * @return True if valid, otherwise false.
     */
    private boolean isResponseValid(int[] validStatusCodes) {
        if ( validStatusCodes != null ) {
            // Validate the response codes.
            final int responseStatusCode = response.getStatusCodeValue();
            for (int validStatusCode : validStatusCodes) {
                if (responseStatusCode == validStatusCode) {
                    return true;
                }
            }

            log.warn("RestClient.isResponseValid invalid response status code {}",responseStatusCode);
        }


        return false;
    }
}
