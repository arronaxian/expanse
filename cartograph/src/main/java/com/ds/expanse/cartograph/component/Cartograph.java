package com.ds.expanse.cartograph.component;

import com.ds.expanse.cartograph.model.MapGrid;
import com.ds.expanse.cartograph.model.MapOverlay;
import com.ds.expanse.cartograph.repository.MapOverlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class Cartograph {
    @Autowired
    MapOverlayRepository mapOverlayRepository;

    @Autowired
    PerlinNoise perlinNoise;

    Map<Integer, String> mapping = new HashMap<>();

    private final static Function<Float, Integer> perlinConversion = (Float perlinValue) -> {
        return Math.round( ( perlinValue + 1 ) * 5 );
    };

    public MapGrid getPerlinMapGrid(int latitude, int longitude) {
        int convertedPerlinValue = perlinNoise.perlin(latitude, longitude, perlinConversion);

        MapGrid grid = new MapGrid(latitude, longitude, Character.forDigit(convertedPerlinValue,10));

        return grid;
    }

    public MapGrid getPerlimMapGrid(int x, int y, MapOverlay.Heading heading) {
        int newX = x + heading.getMoveX();
        int newY = y + heading.getMoveY();

        int convertedPerlinValue = perlinNoise.perlin(newX, newY, perlinConversion);

        MapGrid grid = new MapGrid(newX, newY, Character.forDigit(convertedPerlinValue,10));

        return grid;
    }

    public MapGrid getMapGrid(int latitude, int longitude) {
        return mapOverlayRepository.findMapOverlayBy("1").getMapGrid(latitude, longitude);
    }

    public MapGrid getMapGrid(int x, int y, MapOverlay.Heading compass) {
        MapOverlay overlay = mapOverlayRepository.findMapOverlayBy("1");
        MapGrid grid = overlay.getMapGrid(x,y);

        return mapOverlayRepository.findMapOverlayBy("1").move(grid, compass);
    }
}
