package com.ds.expanse.cartograph.repository;

import com.ds.expanse.cartograph.model.MapOverlay;
import org.springframework.stereotype.Component;

@Component
public class MapOverlayRepositoryImpl implements MapOverlayRepository {
    private static final MapOverlay overlay = new MapOverlay(50,50);

    public MapOverlayRepositoryImpl() {
    }

    public MapOverlay findMapOverlayBy(String id) {
        return overlay;
    }
}
