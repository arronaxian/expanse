package com.ds.expanse.cartograph.model;

import lombok.Getter;
import lombok.Setter;

public class MapOverlay {
    final private MapGrid[][] map;

    public enum Heading {
        north   (0,-1),
        south   (0,1),
        west    (-1,0),
        east    (1,0);

        @Getter @Setter private int moveX;
        @Getter @Setter private int moveY;

        Heading(int moveX, int moveY) {
            this.moveX = moveX;
            this.moveY = moveY;
        }
    }

    public MapOverlay(int x, int y) {
        map = new MapGrid[x][y];

        for ( int index = 0; index < x; index ++ ) {
            for ( int jndex = 0; jndex < y; jndex ++ ) {
                MapGrid mapElement = new MapGrid(index, jndex, '.');
                map[index][jndex] = mapElement;
            }
        }
    }

    public MapGrid getMapGrid(int x, int y) {
        return map[x][y];
    }

    public MapGrid move(MapGrid from, Heading direction) {
        return move(from, direction,1);
    }

    public MapGrid move(MapGrid from, Heading direction, int distance) {
        int newX = from.getCoordinate().getX() + direction.getMoveX();
        int newY = from.getCoordinate().getY() + direction.getMoveY();

        final MapGrid mapElement = map[newX][newY];

        return mapElement;
    }
}
