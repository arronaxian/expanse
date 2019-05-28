package com.ds.expanse.cartograph.controller;

import com.ds.expanse.cartograph.component.Cartograph;
import com.ds.expanse.cartograph.model.MapGrid;
import com.ds.expanse.cartograph.model.MapOverlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/cartograph")
public class CartographController {
    @Autowired
    Cartograph cartograph;

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

//        // row[x],column[y]
//        for ( int x = upperLeftX; x < lowerRightX; x ++ ) {
//            for ( int y = upperLeftY; y < lowerRightY; y++ ) {
//                range.add(cartograph.getPerlinMapGrid(x,y));
//            }
//        }

        return ResponseEntity.ok(range);
    }
}
