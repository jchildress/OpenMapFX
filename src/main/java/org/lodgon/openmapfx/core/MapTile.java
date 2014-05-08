package org.lodgon.openmapfx.core;

import static java.lang.Math.floor;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

/**
 *
 * @author johan
 */
public class MapTile extends Region {

    static final String TILESERVER = "http://tile.openstreetmap.org/";
    private final MapArea mapArea;
    private final int myZoom;
    private final long i,j;
    private boolean debug = false;
    
    /**
     * In most cases, a tile will be shown scaled. The 
     * value for the scale factor depends on the active zoom and the 
     * tile-specific myZoom
     */
        final Scale scale = new Scale();
        
    private final InvalidationListener zl;

    /**
     * Create a specific MapTile for a zoomlevel, x-index and y-index
     *
     * @param mapArea the mapArea that will hold this tile. We need a reference
     * to the MapArea as it contains the active zoom property
     * @param zoom the zoom level for this tile
     * @param i the x-index (between 0 and 2^zoom)
     * @param j the y-index (between 0 and 2^zoom)
     */
    public MapTile(final MapArea mapArea, final int zoom, final long i, final long j) {
        this.mapArea = mapArea;
        this.myZoom = zoom;
        this.i = i;
        this.j = j;
        scale.setPivotX(0);
        scale.setPivotY(0);
        getTransforms().add(scale);
        String url = TILESERVER + zoom + "/" + i + "/" + j + ".png";
        System.out.println("Creating maptile "+this);
        Image image = new Image(url, true);
        ImageView iv = new ImageView(image);
        getChildren().addAll(iv);
        zl = recalculate();
        mapArea.zoomProperty().addListener(new WeakInvalidationListener(zl));
        mapArea.translateXProperty().addListener (new WeakInvalidationListener(zl));
        mapArea.translateYProperty().addListener (new WeakInvalidationListener(zl));
        calculatePosition();
    }
    
    private InvalidationListener recalculate() {
        return o -> calculatePosition();
    }
    private void calculatePosition() {
        double currentZoom = mapArea.zoomProperty().get();
        int visibleWindow = (int)floor(currentZoom + MapArea.TIPPING);
        if ((visibleWindow == myZoom) || isCovering() || ((visibleWindow>= MapArea.MAX_ZOOM)&& (myZoom == MapArea.MAX_ZOOM-1) )) {
            this.setVisible(true);
        }
        else {
            this.setVisible(false);
        }
        if (debug) System.out.println("visible tile "+this+"? "+this.isVisible());
        double sf = Math.pow(2, currentZoom - myZoom);
        scale.setX(sf);
        scale.setY(sf);
        setTranslateX(256 * i * sf);
        setTranslateY(256 * j * sf);
    }
    
    /**
     * Check if the current tile is covering more detailed tiles that are currently being loaded.
     * @return 
     */
    private boolean isCovering() {
        return false;
    }
    
    public String toString() {
        return "Tile["+myZoom+"]"+" "+i+", "+j;
    }
}
