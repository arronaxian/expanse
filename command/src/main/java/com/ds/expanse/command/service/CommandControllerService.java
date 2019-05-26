package com.ds.expanse.command.service;

import com.ds.expanse.command.component.CommandProcess;
import com.ds.expanse.command.component.command.CommandResult;
import com.ds.expanse.command.component.command.DefaultContext;
import com.ds.expanse.command.model.spi.player.Player;
import com.ds.expanse.command.model.spi.player.PlayerPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommandControllerService {
    @Autowired
    CommandProcess processor;


    /**
     * Change the player position relative by units.
     * @param playerName The name of the player to change.
     * @param heading The heading.
     * @param units The units to move.
     * @return
     */
    public Optional<PlayerPosition> changePlayerPosition(String user, String playerName, String heading, int units) {
        final DefaultContext context = new DefaultContext(heading);
        context.setSourcePlayer(new Player(playerName));

        CommandResult result = processor.process(CommandProcess.Sequence.move, context);

        return result.isSuccess() ?
                Optional.ofNullable(context.getPlayer().getPosition()) :
                Optional.empty();
    }

    /**
     * Register a Player to the User.
     * @param userName The User association.
     * @param player The player to be registered.
     * @return The player entity.
     */
    public Optional<Player> registerPlayer(String userName, Player player) {
        final DefaultContext context = new DefaultContext(userName, "create");
        context.setSourcePlayer(player);

        final CommandResult result = processor.process(CommandProcess.Sequence.register, context);

        return result.isSuccess() ?
                Optional.ofNullable(context.getPlayer()) :
                Optional.empty();
    }
}
