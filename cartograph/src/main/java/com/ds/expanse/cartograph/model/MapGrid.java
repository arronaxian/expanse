package com.ds.expanse.cartograph.model;

import lombok.Getter;
import lombok.Setter;

public class MapGrid {
    @Getter @Setter MapCoordinate coordinate;
    @Getter @Setter char tile;
    @Getter @Setter String name;

    public MapGrid(int x, int y, char tile) {
        this.coordinate = new MapCoordinate(x, y);
        this.tile = tile;
        this.name = coordinate.toString();
    }
}
