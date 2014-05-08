package org.lodgon.openmapfx.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lodgon.openmapfx.core.LayeredMap;

public class MapView extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LayeredMap map = new LayeredMap();
        Scene scene = new Scene(map, 800, 600);
        stage.setScene(scene);
        stage.show();
        map.setZoom(4);
        map.setCenter(4.2, 50.2);
    }
}
