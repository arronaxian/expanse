package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.utility.RestClient;
import com.ds.expanse.command.model.spi.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Player microservice commands.
 */
public interface CommandPlayer {
    Logger log = LogManager.getLogger(CommandPlayer.class);
    int PORT_NUMBER = 9090;

    enum Sequence { none, register, move, find, view };

    /**
     * Creates a new Player.
     */
    Command create = (context) -> {
        if ( log.isInfoEnabled() ) log.info("CommandPlayer.create start");

        final RestClient<Player> restClient = RestClient.create(PORT_NUMBER, "/player/register");

        CommandResult result = CommandResult.badRequest("CommandPlayer.create");
        DefaultContext.of(context)
                .ifPresent(defaultContext -> {
                    defaultContext.getSecurityAdapter().addSecurity(restClient);
                    restClient.post(defaultContext.getPlayer(), 201);
                });

        result.setOutcome(restClient.getStatusCode(), restClient.isValidStatus());

        if ( log.isInfoEnabled() ) log.info("CommandPlayer.create complete {}", restClient);

        return result;
    };

    /**
     * Sets an existing Player to new values (i.e., an update).
     */
    Command set = (context) -> {
        if ( log.isInfoEnabled() ) log.info("CommandPlayer.set");

        final RestClient<Player> restClient = RestClient.create(PORT_NUMBER, "/player");

        CommandResult result = CommandResult.badRequest("CommandPlayer.set");
        DefaultContext.of(context)
                .ifPresent(defaultContext -> {
                    defaultContext.getSecurityAdapter().addSecurity(restClient);
                    restClient.post(defaultContext.getPlayer(), 200);
                });

        result.setOutcome(restClient.getStatusCode(), restClient.isValidStatus());

        if ( log.isInfoEnabled() ) log.info("CommandPlayer.set {}", restClient);

        return result;
    };

    /**
     * Searches an existing Player by Name.
     */
    Command search = (context) -> {
        if ( log.isInfoEnabled() ) log.info("CommandPlayer.search");

        final RestClient<Player> restClient = RestClient.create(PORT_NUMBER, "/player");

        CommandResult result = CommandResult.badRequest("CommandPlayer.search");
        DefaultContext.of(context)
                .ifPresent(defaultContext -> {
                    defaultContext.getSecurityAdapter().addSecurity(restClient);
                    restClient.addQueryParameter("id", defaultContext.getPlayer().getId());
                    restClient.addQueryParameter("name", defaultContext.getPlayer().getName());
                    restClient.get(Player.class,200)
                        .ifPresent(player -> {
                            defaultContext.setPlayer(player);
                        });
                });

        result.setOutcome(restClient.getStatusCode(), restClient.isValidStatus());

        if ( log.isInfoEnabled() ) log.info("CommandPlayer.search {}", restClient);

        return result;
    };
}
