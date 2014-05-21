/**
 * Copyright (c) 2014, OpenMapFX
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

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 *
 * @author johan
 */
public class LayeredMap extends Region {
    
    private final BaseMap mapArea;
    private double x0,y0;
    private final ObservableList<MapLayer> layers = FXCollections.observableArrayList();
    
    
    public LayeredMap (BaseMapProvider provider) {

        this.mapArea = provider.getBaseMap();
        
        this.getChildren().add(mapArea.getView());
        this.layers.addListener(new ListChangeListener<MapLayer>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends MapLayer> c) {
               while (c.next()) {
                   for (MapLayer candidate : c.getAddedSubList()) {
                       Node view = candidate.getView();
                       getChildren().add(view);
                       candidate.gotLayeredMap(LayeredMap.this);
                   }
                   for (MapLayer target : c.getRemoved()){
                       getChildren().remove(target.getView());
                   }
               }
            }
        });
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
        setOnZoom(t ->  {
            System.out.println("zoomfactor = "+t.getZoomFactor()+" tot = "+t.getTotalZoomFactor());
            mapArea.zoom(t.getZoomFactor()> 1? .1: -.1, t.getSceneX(),t.getSceneY());
        } );
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
    public BaseMap getMapArea () {
        return this.mapArea;
    }
    
    /**
     * Return a mutable list of all layers that are handled by this LayeredMap
     * The MapArea backing the map is not part of this list
     * @return the list containing all layers
     */
    public ObservableList<MapLayer> getLayers() {
        return layers;
    }
    
    /** 
     * Return the (x,y) coordinates for the provides (lat, lon) point as it
     * would appear on the current map, talking into account the zoom and
     * translate properties
     * @param lat
     * @param lon
     * @return 
     */
    public Point2D getMapPoint (double lat, double lon) {
        return this.mapArea.getMapPoint(lat, lon);
    }
    
    /**
     * Return the zoom property for the backing map
     * @return the zoom property for the backing map
     */
    public DoubleProperty zoomProperty() {
        return this.mapArea.zoomProperty();
    }
    
    /**
     * Return the horizontal translation of the backing map
     * @return the horizontal translation of the backing map
     */
    public DoubleProperty xShiftProperty () {
        return this.mapArea.getView().translateXProperty();
    }
    
    /**
     * Return the vertical translation of the backing map
     * @return the vertical translation of the backing map
     */
    public DoubleProperty yShiftProperty () {
        return this.mapArea.getView().translateYProperty();
    }
    
    public DoubleProperty centerLongitudeProperty() {
        return this.mapArea.centerLon();
    }
    
    public DoubleProperty centerLatitudeProperty() {
        return this.mapArea.centerLat();
    }
}
