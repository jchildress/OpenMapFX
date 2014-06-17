package org.lodgon.openmapfx.service.miataru;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author johan
 */
public class DevicesPane extends VBox {

    private final Model model = Model.getInstance();
    private final Communicator communicator;

    @FXML ListView<Device> deviceList;
    @FXML TextField newDeviceId;

    public DevicesPane(Communicator communicator) {
        this.communicator = communicator;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MiataruService.RESOURCES+"/devices.fxml"));
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
        deviceList.setItems(model.trackingDevices());
        deviceList.setCellFactory(lv -> new DeviceListCell());
    }

    @FXML
    public void addNewDevice() {
        String deviceId = newDeviceId.getText();
        if (!deviceId.trim().isEmpty()) {
            Device device = new Device();
            device.setName(newDeviceId.getText());
            model.trackingDevices().add(device);
            newDeviceId.setText("");
        }
    }

}
