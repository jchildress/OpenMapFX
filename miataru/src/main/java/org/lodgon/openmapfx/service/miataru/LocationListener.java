/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.service.miataru;

import java.util.List;

/**
 *
 * @author joeri
 */
public interface LocationListener {

    void newLocation(Location location);

    void newHistory(Device device, List<Location> locations);

}
