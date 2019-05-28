package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.utility.RestClient;
import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Command cartograph requests data for mappings.
 */
public interface CommandCartograph {
    Logger log = LogManager.getLogger(CommandCartograph.class);
    int PORT_NUMBER = 9092;

    Command heading = (context) -> {
        CommandResult  result = new CommandResult("CommandCartograph.heading");

        DefaultContext.fromContext(context).ifPresent(defaultContext -> {
            RestClient<MapGrid> restClient = RestClient.create(PORT_NUMBER, "cartograph/heading");
            restClient.addQueryParameter("x", defaultContext.getPlayer().getPosition().getX());
            restClient.addQueryParameter("y", defaultContext.getPlayer().getPosition().getY());
            restClient.addQueryParameter("h", defaultContext.getCommand());

            restClient.get(MapGrid.class, 200)
                    .ifPresent(mapGrid -> {
                        defaultContext.setMapGrid(mapGrid);
                        result.setOutcome(200, true);
                    });
        });

        return result;
    };
}
