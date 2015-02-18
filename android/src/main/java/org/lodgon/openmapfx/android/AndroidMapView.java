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
package org.lodgon.openmapfx.android;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.lodgon.openmapfx.core.DefaultBaseMapProvider;
import org.lodgon.openmapfx.core.LayeredMap;
import org.lodgon.openmapfx.core.Position;
import org.lodgon.openmapfx.core.PositionLayer;
import org.lodgon.openmapfx.core.PositionService;
import org.lodgon.openmapfx.service.MapViewPane;
import org.lodgon.openmapfx.service.miataru.MiataruService;

public class AndroidMapView extends Application {

    private static final Logger LOG = Logger.getLogger(AndroidMapView.class.getName());

    private LayeredMap layeredMap;
    private BorderPane appPane;
    private MapViewPane mapPane;
    private Scene scene;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        MiataruService miataru = new MiataruService();

        DefaultBaseMapProvider mapProvider = new DefaultBaseMapProvider();

        layeredMap = new LayeredMap(mapProvider);
        layeredMap.setZoom(11);
        layeredMap.setCenter(50.8456, 4.7238);

        mapPane = new MapViewPane(layeredMap);

        appPane = new BorderPane();
        appPane.setCenter(mapPane);

        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D visualBounds = primaryScreen.getVisualBounds();
        LOG.log(Level.INFO, "Current screen bounds: " + visualBounds.getWidth() + "x" + visualBounds.getHeight());

        URL myLocationImageUrl = this.getClass().getResource("icons/mylocation.png");
        Image myLocationImage = new Image(myLocationImageUrl.toString());
        PositionLayer positionLayer = new PositionLayer(myLocationImage);
        layeredMap.getLayers().add(positionLayer);

        Label availableServicesLabel = new Label("Available Services");
        availableServicesLabel.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setPrefWidth(400.0);
        CustomMenuItem customMenuItem = new CustomMenuItem(availableServicesLabel);
        CheckMenuItem activateMiataruService = new CheckMenuItem("Miataru");
        activateMiataruService.setSelected(false);
        activateMiataruService.selectedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                layeredMap.getLayers().remove(positionLayer);
                miataru.activate(mapPane);
                appPane.setBottom(miataru.getMenu());
            } else {
                layeredMap.getLayers().add(positionLayer);
                miataru.deactivate();
                appPane.setBottom(null);
            }
        });
        contextMenu.getItems().addAll(customMenuItem, activateMiataruService);
        activateMiataruService.setSelected(true);

        // intercept when the user presses one of the phone buttons
        EventHandler<KeyEvent> keyEventHandler = (e) -> {
            LOG.log(Level.INFO, "A key was pressed: " + e);

            if (e.getCode().equals(KeyCode.ESCAPE)) {
                // the user pressed the back button, so we quit the application
                System.exit(1);
            } else if (e.getCode().equals(KeyCode.CONTEXT_MENU)) {
                // the user pressed the context menu button, so open the menu
//                if (contextMenu.isShowing()) {
//                    contextMenu.hide();
//                } else {
//                    contextMenu.show(primaryStage, visualBounds.getWidth(), visualBounds.getHeight());
//                }
            }
        };

        // update the position only when the miataru service is not active
        PositionService positionService = PositionService.getInstance();
        positionService.positionProperty().addListener(ov -> {
            if (!activateMiataruService.isSelected()) {
                Position currentPosition = positionService.positionProperty().get();
                positionLayer.updatePosition(currentPosition.getLatitude(), currentPosition.getLongitude());
                layeredMap.setCenter(currentPosition.getLatitude(), currentPosition.getLongitude());
            }
        });

        // create the main application scene
        scene = new Scene(appPane, visualBounds.getWidth(), visualBounds.getHeight());

        // show the primary stage
        primaryStage.setTitle("Location Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(keyEventHandler);

        // handle changes in screen rotation etc.
        Screen.getScreens().addListener((ListChangeListener.Change<? extends Screen> change) -> {
            while (change.next()) {
                Screen updatedPrimaryScreen = Screen.getPrimary();
                Rectangle2D updatedVisualBounds = updatedPrimaryScreen.getVisualBounds();

                LOG.log(Level.INFO, "Screen changed, new screen bounds: " + visualBounds.getWidth() + "x" + visualBounds.getHeight());

                Label holdon = new Label("hold on...");
                scene.setRoot(holdon);
                scene = new Scene(appPane, updatedVisualBounds.getWidth(), updatedVisualBounds.getHeight());
                scene.setOnKeyPressed(keyEventHandler);

                stage.setScene(scene);
            }
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
