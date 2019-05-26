package com.ds.expanse.command.component.command;

import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import com.ds.expanse.command.model.spi.player.Player;
import com.ds.expanse.command.model.spi.player.PlayerPosition;

/**
 * Command effects produces mutation on the target object.
 *
 */
public interface CommandEffects {
    /**
     * Prepares a new Player.
     */
    Command preparePlayer = (context) -> {
        DefaultContext.fromContext(context)
                .ifPresent(defaultContext -> {
                    Player player = new Player();

                    player.setName(defaultContext.getSourcePlayer().getName());

                    PlayerPosition position = new PlayerPosition(50,50);
                    player.setPosition(position);

                    defaultContext.setPlayer(player);
                });

        return new CommandResult("CommandEffects.preparePlayer", CommandResult.Status.ok, true);
    };

    /**
     * Moves a Player.
     */
    Command movePlayer = (context) -> {
        final DefaultContext defaultContext = DefaultContext.fromContext(context).get();
        final MapGrid grid = defaultContext.getMapGrid();

        final CommandResult result = new CommandResult("CommandEffects.movePlayer");
        if ( defaultContext.getAdapter().canMove(defaultContext.getPlayer(), grid) ) {
            PlayerPosition position = defaultContext.getPlayer().getPosition();
            position.setX(grid.getCoordinate().getX());
            position.setY(grid.getCoordinate().getY());

            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;
    };
}
