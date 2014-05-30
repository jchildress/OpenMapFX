/**
 * Copyright (c) 2014, Johan Vos, LodgON All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of LodgON, the website lodgon.com,
 * nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL LODGON BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lodgon.openmapfx.leapmotion;

//import org.lodgon.mapfx.core.FlatMapArea;
import com.leapmotion.leap.Controller;
import javafx.application.Application;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.lodgon.openmapfx.core.BaseMap;
import org.lodgon.openmapfx.core.MapArea;
import org.lodgon.openmapfx.core.LayeredMap;
import org.lodgon.openmapfx.core.DefaultBaseMapProvider;

public class LeapMotionMapView extends Application {

    private SimpleLeapListener listener;
    private Controller leapController;
    private Scene scene;
    private long last = 0;

    @Override
    public void start(Stage primaryStage) {
        String lp = System.getProperty("java.library.path");
        System.out.println("lp = "+lp);
        DefaultBaseMapProvider provider = new DefaultBaseMapProvider();

        LayeredMap map = new LayeredMap(provider);
       BaseMap mapArea = map.getMapArea();

        Label copyright = new Label("(c) OpenStreetMap Contributors");
        Overlay overlay = new Overlay();
        Pane parent = new Pane();
        parent.getChildren().addAll(map, overlay, copyright);

        scene = new Scene(parent, 800, 600);
        primaryStage.setTitle("JavaFX Map application");
        primaryStage.setScene(scene);
        primaryStage.show();
        copyright.setManaged(false);
        copyright.layoutYProperty().bind(scene.heightProperty().add(-30));
        copyright.setLayoutX(10);
        overlay.layoutXProperty().bind(scene.widthProperty().add(-overlay.getWidth()));
        overlay.layoutYProperty().bind(scene.heightProperty().add(-overlay.getHeight()));
//        map.loadTiles();
        map.setZoom(5);
        map.setCenter(50.8,4.3);
        try {
            listener = new SimpleLeapListener();
            leapController = new Controller();
        } catch (UnsatisfiedLinkError e) {
            // seems we can't catch it, we're probably too late
            System.out.println("EXCEPTION: " + e);
        }
        leapController.addListener(listener);

        listener.palmProperty().addListener((ov, t, t1)
                -> Platform.runLater(() -> {
                    // mapArea.move3D(t1);
                    if (Math.abs(t1.getX()) > 10) {
                        mapArea.moveX((t1.getX() / 35d));
                    }
                    if (Math.abs(t1.getZ()) > 10) {
                        mapArea.moveY((t1.getZ() / 35d));
                    }
                    if ((t1.getY() < 160) && (t1.getY() > 30)) {
                        if (System.currentTimeMillis() - last > 20) {
                            mapArea.zoom(.1, scene.getWidth()/2, scene.getHeight()/2);
                            last = System.currentTimeMillis();
                        }
                    }
                    if (t1.getY() > 240) {
                        if (System.currentTimeMillis() - last > 20) {
                            mapArea.zoom(-.1, scene.getWidth()/2, scene.getHeight()/2);
                            last = System.currentTimeMillis();
                        }
                    }
                    overlay.setNavigation(t1);
                }));
        map.zoomProperty().addListener((ov, t, t1) -> overlay.setZoom(t1.intValue()));
    }

    @Override
    public void stop() {
        leapController.removeListener(listener);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
