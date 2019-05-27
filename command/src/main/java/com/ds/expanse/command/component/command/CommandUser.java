package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.utility.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CommandUser {
    Logger log = LogManager.getLogger(CommandUser.class);
    int PORT_NUMBER = 8080;

    Command addPlayer = (context) -> {
        log.info("CommandUser.addPlayer");

        final RestClient restClient = RestClient.create(PORT_NUMBER, "user/{username}/player/{id}");

        CommandResult result = new CommandResult("CommandUser.addPlayer");
        DefaultContext.fromContext(context)
                .ifPresent(defaultContext -> {
                    restClient.addURIParameters("username", defaultContext.getUserName());
                    restClient.addURIParameters("id", defaultContext.getPlayer().getId());
                    result.setSuccess(restClient.put( 200));
                    result.setCode(restClient.getStatusCode());
                });

        if (log.isInfoEnabled() ) log.info("CommandUser.addPlayer {}", restClient.isValidStatus());

        return result;
    };
}
