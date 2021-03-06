package com.ds.expanse.command.engine;

import com.ds.expanse.command.model.spi.player.PlayerPosition;

public class MathUtil {
    public static final boolean inRange(PlayerPosition a, PlayerPosition b, float range) {
        if ( range == -1 ) {
            return true;
        }

        final int xx = ( a.getX() - b.getX() );
        final int yy = ( a.getY() - b.getY() );

        return range <= Math.sqrt(xx*xx + yy*yy);
    }

    public static final int random(int range) {
        return (int)Math.round(Math.random()*range);
    }
}
