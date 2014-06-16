package org.lodgon.openmapfx.service.miataru;

/**
 *
 * @author johan
 */
public class UpdateLocation {

    private Config config;
    private Location[] location;
    
    public UpdateLocation() {}
    
    public UpdateLocation config (Config v) {
        this.config = v;
        return this;
    }
    
    public UpdateLocation location(Location[] v) {
        this.location = v;
        return this;
    }
    
    public String json() {
        String answer = "{\n"
                + "\"MiataruConfig\":" + config.json()
                +",\n"
                + "\"MiataruLocation\": [";
        boolean notFirst = false;
        for (Location l : location) {
            if (notFirst) {
                 answer = answer +",";
            }
            notFirst = true;
            answer = answer + l.json();
        }
        answer = answer + "]\n}";
        System.out.println("[JVDBG] UpdateLocation, send "+answer);
        return answer;
    }
}
