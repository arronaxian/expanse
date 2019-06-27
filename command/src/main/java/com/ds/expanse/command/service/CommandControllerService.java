package com.ds.expanse.command.service;

import com.ds.expanse.command.component.CommandProcess;
import com.ds.expanse.command.component.command.DefaultContext;
import com.ds.expanse.command.component.command.Result;
import com.ds.expanse.command.model.spi.player.Player;
import com.ds.expanse.command.model.spi.player.PlayerPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Command controller service.
 */
@Service
public class CommandControllerService {
    @Autowired
    CommandProcess processor;

    /**
     * Change the player_find position relative by units.
     * @param playerName The name of the player_find to change.
     * @param heading The heading.
     * @param units The units to player_move.
     * @return
     */
    public Optional<PlayerPosition> changePlayerPosition(String user, String playerName, String heading, int units) {
        final DefaultContext context = new DefaultContext(heading);
        context.setUserProvidedPlayer(new Player(playerName));

        Result<Boolean> result = processor.process(CommandProcess.Sequence.player_move, context);

        return result.outcome() ?
                Optional.ofNullable(context.getPlayer().getPosition()) :
                Optional.empty();
    }

    /**
     * Register a Player to the User.
     * @param userName The User association.
     * @param player The player_find to be registered.
     * @return The player_find entity.
     */
    public Optional<Player> registerPlayer(String userName, Player player) {
        final DefaultContext context = new DefaultContext(userName, "create");
        context.setUserProvidedPlayer(player);

        final Result<Boolean> result = processor.process(CommandProcess.Sequence.player_register, context);

        return result.outcome() ?
                Optional.ofNullable(context.getPlayer()) :
                Optional.empty();
    }

    /**
     * Get Player for User
     */
    public Optional<Player> getPlayer(String userName, String ... playerNames) {
        final DefaultContext context = new DefaultContext(userName, "player_find");
        final Result<Boolean> result = processor.process(CommandProcess.Sequence.player_find, context);

        return result.outcome() ?
                Optional.ofNullable(context.getPlayer()) :
                Optional.empty();
    }

}
