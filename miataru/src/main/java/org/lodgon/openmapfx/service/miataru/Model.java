/*
 * Copyright (c) 2014, 2015, OpenMapFX and LodgON
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of LodgON, OpenMapFX, any associated website, nor the
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.util.converter.BooleanStringConverter;
import org.lodgon.openmapfx.core.SettingsService;

/**
 *
 * @author johan
 */
public class Model {

    private static final String SETTING_DATA_RETENTION_TIME = "dataRetentionTime";
    private static final String SETTING_DEVICE_NAME = "deviceName";
    private static final String SETTING_DEVICES = "devices";
    private static final String SETTING_HISTORY_ENABLED = "historyEnabled";
    private static final String SETTING_SERVER_LOCATION = "serverLocation";
    private static final String SETTING_TRACKING_ENABLED = "trackingEnabled";
    private static final String SETTING_UPDATE_INTERVAL = "updateInterval";

    private final StringProperty dataRetentionTimeProperty = new SimpleStringProperty("30");
    private final StringProperty deviceNameProperty = new SimpleStringProperty("Demo Device");
    private final BooleanProperty historyEnabledProperty = new SimpleBooleanProperty(false);
//    private final StringProperty serverProperty = new SimpleStringProperty("http://192.168.1.6:8080/miataru/v1");
    private final StringProperty serverLocationProperty = new SimpleStringProperty("service.miataru.com");
//    private final StringProperty serverProperty = new SimpleStringProperty("http://localhost:8080/miataru/v1");
//    private final StringProperty serverProperty = new SimpleStringProperty("http://lodgon.dyndns.org:9999/miataru/v1");
    private final ObservableList<Device> trackingDevices = FXCollections.observableArrayList();
    private final StringProperty trackingDevicesSettingProperty = new SimpleStringProperty("");
    private final BooleanProperty trackingEnabledProperty = new SimpleBooleanProperty(true);
    private final StringProperty updateIntervalProperty = new SimpleStringProperty("30s");
    private final ObjectProperty<Device> showingHistoryForDeviceProperty = new SimpleObjectProperty<>();

    public Model() {
        SettingsService settingsService = SettingsService.getInstance();

        String dataRetentionTimeSetting = settingsService.getSetting(SETTING_DATA_RETENTION_TIME);
        if (dataRetentionTimeSetting != null) {
            dataRetentionTimeProperty.set(dataRetentionTimeSetting);
        }

        String deviceNameSetting = settingsService.getSetting(SETTING_DEVICE_NAME);
        if (deviceNameSetting != null) {
            deviceNameProperty.set(deviceNameSetting);
        }

        String historyEnabledSetting = settingsService.getSetting(SETTING_HISTORY_ENABLED);
        if (historyEnabledSetting != null) {
            historyEnabledProperty.set(Boolean.parseBoolean(historyEnabledSetting));
        }

        String serverLocationSetting = settingsService.getSetting(SETTING_SERVER_LOCATION);
        if (serverLocationSetting != null) {
            serverLocationProperty.set(serverLocationSetting);
        }

        String trackingEnabledSetting = settingsService.getSetting(SETTING_TRACKING_ENABLED);
        if (trackingEnabledSetting != null) {
            trackingEnabledProperty.set(Boolean.parseBoolean(trackingEnabledSetting));
        }

        String updateIntervalSetting = settingsService.getSetting(SETTING_UPDATE_INTERVAL);
        if (updateIntervalSetting != null) {
            updateIntervalProperty.set(updateIntervalSetting);
        }

        String trackingDevicesSetting = settingsService.getSetting(SETTING_DEVICES);
        if (trackingDevicesSetting != null) {
            trackingDevicesSettingProperty.set(trackingDevicesSetting);
            String[] trackingDeviceNames = trackingDevicesSetting.split("\\|");
            for (String trackingDeviceName : trackingDeviceNames) {
                Device trackingDevice = new Device();
                trackingDevice.setName(trackingDeviceName);
                trackingDevices.add(trackingDevice);
            }
        }

        dataRetentionTimeProperty.addListener(new SettingChangeListener<>(SETTING_DATA_RETENTION_TIME));
        deviceNameProperty.addListener(new SettingChangeListener<>(SETTING_DEVICE_NAME));
        historyEnabledProperty.addListener(new SettingChangeListener<>(SETTING_HISTORY_ENABLED, new BooleanStringConverter()));
        serverLocationProperty.addListener(new SettingChangeListener<>(SETTING_SERVER_LOCATION));
        trackingDevicesSettingProperty.addListener(new SettingChangeListener<>(SETTING_DEVICES));
        trackingEnabledProperty.addListener(new SettingChangeListener<>(SETTING_TRACKING_ENABLED, new BooleanStringConverter()));
        updateIntervalProperty.addListener(new SettingChangeListener<>(SETTING_UPDATE_INTERVAL));
        trackingDevices.addListener((Change<? extends Device> change) -> {
            StringBuilder sbTrackingDevicesSetting = new StringBuilder();
            for (int i = 0; i < change.getList().size(); i++) {
                if (i > 0) {
                    sbTrackingDevicesSetting.append("|");
                }
                sbTrackingDevicesSetting.append(change.getList().get(i).getName());
            }
            trackingDevicesSettingProperty.set(sbTrackingDevicesSetting.toString());
        });
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
        String serverServiceLocation;

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

    public BooleanProperty trackingEnabledProperty() {
        return trackingEnabledProperty;
    }

    public boolean isTrackingEnabled() {
        return trackingEnabledProperty.get();
    }

    public BooleanProperty historyEnabledProperty() {
        return historyEnabledProperty;
    }

    public boolean isHistoryEnabled() {
        return historyEnabledProperty.get();
    }

    public ObjectProperty<Device> showingHistoryForDeviceProperty() {
        return showingHistoryForDeviceProperty;
    }

    public Device getShowingHistoryForDevice() {
        return showingHistoryForDeviceProperty.get();
    }

    public StringProperty updateIntervalProperty() {
        return updateIntervalProperty;
    }

    public String getUpdateInterval() {
        return updateIntervalProperty.get();
    }
}
