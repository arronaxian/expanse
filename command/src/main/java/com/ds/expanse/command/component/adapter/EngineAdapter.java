package com.ds.expanse.command.component.adapter;

import com.ds.expanse.command.engine.Cartography;
import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import com.ds.expanse.command.model.spi.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Command process engine engineAdapter to Engine functions.
 */
@Component
public class EngineAdapter {
    @Autowired
    Cartography cartography;

    public boolean canMove(Player player, MapGrid grid) {
        return true;
    }
}
