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
public class OSMTileProvider implements TileProvider {
    
    private static final String providerName = "OpenStreetMap";
    
    private static final TileType[] tileTypes = new TileType[1];
    static {
        tileTypes[0] = new TileType("Map", "http://tile.openstreetmap.org/", "© OpenStreetMap contributors");
    }
    
    public OSMTileProvider() {
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
        return tileTypes[0];
    }

    @Override
    public String getAttributionNotice() {
        return "© OpenStreetMap contributors";
    }
    
    @Override
    public String toString() {
        return getProviderName();
    }
    
}
