package com.ds.expanse.cartograph.model;

import lombok.Getter;
import lombok.Setter;

public class MapCoordinate {
    @Getter @Setter private int x;
    @Getter @Setter private int y;

    public MapCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("coordinate [%s,%s]",x,y);
    }
}
