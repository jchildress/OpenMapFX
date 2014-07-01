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
