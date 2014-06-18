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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author johan
 */
public class Model {

    private final static Model instance = new Model();

    private final BooleanProperty trackingProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty historyEnabledProperty = new SimpleBooleanProperty(false);
    private final StringProperty deviceNameProperty = new SimpleStringProperty("Demo Device");
    private final StringProperty dataRetentionTimeProperty = new SimpleStringProperty("12");
//    private final StringProperty serverProperty = new SimpleStringProperty("http://192.168.1.6:8080/miataru/v1");
    private final StringProperty serverLocationProperty = new SimpleStringProperty("service.miataru.com");
//    private final StringProperty serverProperty = new SimpleStringProperty("http://localhost:8080/miataru/v1");
//    private final StringProperty serverProperty = new SimpleStringProperty("http://lodgon.dyndns.org:9999/miataru/v1");
    private final ObservableList<Device> trackingDevices = FXCollections.observableArrayList();

    private Model() {
    }

    public static Model getInstance() {
        return instance;
    }

    public ObservableList<Device> trackingDevices() {
        return trackingDevices;
    }

    public StringProperty deviceNameProperty() {
        return deviceNameProperty;
    }

    public String getDeviceName() {
        return deviceNameProperty.get();
    }

    public StringProperty dataRetentionTimeProperty() {
        return dataRetentionTimeProperty;
    }

    public String getDataRetentionTime() {
        return dataRetentionTimeProperty.get();
    }

    public StringProperty serverLocationProperty() {
        return serverLocationProperty;
    }

    public String getServerLocation() {
        return serverLocationProperty.get();
    }

    public String getServerServiceLocation() {
        String serverServiceLocation = "";

        String serverLocation = getServerLocation();
        if (serverLocation.startsWith("http")) {
            serverServiceLocation = serverLocation;
        } else {
            serverServiceLocation = "http://" + serverLocation;
        }

        if (!serverServiceLocation.endsWith("/")) {
            serverServiceLocation += "/";
        }
        if (!serverServiceLocation.endsWith("v1/")) {
            serverServiceLocation += "v1/";
        }

        return serverServiceLocation;
    }

    public BooleanProperty trackingProperty() {
        return trackingProperty;
    }

    public boolean isTracking() {
        return trackingProperty.get();
    }

    public BooleanProperty historyEnabledProperty() {
        return historyEnabledProperty;
    }

    public boolean isHistoryEnabled() {
        return historyEnabledProperty.get();
    }

}
