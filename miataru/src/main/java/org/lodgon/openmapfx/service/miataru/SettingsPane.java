package org.lodgon.openmapfx.service.miataru;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
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
    @FXML TextField serverLocation;

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
        track.selectedProperty().bindBidirectional(model.trackingProperty());
        history.selectedProperty().bindBidirectional(model.historyEnabledProperty());
        deviceName.textProperty().bindBidirectional(model.deviceNameProperty());
        serverLocation.textProperty().bindBidirectional(model.serverLocationProperty());
    }

}
