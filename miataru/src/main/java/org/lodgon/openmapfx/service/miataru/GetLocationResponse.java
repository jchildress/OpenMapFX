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
 *     * Neither the name of OpenMapFX, any associated website, nor the
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
package org.lodgon.openmapfx.service.miataru;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joeri
 */
public class GetLocationResponse {
    private List<Location> locations = new ArrayList<>();

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void fromJson(String json) {
        int idxMiataruLocation = json.indexOf("MiataruLocation");
        int idxMiataruLocationArray = json.indexOf("[", idxMiataruLocation);
        int idxMiataruLocationObject = json.indexOf("{", idxMiataruLocationArray);
        while (idxMiataruLocationObject != -1) {
            String device = getJsonValue(json, "Device", idxMiataruLocationObject);
            String timestamp = getJsonValue(json, "Timestamp", idxMiataruLocationObject);
            String longitude = getJsonValue(json, "Longitude", idxMiataruLocationObject);
            String latitude = getJsonValue(json, "Latitude", idxMiataruLocationObject);
            String horizontalAccuracy = getJsonValue(json, "HorizontalAccuracy", idxMiataruLocationObject);

            Location location = new Location();
            location.device(device).timestamp(timestamp)
                    .latitude(latitude).longitude(longitude)
                    .horizontalAccuracy(horizontalAccuracy);

            locations.add(location);

            idxMiataruLocationObject = json.indexOf("{", idxMiataruLocationObject + 1);
        }
    }

    private String getJsonValue(String json, String key, int fromIndex) {
        int idxKey = json.indexOf(key, fromIndex);
        int idxKeyColon = json.indexOf(":", idxKey);
        int idxKeyQuote = json.indexOf("\"", idxKeyColon);
        String value = json.substring(idxKeyQuote + 1, json.indexOf("\"", idxKeyQuote + 1));
        return value;
    }
}
