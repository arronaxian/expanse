package com.ds.expanse.command.model.spi.player;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.apache.bcel.util.Play;

public class PlayerPosition {
    @Getter @Setter int x;
    @Getter @Setter int y;

    public PlayerPosition() {
    }

    public PlayerPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
