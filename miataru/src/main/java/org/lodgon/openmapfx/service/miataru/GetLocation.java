package org.lodgon.openmapfx.service.miataru;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author johan
 */
public class GetLocation {

    private final List<Device> devices = new ArrayList<>();
    
    public GetLocation() {}

    public GetLocation device(Device device) {
        this.devices.add(device);
        return this;
    }

    public String json() {
        StringBuilder answer = new StringBuilder();
        answer.append("{\"MiataruGetLocation\":[");
        for (int i = 0; i < devices.size(); i++) {
            if (i > 0) answer.append(",");
            answer.append("{\"Device\":\"").append(devices.get(i).getName()).append("\"}");
        }
        answer.append("]}");
        System.out.println("[JVDBG] GetLocation, send "+answer);
        return answer.toString();
    }

}
