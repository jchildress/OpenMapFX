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

package org.lodgon.openmapfx.android;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import java.util.List;
import javafx.application.Platform;
import javafxports.android.FXActivity;
import org.lodgon.openmapfx.core.Position;
import org.lodgon.openmapfx.core.PositionProvider;
import org.lodgon.openmapfx.core.PositionService;

/**
 *
 * @author johan
 */
public class AndroidPositionService implements PositionProvider, LocationListener {
    private final LocationManager lm;
    private String provider;
    private final double lat;
    private final double lon;
    private boolean debug = true;
    
    public AndroidPositionService () {
          Context ctx = FXActivity.getInstance();
           
            Object systemService = ctx.getSystemService(FXActivity.LOCATION_SERVICE);
            lm = (LocationManager) systemService;
            List<String> providers = lm.getAllProviders();
            if ((providers != null) && (providers.size() > 0)) {
                this.provider = providers.get(providers.size() - 1);
            }
            boolean enabled = lm
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
            }
            Location loc = lm.getLastKnownLocation(provider);
            if (loc != null) {
                lat = loc.getLatitude();
                lon = loc.getLongitude();
            } else {
                lat = 0;
                lon = 0;
            }
            Thread t = new Thread() {
                public void run() {
                    Looper.prepare();
                    lm.requestLocationUpdates(provider, 400, 1, AndroidPositionService.this);
                    Looper.loop();
                }
            };
            System.out.println("[JVDBG] START LOCATIONUPDATER");
            t.start();
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (debug) System.out.println("[JVDBG] GOT NEW LOC: " + loc);
        if (loc != null) {
            final double mylat = loc.getLatitude();
            final double mylon = loc.getLongitude();
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (debug) System.out.println("retrieved new positoin: "+mylat+", "+mylon);
                    Position pos = new Position(mylat, mylon);
                  //  AndroidPositionService.this.positionProperty().set(pos);
            
                }
            });
        }
    }

    @Override
    public void onStatusChanged(String v, int i, Bundle bundle) {
        System.out.println("Status changed for " + v + " to " + i + " by " + bundle);
    }

    @Override
    public void onProviderEnabled(String v) {
        System.out.println("PROVIDER ENABLED: " + v);
    }

    @Override
    public void onProviderDisabled(String v) {
        System.out.println("PROVIDER DISABLED: " + v);
    }

}
