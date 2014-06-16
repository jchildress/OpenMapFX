package org.lodgon.openmapfx.service.miataru;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author johan
 */
public class SettingsPane extends AnchorPane {

    private final Model model = Model.getInstance();

    @FXML CheckBox track;
    @FXML TextField deviceName;

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
        track.setSelected(model.trackProperty().get());
        deviceName.setText(model.deviceNameProperty().get());
    }

    @FXML
    public void applyChanges() {
        model.trackProperty().set(track.isSelected());
        model.deviceNameProperty().set(deviceName.getText());
    }

}
