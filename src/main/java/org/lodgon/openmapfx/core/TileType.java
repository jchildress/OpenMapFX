/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.core;

/** Describes a type of tile that can be returned from a {@link TileProvider}, 
 * for example, map, terrain or satellite. The base address is set here to be 
 * able to cope with potential variations to supply methods.
 *
 * @author Geoff Capper
 */
public class TileType {
    
    private final String typeName;
    private final String baseURL;
    private final String attributionNotice;
    
    public TileType(String typeName, String baseURL) {
        this.typeName = typeName;
        this.baseURL = baseURL;
        this.attributionNotice = "";
    }
    
    public TileType(String typeName, String baseURL, String attributionNotice) {
        this.typeName = typeName;
        this.baseURL = baseURL;
        this.attributionNotice = attributionNotice;
    }
    
    /** The display name for this type of tile, for use in the user interface.
     * 
     * @return 
     */
    public String getTypeName() {
        return typeName;
    }
    
    /** Returns the base URL for obtaining this type of tile from the tile provider.
     * 
     * @return The base URL, ending in a forward slash so that zoom and location 
     * can be appended directly.
     */
    public String getBaseURL() {
        return baseURL;
    }
    
    public String getAttributionNotice() {
        return attributionNotice;
    }
    
}
