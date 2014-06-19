package org.lodgon.openmapfx.service.miataru;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author johan
 */
public class Location {
    
    @JsonProperty("Device")
    private String device;
    @JsonProperty("Timestamp")
    private String timestamp;
    @JsonProperty("Longitude")
    private String longitude;
    @JsonProperty("Latitude")
    private String latitude;
    @JsonProperty("HorizontalAccuracy")
    private String horizontalAccuracy;
    
    public Location () {
        
    }
    
    public String getDevice() {
        return this.device;
    }
    
    public Location device (String v) {
        this.device = v;
        return this;
    }
    
    public Location timestamp (String v) {
        this.timestamp = v;
        return this;
    }
    
    public Location longitude (String v) {
        this.longitude = v;
        return this;
    }
    
    public double getLongitude() {
        return Double.valueOf(longitude);
    }
    
    public Location latitude (String v) {
        this.latitude = v;
        return this;
    }
    
    public double getLatitude () {
        return Double.valueOf(latitude);
    }
    public Location horizontalAccuracy (String v) {
        this.horizontalAccuracy = v;
        return this;
    }
    
    public String json() {
        String answer = "{\n"
                + "\"Device\": \""+device+"\",\n"
                + "\"Timestamp\": \""+timestamp+"\",\n"
                + "\"Longitude\": \""+longitude+"\",\n"
                + "\"Latitude\": \""+latitude+"\",\n"
                + "\"HorizontalAccuracy\": \""+horizontalAccuracy+"\"\n"
                + "}";
        return answer;
    }
    
}
