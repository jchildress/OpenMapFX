
package org.lodgon.openmapfx.service.miataru;

/**
 *
 * @author johan
 */
public class Config {
    
    private String enableLocationHistory;
    private String locationDataRetentionTime;
    
    public Config() {
    }
    
    public Config enableLocationHistory (String v) {
        this.enableLocationHistory = v;
        return this;
    }
    
    public Config locationDataRetentionTime (String v) {
        this.locationDataRetentionTime = v;
        return this;
    }
    
    public String json() {
        String answer = "{\n"
                +"\"EnableLocationHistory\":\""+enableLocationHistory+"\",\n"
                +"\"LocationDataRetentionTime\":\""+locationDataRetentionTime+"\"\n"
                +"}";
        return answer;
    }
}
