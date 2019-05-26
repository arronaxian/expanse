package com.ds.expanse.command.model.spi.cartograph;

import lombok.Getter;
import lombok.Setter;

public class MapGrid {
    @Getter @Setter private MapCoordinate coordinate;
    @Getter @Setter private char tile;
    @Getter @Setter private String name;

    public MapGrid() {
    }

    public MapGrid(int x, int y, char tile) {
        this.coordinate = new MapCoordinate(x, y);
        this.tile = tile;
        this.name = coordinate.toString();
    }
}
