package org.lodgon.openmapfx.service.miataru;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import org.lodgon.openmapfx.service.miataru.Device;

/**
 *
 * @author johan
 */
public class DevicesPane extends AnchorPane implements Initializable {

    private final Model model = Model.getInstance();
    @FXML ListView<Device> deviceList;
    @FXML TextField newDeviceId;
    
    public DevicesPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MiataruService.RESOURCES+"/devices.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            Object load = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize!!");
        deviceList.setItems(model.trackingDevices());
        deviceList.setCellFactory(new Callback<ListView<Device>, ListCell<Device>>(){

            @Override
            public ListCell<Device> call(ListView<Device> param) {
            return new ListCell<Device>() {
                @Override
                protected void updateItem(Device item,  boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText("EMPTY");
                        setGraphic(null);
                    }
                    else {
                        if (item != null) {
                            Region r = new Region();
                            r.setPrefWidth(18);
                            Label l = new Label (item.getName());
                            r.getStyleClass().add("delete");
                            r.setOnMouseClicked(new EventHandler<MouseEvent>() {

                                @Override
                                public void handle(MouseEvent event) {
                                    System.out.println("Delete asked for "+item);
                                    model.trackingDevices().remove(item);
                                }
                            });
                            setGraphic(new FlowPane(r,l));
                        } else {
                            setText("NULL");
                        setGraphic(null);
                        }
                    }
                }
            }; 
            }
        });
    }
    
    public void addNewDevice () {
        Device device = new Device();
        device.setName(newDeviceId.getText());
        model.trackingDevices().add(device);
        newDeviceId.setText("");
       
    }

}
