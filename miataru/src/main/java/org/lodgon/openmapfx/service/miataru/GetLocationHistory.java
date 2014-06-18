package org.lodgon.openmapfx.service.miataru;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author johan
 */
public class GetLocationHistory {

    private Device device;
    private int amount;
    
    public GetLocationHistory() {}

    public GetLocationHistory device(Device device) {
        this.device = device;
        return this;
    }

    public GetLocationHistory amount(int amount) {
        this.amount = amount;
        return this;
    }

    public String json() {
        StringBuilder answer = new StringBuilder();
        answer.append("{\"MiataruGetLocationHistory\":");
        answer.append("{\"Device\":\"").append(device.getName()).append("\",\"Amount\":\"").append(amount).append("\"}");
        answer.append("}");
        System.out.println("[JVDBG] GetLocationHistory, send "+answer);
        return answer.toString();
    }

}
