package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.utility.RestClient;
import com.ds.expanse.command.model.spi.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface CommandUser {
    Logger log = LogManager.getLogger(CommandUser.class);
    int PORT_NUMBER = 8080;

    /**
     * Adds the Player Id to the User
     */
    Command addPlayer = (context) -> {
        log.info("CommandUser.addPlayer");

        final RestClient restClient = RestClient.create(PORT_NUMBER, "user/{username}/player/{id}");

        CommandResult result = new CommandResult("CommandUser.addPlayer");
        DefaultContext.of(context)
                .ifPresent(defaultContext -> {
                    defaultContext.getSecurityAdapter().addSecurity(restClient);
                    restClient.addURIParameters("username",
                            defaultContext.getSecurityAdapter().getUserName());
                    restClient.addURIParameters("id", defaultContext.getPlayer().getId());
                    restClient.put( 200);
                });

        result.setOutcome(restClient.getStatusCode(), restClient.isValidStatus());

        if (log.isInfoEnabled() ) log.info("CommandUser.addPlayer {}", restClient.isValidStatus());

        return result;
    };

    /**
     * Gets the Player Id for the User
     */
    Command getPlayer = (context) -> {
        log.info("CommandUser.getPlayer");

        final RestClient<List<String>> restClient = RestClient.create(PORT_NUMBER, "user/{username}/player");

        CommandResult result = new CommandResult("CommandUser.getPlayer");
        DefaultContext.of(context)
                .ifPresent(defaultContext -> {
                    defaultContext.getSecurityAdapter().addSecurity(restClient);
                    restClient.addURIParameters("username",
                            defaultContext.getSecurityAdapter().getUserName());

                    restClient.get( List.class, 200)
                            .ifPresent(playerIds -> {
                                final Player player = new Player();
                                player.setId(playerIds.get(0));

                                defaultContext.setPlayer(player);

                                defaultContext.getEngineAdapter().addPlayer(player);
                            });
                });

        result.setOutcome(restClient.getStatusCode(), restClient.isValidStatus());

        if (log.isInfoEnabled() ) log.info("CommandUser.addPlayer {}", restClient.isValidStatus());

        return result;
    };
}
