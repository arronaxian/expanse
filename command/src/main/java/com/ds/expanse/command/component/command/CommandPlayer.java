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
    final int PORT_NUMBER = 9090;

    /**
     * Creates a new Player.
     */
    Command create = (context) -> {
        if ( log.isInfoEnabled() ) log.info("CommandPlayer.create start");

        final RestClient restClient = RestClient.create(PORT_NUMBER, "/player/register");

        CommandResult result = new CommandResult("CommandPlayer.create");
        DefaultContext.fromContext(context)
                .ifPresent(defaultContext -> {
                    result.setSuccess(restClient.post(defaultContext.getPlayer(), 201));
                });

        result.setCode(restClient.getStatusCode());

        if ( log.isInfoEnabled() ) log.info("CommandPlayer.create complete {}", restClient);

        return result;
    };

    /**
     * Sets an existing Player to new values (i.e., an update).
     */
    Command set = (context) -> {
        if ( log.isInfoEnabled() ) log.info("CommandPlayer.set");

        final RestClient restClient = RestClient.create(PORT_NUMBER, "/player");

        CommandResult result = new CommandResult("CommandPlayer.set");
        DefaultContext.fromContext(context)
                .ifPresent(defaultContext -> {
                    result.setSuccess(restClient.post(defaultContext.getPlayer(), 200));
                });

        result.setCode(restClient.getStatusCode());

        if ( log.isInfoEnabled() ) log.info("CommandPlayer.set {}" + restClient);

        return result;
    };

    /**
     * Gets an existing Player.
     */
    Command get = (context) -> {
        if ( log.isInfoEnabled() ) log.info("CommandPlayer.get");

        final RestClient<Player> restClient = RestClient.create(PORT_NUMBER, "/player/{name}");

        CommandResult result = new CommandResult("CommandPlayer.get");
        DefaultContext.fromContext(context)
                .ifPresent(defaultContext -> {
                    restClient.addURIParameters("name", defaultContext.getSourcePlayer().getName());
                    restClient.get(Player.class,200)
                        .ifPresent(player -> {
                            defaultContext.setPlayer(player);
                            result.setSuccess(true);
                        });
                });

        result.setCode(restClient.getStatusCode());

        if ( log.isInfoEnabled() ) log.info("CommandPlayer.get {}", restClient);

        return result;
    };
}
