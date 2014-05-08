package org.lodgon.openmapfx.core;

import javafx.scene.layout.Region;

/**
 *
 * @author johan
 */
public class LayeredMap extends Region {
    
    private final MapArea mapArea;
    private double x0,y0;
    
    public LayeredMap () {
        this.mapArea = new MapArea();
        this.getChildren().add(mapArea);
        setOnMousePressed(t -> {
            x0 = t.getSceneX();
            y0 = t.getSceneY();
        });
        setOnMouseDragged(t -> {
            mapArea.moveX(x0-t.getSceneX());
            mapArea.moveY(y0-t.getSceneY());
            x0 = t.getSceneX();
            y0 = t.getSceneY();
        });
        setOnScroll(t -> mapArea.zoom(t.getDeltaY(), t.getSceneX(), t.getSceneY()) );

    }
    
    /**
     * Explicitly set the zoom level for this map.
     * @param z the zoom level
     */
    public void setZoom (double z) {
        this.mapArea.setZoom(z);
    }
    
    /**
     * Explicitly center the map around this location
     * @param lat latitude
     * @param lon longitude
     */
    public void setCenter (double lat, double lon) {
        this.mapArea.setCenter(lat, lon);
    }
    
    
}
