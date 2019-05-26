package com.ds.expanse.command.model.spi.cartograph;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class MapCoordinate {
    @Getter @Setter private int x;
    @Getter @Setter private int y;

    public MapCoordinate() {
    }

    public MapCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("position [%s,%s]",x,y);
    }
}
