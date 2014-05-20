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

/** Describes a type of tile that can be returned from a {@link TileProvider}, 
 * for example, map, terrain or satellite. The base address is set here to be 
 * able to cope with potential variations to supply methods.
 *
 * @author Geoff Capper
 */
public class TileType implements MapStyleType {
    
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