package com.ds.expanse.player.model;

import lombok.Getter;
import lombok.Setter;

public class PlayerPosition {
    @Getter @Setter int x;
    @Getter @Setter int y;

    public PlayerPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
