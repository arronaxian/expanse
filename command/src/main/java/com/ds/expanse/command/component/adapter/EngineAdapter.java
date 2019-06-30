package com.ds.expanse.command.component.adapter;

import com.ds.expanse.command.engine.GameEngine;
import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import com.ds.expanse.command.model.spi.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Command process engine engineAdapter to Engine functions.
 */
@Component
public class EngineAdapter {
    @Autowired
    GameEngine gameEngine;

    public boolean canMove(Player player, MapGrid grid) {
        return true;
    }

    public void addPlayer(Player player) {
        gameEngine.addPlayer(player);
    }

    public List<Integer> getPlayersNearMe(Player player) {
        return gameEngine.getPlayersNearMe(player);
    }
}
