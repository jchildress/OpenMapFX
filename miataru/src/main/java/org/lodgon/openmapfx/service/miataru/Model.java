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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import org.lodgon.openmapfx.core.SettingsService;

/**
 *
 * @author johan
 */
public class Model {

    private static final String SETTING_DEVICE_NAME = "device.name";
    private static final String SETTING_DEVICES = "devices";
    
    private final static Model instance = new Model();
    private final StringProperty deviceNameProperty = new SimpleStringProperty();
    private final ObservableList<Device> trackingDevices = FXCollections.observableArrayList();
    
    private Model() {
        trackingDevices.addListener((Change<? extends Device> change) -> {
            if (change.getList().isEmpty()) {
                SettingsService.getInstance().removeSetting(SETTING_DEVICES);
            } else {
                StringBuilder devicesSetting = new StringBuilder();
                for (int i = 0; i < change.getList().size(); i++) {
                    if (i > 0) {
                        devicesSetting.append("|");
                    }
                    devicesSetting.append(change.getList().get(i).getName());
                }
                SettingsService.getInstance().storeSetting(SETTING_DEVICES, devicesSetting.toString());
            }
        });
    }

    public static Model getInstance() {
        return instance;
    }

    public void loadSettings(SettingsService settingsService) {
        String localDeviceName = settingsService.getSetting(SETTING_DEVICE_NAME);
        if (localDeviceName != null) {
            deviceNameProperty.set(localDeviceName);
        }

        String deviceNamesSetting = settingsService.getSetting(SETTING_DEVICES);
        if (deviceNamesSetting != null) {
            System.out.println("deviceNamesSetting = " + deviceNamesSetting);
            String[] deviceNames = deviceNamesSetting.split("\\|");
            for (String deviceName : deviceNames) {
                Device device = new Device();
                device.setName(deviceName);
                trackingDevices.add(device);
            }
        }
    }

    public ObservableList<Device> trackingDevices() {
        return trackingDevices;
    }
    
    public StringProperty deviceNameProperty() {
        return deviceNameProperty;
    }
}
