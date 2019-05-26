package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.utility.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CommandUser {
    int PORT_NUMBER = 8080;

    static Logger log = LogManager.getLogger(CommandUser.class);

    Command addPlayer = (context) -> {
        log.info("CommandUser.addPlayer");

        final RestClient restClient = RestClient.create(PORT_NUMBER);

        CommandResult result = new CommandResult("CommandUser.addPlayer");
        DefaultContext.fromContext(context)
                .ifPresent(defaultContext -> {
                    restClient.addURIParameters("username", defaultContext.getUserName());
                    restClient.addURIParameters("id", defaultContext.getPlayer().getId());
                    result.setSuccess(restClient.put("user/{username}/player/{id}", 200));
                    result.setCode(restClient.getStatusCode());
                });

        if (log.isInfoEnabled() ) log.info("CommandUser.addPlayer {}", restClient.isOk());

        return result;
    };
}
