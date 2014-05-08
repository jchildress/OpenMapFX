/**
 * Copyright (c) 2014, Johan Vos, LodgON
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of LodgON, the website lodgon.com, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL LODGON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lodgon.openmapfx.core;

import static java.lang.Math.floor;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final long i, j;
    private boolean debug = false;
    private final List<MapTile> covering = new LinkedList<>();


    /**
     * In most cases, a tile will be shown scaled. The value for the scale
     * factor depends on the active zoom and the tile-specific myZoom
     */
    final Scale scale = new Scale();

    private final InvalidationListener zl;
    private final BooleanProperty loading = new SimpleBooleanProperty();
    private final MapTile parentTile;
    private final Image image;

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
        if (debug) System.out.println("Creating maptile " + this);
        image = new Image(url, true);
        loading.bind(image.progressProperty().lessThan(1.));
        ImageView iv = new ImageView(image);
        getChildren().addAll(iv);
        
        parentTile = mapArea.findCovering(zoom, i, j);
        if (parentTile != null) {
            parentTile.addCovering(this);
        }
        InvalidationListener ipl = createImageProgressListener();
        image.progressProperty().addListener(new WeakInvalidationListener(ipl));
        
        zl = recalculate();
        
        mapArea.zoomProperty().addListener(new WeakInvalidationListener(zl));
        mapArea.translateXProperty().addListener(new WeakInvalidationListener(zl));
        mapArea.translateYProperty().addListener(new WeakInvalidationListener(zl));
        calculatePosition();
    }

    /**
     * Check if the image in this tile is still loading
     *
     * @return true in case the image is still loading, false in case the image
     * is loaded
     */
    public boolean loading() {
        return loading.get();
    }

    /**
     * Indicate that we are used to cover the loading tile.
     * As soon as we are covering for at least 1 tile, we are visible.
     * @param me a (new) tile which image is still loading
     */
    public void addCovering(MapTile me) {
        covering.add(me);
        setVisible(true);
    }

    /**
     * Remove the supplied tile from the covering list, as its image has been loaded.
     * @param me 
     */
    public void removeCovering(MapTile me) {
        covering.remove(me);
        calculatePosition();
    }
    
    /**
     * Return the tile that will cover us while loading
     * @return the lower-level zoom tile that covers this tile.
     */
    public MapTile getCovering() {
        return parentTile;
    }
    
    @Override
    public String toString() {
        return "Tile[" + myZoom + "]" + " " + i + ", " + j;
    }

    private InvalidationListener recalculate() {
        return o -> calculatePosition();
    }

    private InvalidationListener createImageProgressListener() {
        InvalidationListener answer = o -> {
            if (image.getProgress() >= 1.) {
                if (parentTile != null) {
                    parentTile.removeCovering(MapTile.this);
                }
            }
        };
        return answer;
    }

    private void calculatePosition() {
        double currentZoom = mapArea.zoomProperty().get();
        int visibleWindow = (int) floor(currentZoom + MapArea.TIPPING);
        if ((visibleWindow == myZoom) || isCovering() || ((visibleWindow >= MapArea.MAX_ZOOM) && (myZoom == MapArea.MAX_ZOOM - 1))) {
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
        if (debug) {
            System.out.println("visible tile " + this + "? " + this.isVisible());
        }
        double sf = Math.pow(2, currentZoom - myZoom);
        scale.setX(sf);
        scale.setY(sf);
        setTranslateX(256 * i * sf);
        setTranslateY(256 * j * sf);
    }

    /**
     * Check if the current tile is covering more detailed tiles that are
     * currently being loaded.
     *
     * @return
     */
    private boolean isCovering() {
        return covering.size()>0;
    }

}
