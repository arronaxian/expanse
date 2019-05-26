package com.ds.expanse.cartograph.repository;

import com.ds.expanse.cartograph.model.MapOverlay;

public interface MapOverlayRepository {
    MapOverlay findMapOverlayBy(String id);
}
