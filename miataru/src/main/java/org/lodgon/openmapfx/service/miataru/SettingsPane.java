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

    private final Model model = Model.getInstance();

    @FXML CheckBox track;
    @FXML CheckBox history;
    @FXML TextField deviceName;
    @FXML ComboBox<String> dataRetentionTime;
    @FXML TextField serverLocation;
    @FXML ComboBox<String> updateInterval;

    public SettingsPane() {
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

        track.selectedProperty().bindBidirectional(model.trackingProperty());
        history.selectedProperty().bindBidirectional(model.historyEnabledProperty());
        deviceName.textProperty().bindBidirectional(model.deviceNameProperty());
        dataRetentionTime.valueProperty().bindBidirectional(model.dataRetentionTimeProperty());
        serverLocation.textProperty().bindBidirectional(model.serverLocationProperty());
        updateInterval.valueProperty().bindBidirectional(model.updateIntervalProperty());
    }

}
