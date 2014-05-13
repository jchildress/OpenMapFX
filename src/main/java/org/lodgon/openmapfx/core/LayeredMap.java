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
    
    /**
     * Return the MapArea that is backing this map 
     * @return the MapArea used as the geomap for this layeredmap
     */
    public MapArea getMapArea () {
        return this.mapArea;
    }
    
    
}
