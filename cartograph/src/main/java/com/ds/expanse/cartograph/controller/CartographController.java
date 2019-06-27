package com.ds.expanse.cartograph.controller;

import com.ds.expanse.cartograph.component.Cartograph;
import com.ds.expanse.cartograph.model.MapGrid;
import com.ds.expanse.cartograph.model.MapOverlay;
import com.ds.expanse.cartograph.model.PerlinMapGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path="/cartograph")
public class CartographController {
    @Autowired
    Cartograph cartograph;

    private final static List<Integer> EDGE_OF_WORLD = new ArrayList<>();
    static {
        for ( int index = 0; index < 16*16; index ++ ) {
            EDGE_OF_WORLD.add(-1);
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/grid")
    public ResponseEntity<MapGrid> getMapGrid(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(cartograph.getPerlinMapGrid(x, y));
        } catch ( ArrayIndexOutOfBoundsException e ) {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/heading")
    public ResponseEntity<MapGrid> getMapGridCompass(@RequestParam(name = "x") int positionX,
                                                     @RequestParam(name = "y") int positionY,
                                                     @RequestParam(name = "h") MapOverlay.Heading heading) {
        try {
            return ResponseEntity.ok(cartograph.getPerlimMapGrid(positionX, positionY, heading));
        } catch (ArrayIndexOutOfBoundsException e ) {
            // Indicates there is no content at that latitude and longitude.
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Gets the Perlin noise rect area (x1,y1) to (x2,y2).
     * @param x1 Upper left corner (x)
     * @param y1 Upper left corner (y)
     * @param x2 Lower right corner (x)
     * @param y2 Lower right corner (y)
     * @return A range over x to x + areaX and y to y + areaY.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/perlin/rect")
    public ResponseEntity<List<Integer>> getMapGridRectArea(@RequestParam(name = "x1") int x1,
                                                            @RequestParam(name = "y1") int y1,
                                                            @RequestParam(name = "x2") int x2,
                                                            @RequestParam(name = "y2") int y2) {

        try {
            final List<Integer> range = new ArrayList<>();
            // row[x],column[y]
            for (int yy = y1; yy < y2; yy++) {
                for (int xx = x1; xx < x2; xx++) {
                    range.add(cartograph.getPerlin(xx, yy));
                }
            }
            return ResponseEntity.ok(range);
        } catch ( ArrayIndexOutOfBoundsException e ) {
            return ResponseEntity.ok(EDGE_OF_WORLD);
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/area")
    public ResponseEntity<List<MapGrid>> getMapGridArea(@RequestParam(name = "cx") int centerX,
                                                        @RequestParam(name = "cy") int centerY,
                                                        @RequestParam(name = "ax") int areaX,
                                                        @RequestParam(name = "ay") int areaY) {
        List<MapGrid> range = new ArrayList<>();

        int upperLeftX = centerX - areaX;
        int upperLeftY = centerY - areaY;

        int lowerRightX = centerX + areaX;
        int lowerRightY = centerY + areaY;

        // row[x],column[y]
        for ( int y = upperLeftY; y <= lowerRightY; y++ ) {
            for ( int x = upperLeftX; x <= lowerRightX; x ++ ) {
                range.add(cartograph.getPerlinMapGrid(x,y));
            }
        }

        return ResponseEntity.ok(range);
    }
}
