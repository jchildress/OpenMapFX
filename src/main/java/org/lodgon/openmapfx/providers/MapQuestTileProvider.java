/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.providers;

import org.lodgon.openmapfx.core.TileProvider;
import org.lodgon.openmapfx.core.TileType;

/**
 *
 * @author Geoff Capper
 */
public class MapQuestTileProvider implements TileProvider {
    
    private static final String providerName = "MapQuest";
    
    private static final TileType[] tileTypes = new TileType[2];
    static {
        tileTypes[0] = new TileType("Map", "http://otile1.mqcdn.com/tiles/1.0.0/map/", "© OpenStreetMap contributors");
        tileTypes[1] = new TileType("Satellite", "http://otile1.mqcdn.com/tiles/1.0.0/sat/", "Portions Courtesy NASA/JPL-Caltech and U.S. Depart. of Agriculture, Farm Service Agency");
    }
    
    public MapQuestTileProvider() {
    }
    
    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public TileType[] getTileTypes() {
        return tileTypes;
    }
    
    @Override
    public TileType getDefaultType() {
        return tileTypes[1];
    }
    
    @Override
    public String getAttributionNotice() {
        return "<p>Tiles Courtesy of <a href=\"http://www.mapquest.com/\" target=\"_blank\">MapQuest</a> <img src=\"http://developer.mapquest.com/content/osm/mq_logo.png\"></p>";
    }
    
    @Override
    public String toString() {
        return getProviderName();
    }
    
}
