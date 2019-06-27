package com.ds.expanse.cartograph.model;

import lombok.Getter;
import lombok.Setter;

public class PerlinMapGrid {
    @Getter @Setter int x;
    @Getter @Setter int y;
    @Getter @Setter int t;

    public PerlinMapGrid(int x, int y, int tile) {
        this.x = x;
        this.y = y;
        this.t = tile;
    }
}
