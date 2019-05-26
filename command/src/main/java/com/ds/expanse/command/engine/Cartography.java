package com.ds.expanse.command.engine;

import com.ds.expanse.command.model.spi.cartograph.MapCoordinate;
import org.springframework.stereotype.Service;

@Service
public class Cartography {
    public boolean canMove(MapCoordinate newPosition, char tile) {
        return true;
    }
}
