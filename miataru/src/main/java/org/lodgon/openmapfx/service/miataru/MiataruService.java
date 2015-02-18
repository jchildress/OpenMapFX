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
 *     * Neither the name of OpenMapFX, any associated website, nor the
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

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.lodgon.openmapfx.core.MultiPositionLayer;
import org.lodgon.openmapfx.core.Position;
import org.lodgon.openmapfx.core.PositionLayer;
import org.lodgon.openmapfx.core.PositionService;
import org.lodgon.openmapfx.service.MapViewPane;
import org.lodgon.openmapfx.service.OpenMapFXService;

/**
 *
 * @author johan
 */
public class MiataruService implements OpenMapFXService, LocationListener  {

    private static final Logger LOG = Logger.getLogger(MiataruService.class.getName());

    private PositionService positionService;
    private ObjectProperty<Position> positionProperty;

    private final Map<String, Node> deviceNodes = new HashMap<>();
    private final MultiPositionLayer historyPositionLayer = new MultiPositionLayer();
    private final MultiPositionLayer knownDevicesPositionLayer = new MultiPositionLayer();
    private final PositionLayer personalPositionLayer;
    private Timeline getLocationsTimeline;

    final static String RESOURCES = "/org/lodgon/openmapfx/services/miataru";

    private final Device device;

    private final Model model = new Model();
    private final Communicator communicator = new Communicator(model);

    private final DevicesPane devicesPane;
    private final SettingsPane settingsPane;

    private MapViewPane pane;
    
    public MiataruService() {
        this.device = new Device();
        this.device.nameProperty().bind(model.deviceNameProperty());

        Marker personalPositionMarker = new Marker();
        personalPositionLayer = new PositionLayer(personalPositionMarker,
                personalPositionMarker.getCenterX(), personalPositionMarker.getCenterY());

        this.devicesPane = new DevicesPane(communicator, model);
        this.settingsPane = new SettingsPane(model);

        communicator.addLocationListener(this);

        getLocationsTimeline = new Timeline(new KeyFrame(Duration.ZERO, e -> communicator.retrieveLocation(model.trackingDevices())),
                new KeyFrame(Duration.valueOf(model.getUpdateInterval())));
        getLocationsTimeline.setCycleCount(Timeline.INDEFINITE);
        model.updateIntervalProperty().addListener((ov, oldValue, newValue) -> {
            try {
                Duration duration = Duration.valueOf(newValue);
                Timeline.Status initialTimelineStatus = getLocationsTimeline.getStatus();
                if (initialTimelineStatus.equals(Timeline.Status.RUNNING)) {
                    getLocationsTimeline.stop();
                }
                getLocationsTimeline.getKeyFrames().set(1, new KeyFrame(duration));
                if (initialTimelineStatus.equals(Timeline.Status.RUNNING)) {
                    getLocationsTimeline.play();
                }
            } catch (IllegalArgumentException ex) {
                // ignore
            }
        });
    }

    @Override
    public String getName() {
        return "Miataru";
    }

    @Override
    public Node getMenu() {
        URL devicesUrl = this.getClass().getResource(RESOURCES + "/icons/devices.png");
        ImageView devicesView = new ImageView(devicesUrl.toString());
        Label devicesLabel = new Label ("devices");
        VBox devicesBox = new VBox(devicesView, devicesLabel);
        devicesBox.setAlignment(Pos.TOP_CENTER);
        devicesBox.setOnMouseClicked(e -> pane.setActiveNode(devicesPane));

        URL mapUrl = this.getClass().getResource(RESOURCES + "/icons/map.png");
        ImageView mapView= new ImageView(mapUrl.toString());
        Label mapLabel = new Label ("map");
        VBox mapBox = new VBox(mapView, mapLabel);
        mapBox.setAlignment(Pos.TOP_CENTER);
        mapBox.setOnMouseClicked(e -> {
            if (!pane.getMap().getLayers().contains(personalPositionLayer)) {
                pane.getMap().getLayers().remove(historyPositionLayer);
                pane.getMap().getLayers().addAll(personalPositionLayer, knownDevicesPositionLayer);
            }
            pane.showMap();
        });

        URL historyUrl = this.getClass().getResource(RESOURCES + "/icons/history.png");
        ImageView historyView= new ImageView(historyUrl.toString());
        Label historyLabel = new Label ("history");
        VBox historyBox = new VBox(historyView, historyLabel);
        historyBox.setAlignment(Pos.TOP_CENTER);
        historyBox.setOnMouseClicked(e -> {
            communicator.retrieveHistory(device);
        });

        URL settingsUrl = this.getClass().getResource(RESOURCES + "/icons/settings.png");
        ImageView settingsView= new ImageView(settingsUrl.toString());
        Label settingsLabel = new Label("settings");
        VBox settingsBox = new VBox (settingsView, settingsLabel);
        settingsBox.setAlignment(Pos.TOP_CENTER);
        settingsBox.setOnMouseClicked(e -> pane.setActiveNode(settingsPane));

        GridPane menu = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25.0);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(25.0);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(25.0);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPercentWidth(25.0);
        menu.getColumnConstraints().addAll(column1, column2, column3, column4);

        menu.addRow(0, devicesBox, mapBox, historyBox, settingsBox);

        return menu;
    }

    @Override
    public void activate(MapViewPane pane) {
        LOG.log(Level.INFO, "Activating Miataru Service");

        this.pane = pane;
        pane.getMap().getLayers().addAll(personalPositionLayer, knownDevicesPositionLayer);

        if (positionService == null) {
            positionService = PositionService.getInstance();
            positionProperty = positionService.positionProperty();
            positionProperty.addListener(observable -> {
                Position position = positionProperty.get();
                double lat = position.getLatitude();
                double lon = position.getLongitude();
                personalPositionLayer.updatePosition(lat, lon);
                if (model.trackingEnabledProperty().get()) {
                    communicator.updateLocation(lat, lon);
                    System.out.println("new position: "+positionProperty.get());
                }
            });
        }

        if (positionProperty.isNotNull().get()) {
            personalPositionLayer.updatePosition(positionProperty.get().getLatitude(),
                    positionProperty.get().getLongitude());
            if (model.trackingEnabledProperty().get()) {
                communicator.updateLocation(positionProperty.get().getLatitude(),
                        positionProperty.get().getLongitude());
            }
        }

        getLocationsTimeline.play();
    }

    @Override
    public void deactivate() {
        LOG.log(Level.INFO, "Deactivating Miataru Service");

        this.pane.getMap().getLayers().removeAll(personalPositionLayer,
                knownDevicesPositionLayer, historyPositionLayer);
        this.pane.showMap();
        this.getLocationsTimeline.stop();
    }

    @Override
    public void newLocation(Location location) {
        Node deviceNode = deviceNodes.get(location.getDevice());
        if (deviceNode == null) {
            deviceNode = new Circle(5, Color.RED);
            deviceNodes.put(location.getDevice(), deviceNode);
            knownDevicesPositionLayer.addNode(deviceNode, location.getLatitude(), location.getLongitude());
        } else {
            knownDevicesPositionLayer.updatePosition(deviceNode, location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void newHistory(Device device, List<Location> locations) {
        if (device.equals(model.showingHistoryForDeviceProperty().get())) {
            historyPositionLayer.removeAllNodes();
            for (Location location : locations) {
                Circle circle = new Circle(5, Color.BLUE);
                historyPositionLayer.addNode(circle, location.getLatitude(), location.getLongitude());
            }

            if (!pane.getMap().getLayers().contains(historyPositionLayer)) {
                pane.getMap().getLayers().removeAll(personalPositionLayer, knownDevicesPositionLayer);
                pane.getMap().getLayers().addAll(historyPositionLayer);
            }
            pane.showMap();
        }
    }

}
