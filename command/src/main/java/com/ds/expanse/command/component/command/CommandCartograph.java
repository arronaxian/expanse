package com.ds.expanse.command.component.command;

import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Command cartograph requests data for mappings.
 */
public interface CommandCartograph {
    public static final int PORT_NUMBER = 9092;
    public static final String HOST = "localhost";
    public static final String SCHEME = "http";

    Command heading = (context) -> {
        String URL_MOVE = "cartograph/heading";

        DefaultContext defaultContext = (DefaultContext)context;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(URL_MOVE)
                .port(PORT_NUMBER)
                .host(HOST)
                .scheme(SCHEME)
                .queryParam("x", defaultContext.getPlayer().getPosition().getX())
                .queryParam("y", defaultContext.getPlayer().getPosition().getY())
                .queryParam("h", defaultContext.getCommand());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MapGrid> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, MapGrid.class);

        if ( response.hasBody() ) {
            defaultContext.setMapGrid(response.getBody());
        }

        boolean isOk = ((ResponseEntity<MapGrid>) response).getStatusCode() == HttpStatus.OK;
        int code = ((ResponseEntity<MapGrid>) response).getStatusCode().value();

        return new CommandResult("CommandCartograph.heading", code, isOk);
    };
}
