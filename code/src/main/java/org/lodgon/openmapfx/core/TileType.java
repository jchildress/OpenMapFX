/*
 * Copyright (c) 2014, 2015, OpenMapFX and LodgON
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of LodgON, OpenMapFX, any associated website, nor the
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/** Describes a type of tile that can be returned from a {@link TileProvider}, 
 * for example, map, terrain or satellite. The base address is set here to be 
 * able to cope with potential variations to supply methods.
 *
 * @author Geoff Capper
 */
public class TileType implements MapTileType {
    
    private final String typeName;
    private final String baseURL;
    private final String attributionNotice;
    private String fileStorageBase = null;
    
    public TileType(String typeName, String baseURL) {
        this(typeName, baseURL,"");
    }
    
    public TileType(String typeName, String baseURL, String attributionNotice) {
        this.typeName = typeName;
        this.baseURL = baseURL;
        this.attributionNotice = attributionNotice;
    }
    
    public void setFileStorageBase(String store) {
        this.fileStorageBase = store;
    }
    
    /** The display name for this type of tile, for use in the user interface.
     * 
     * @return the name of the type
     */
    @Override
    public String getTypeName() {
        return typeName;
    }
    
    /** Returns the base URL for obtaining this type of tile from the tile provider.
     * 
     * @return The base URL, ending in a forward slash so that zoom and location 
     * can be appended directly.
     */
	@Override
    public String getBaseURL() {
        return baseURL;
    }
    
    public String getFileCached(int zoom, long i, long j) {
        if (fileStorageBase != null) {
            String enc = File.separator+zoom+File.separator+i+File.separator+j+".png";
            System.out.println("looking for "+enc+" in "+fileStorageBase);
            File candidate = new File(fileStorageBase, enc);
            if (candidate.exists()) {
                return candidate.toURI().toString();
            } 
        }
        return null;
    }
    @Override
    public String getFullURL (int zoom, long i, long j) {
        String cached = getFileCached(zoom, i, j);
        return (cached != null)? cached: getBaseURL() + zoom + "/" + i + "/" + j + ".png";
    }
    
    public InputStream getInputStream(int zoom, long i, long j) {
        try {
            String cached = getFileCached(zoom, i, j);
            if (cached != null) {
                try {
                    return new FileInputStream(new File(new URI(cached)));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TileType.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(TileType.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String urlString = getBaseURL() + zoom + "/" + i + "/" + j + ".png";
            URL url = new URL(urlString);
            InputStream inputStream = url.openConnection().getInputStream();
            if (fileStorageBase != null) {
                String enc = File.separator+zoom+File.separator+i+File.separator+j+".png";
                File candidate = new File(fileStorageBase, enc);
                candidate.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(candidate)) {
                    byte[] buff = new byte[4096];
                    int len = inputStream.read(buff);
                    while (len > 0) {
                        fos.write(buff, 0, len);
                        len = inputStream.read(buff);
                    }
                }
                inputStream.close();
                return new FileInputStream(candidate);
            } else return inputStream;
        } catch (MalformedURLException ex) {
            Logger.getLogger(TileType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TileType.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       
    }
    
    @Override
    public String getAttributionNotice() {
        return attributionNotice;
    }
    
    @Override
    public String toString() {
        return getTypeName();
    }

    
}
