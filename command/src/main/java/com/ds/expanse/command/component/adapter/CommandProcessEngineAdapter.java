package com.ds.expanse.command.component.adapter;

import com.ds.expanse.command.engine.Cartography;
import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import com.ds.expanse.command.model.spi.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Command process engine adapter to Engine functions.
 */
@Service
public class CommandProcessEngineAdapter {
    @Autowired
    Cartography cartography;

    public boolean canMove(Player player, MapGrid grid) {
        return true;
    }
}
