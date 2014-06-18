/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.service.miataru;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joeri
 */
public class GetLocationHistoryResponse {
    private List<Location> locations = new ArrayList<>();

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void fromJson(String json) {
        int idxMiataruLocation = json.indexOf("MiataruLocation");
        if (idxMiataruLocation != -1) {
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
    }

    private String getJsonValue(String json, String key, int fromIndex) {
        int idxKey = json.indexOf(key, fromIndex);
        int idxKeyColon = json.indexOf(":", idxKey);
        int idxKeyQuote = json.indexOf("\"", idxKeyColon);
        String value = json.substring(idxKeyQuote + 1, json.indexOf("\"", idxKeyQuote + 1));
        return value;
    }
}
