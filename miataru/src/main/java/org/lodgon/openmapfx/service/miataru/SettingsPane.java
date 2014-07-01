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

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author johan
 */
public class SettingsPane extends GridPane {

    private final Model model;

    @FXML CheckBox track;
    @FXML CheckBox history;
    @FXML TextField deviceName;
    @FXML ComboBox<String> dataRetentionTime;
    @FXML TextField serverLocation;
    @FXML ComboBox<String> updateInterval;

    public SettingsPane(Model model) {
        this.model = model;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MiataruService.RESOURCES+"/settings.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            Object load = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize() {
        dataRetentionTime.getItems().addAll("15", "30", "45", "60", "75", "90");
        updateInterval.getItems().addAll("10s", "30s", "1m", "2m");

        track.selectedProperty().bindBidirectional(model.trackingEnabledProperty());
        history.selectedProperty().bindBidirectional(model.historyEnabledProperty());
        deviceName.textProperty().bindBidirectional(model.deviceNameProperty());
        dataRetentionTime.valueProperty().bindBidirectional(model.dataRetentionTimeProperty());
        serverLocation.textProperty().bindBidirectional(model.serverLocationProperty());
        updateInterval.valueProperty().bindBidirectional(model.updateIntervalProperty());
    }

}
