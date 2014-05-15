/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.core;

/** Interface used to describe a source of tiles, to allow for switching 
 * between both providers and the types of tiles that are displayed.
 *
 * @author Geoff Capper
 */
public interface TileProvider {
    
    /** Get the display name of the tile provider, for use in the UI.
     * 
     * @return The display name of the tile provider, e.g. "Map Quest"
     */
    public String getProviderName();
    
    /** Get an array of {@link TileType}s that this provider can supply.
     * 
     * @return 
     */
    public TileType[] getTileTypes();
    
    /** Gets the default tile type for this provider, typically the map tile.
     * 
     * @return 
     */
    public TileType getDefaultType();
    
//    /** Get the base address for any tile requests. This must end with a 
//     * forward slash, so that the dynamic parameters can be appended directly. 
//     * e.g. http://otile1.mqcdn.com/tiles/1.0.0/sat/ for the satellite tile 
//     * types.
//     * 
//     * @param tileType One of the selection of TileTypes returned from getTileTypes().
//     * @return The base URL for use in obtaining tiles.
//     */
//    public String getBaseURL(TileType tileType);
    
    /** The attribution notice that is required by the tile provider to be 
     * displayed.
     * 
     * @return Any legally required attribution notice that must be displayed.
     */
    public String getAttributionNotice();
    
}
