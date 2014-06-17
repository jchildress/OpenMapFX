/**
 * Copyright (c) 2014, OpenMapFX All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of LodgON, the website lodgon.com,
 * nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL LODGON BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lodgon.openmapfx.service.miataru;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import org.datafx.provider.ObjectDataProvider;
import org.datafx.provider.ObjectDataProviderBuilder;
import org.datafx.reader.RestSourceBuilder;

/**
 *
 * @author johan
 */
public class Communicator {

//    private static final String SERVER = "http://192.168.1.6:8080/miataru/v1";
    private static final String SERVER = "http://service.miataru.com/v1";
//    private static final String SERVER = "http://localhost:8080/miataru/v1";
//    private static final String SERVER = "http://lodgon.dyndns.org:9999/miataru/v1";

    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final List<LocationListener> locationListeners = new ArrayList<>();

    private final Model model;

    public Communicator(Model model) {
        this.model = model;
    }

    public void addLocationListener(LocationListener locationListener) {
        this.locationListeners.add(locationListener);
    }

    public void retrieveLocation(List<Device> devices) {
        if (!devices.isEmpty()) {
            GetLocation gl = new GetLocation();
            for (Device device : devices) {
                gl.device(device);
            }
            ObjectProperty<String> resultProperty = new SimpleObjectProperty<>();
            RestSourceBuilder rsb = RestSourceBuilder.create();
            rsb.host(SERVER + "/GetLocation")
                    .contentType("application/json")
                    .converter(new PlainTextConverter())
                    .dataString(gl.json());
            ObjectDataProviderBuilder odb = ObjectDataProviderBuilder.create();
            ObjectDataProvider provider = odb.dataReader(rsb.build()).resultProperty(resultProperty).build();

            Platform.runLater(() -> {
                Worker worker = provider.retrieve();
                worker.stateProperty().addListener((ov, oldState, newState) -> {
                    System.out.println("status of updateloc from " + oldState + " to " + newState);
                    System.out.println("[JVDBG] GETLOCATION response = " + resultProperty.get());
                    if (newState.equals(Worker.State.SUCCEEDED)) {
                        GetLocationResponse getLocationResponse = new GetLocationResponse();
                        getLocationResponse.fromJson(resultProperty.get());
                        for (LocationListener locationListener : locationListeners) {
                            for (Location location : getLocationResponse.getLocations()) {
                                locationListener.newLocation(location);
                            }
                        }
                    } else {

                    }
                });
            });
        }
    }

    public void updateLocation(double lat, double lon) {
        String deviceName = model.deviceNameProperty().get();
        if (deviceName == null || deviceName.trim().isEmpty()) {
            deviceName = "Demo Device";
        }
        System.out.println("update loc for " + deviceName + " to " + lon + ", lat = " + lat + ", server = " + SERVER);
        try {
            String enableLocationHistory = model.historyEnabledProperty().get() ? "True" : "False";
            Config c = new Config().enableLocationHistory(enableLocationHistory).locationDataRetentionTime("15");
            Location l = new Location().device(deviceName).timestamp(Long.toString(System.currentTimeMillis() / 1000))
                    .longitude(Double.toString(lon)).latitude(Double.toString(lat)).horizontalAccuracy("40.00");
            Location[] la = new Location[1];
            la[0] = l;
            UpdateLocation ul = new UpdateLocation().config(c).location(la);
            ObjectProperty<String> resultProperty = new SimpleObjectProperty<>();
            RestSourceBuilder rsb = RestSourceBuilder.create();
            rsb.host(SERVER + "/UpdateLocation")
                    .contentType("application/json")
                    .converter(new PlainTextConverter())
                    .dataString(ul.json());
            ObjectDataProviderBuilder odb = ObjectDataProviderBuilder.create();
            ObjectDataProvider provider = odb.dataReader(rsb.build()).resultProperty(resultProperty).build();
            Platform.runLater(() -> {
                Worker worker = provider.retrieve();
                worker.stateProperty().addListener((ov, oldState, newState) -> {
                    System.out.println("status of updateloc from " + oldState + " to " + newState);
                    System.out.println("[JVDBG] UPDATELOCATION response = " + resultProperty.get());
                    if (newState.equals(State.FAILED)) {
                        worker.getException().printStackTrace();
                    }
                });
            });
        } catch (Exception ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, "Exception while updating location", ex);
        }
    }

}
