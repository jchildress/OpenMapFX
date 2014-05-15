/**
 * Copyright (c) 2014, Johan Vos, LodgON
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
package org.lodgon.openmapfx.android;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.animation.Animation;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxports.android.FXActivity;
import org.lodgon.openmapfx.core.LayeredMap;
import org.lodgon.openmapfx.core.PositionLayer;
import org.lodgon.openmapfx.core.TileProvider;
import org.lodgon.openmapfx.core.TileType;
import org.lodgon.openmapfx.providers.OSMTileProvider;

// import org.scenicview.ScenicView;

public class AndroidMapView extends Application implements LocationListener{
    
    private LocationManager lm;
    private String provider;
    private LayeredMap layeredMap;
    boolean real = true;
    BorderPane appPane;
    Pane mapPane;
    private Scene scene;
    private Stage stage;
    private final int STARTZOOM = 14;
    private boolean debug = true;
    
    @Override
    public void start(Stage primaryStage) {
        EventHandler backEvent = createBackEvent();
        System.out.println("[JVDBG] START APPLICATION");
        this.stage = primaryStage;
        double lat = 50.8456;
        double lon = 4.7238;
        TileProvider osm = new OSMTileProvider();
        ObjectProperty<TileType> typeProperty = new SimpleObjectProperty<>(osm.getDefaultType());
        layeredMap = new LayeredMap(typeProperty);
        layeredMap.setZoom(14);
        if (real) {
            Context ctx = FXActivity.getInstance();
           
            Object systemService = ctx.getSystemService(FXActivity.LOCATION_SERVICE);
            lm = (LocationManager) systemService;
            List<String> providers = lm.getAllProviders();
            if ((providers != null) && (providers.size() > 0)) {
                this.provider = providers.get(providers.size() - 1);
            }
            boolean enabled = lm
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
            }
            Location loc = lm.getLastKnownLocation(provider);
            if (loc != null) {
                lat = loc.getLatitude();
                lon = loc.getLongitude();
            } else {
                lat = 0;
                lon = 0;
            }
            Thread t = new Thread() {
                public void run() {
                    Looper.prepare();
                    lm.requestLocationUpdates(provider, 400, 1, AndroidMapView.this);
                    Looper.loop();
                }
            };
            System.out.println("[JVDBG] START LOCATIONUPDATER");
            t.start();

        } else {
            fakeUpdates();
        }
  
        mapPane = new Pane();
        mapPane.getChildren().addAll(layeredMap);//, c);
        
        appPane = new BorderPane();
        appPane.setCenter(mapPane);
        Screen screen = Screen.getPrimary();
        Rectangle2D b = screen.getVisualBounds();
        scene = new Scene(appPane, b.getWidth(), b.getHeight());
      //  ScenicView.show(scene);
        primaryStage.setTitle("Location Tracker");
        
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(backEvent);
        scene.getStylesheets().add("style.css");
        Screen.getScreens().addListener(new ListChangeListener<Screen>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends Screen> change) {
                while (change.next()) {
                    System.out.println("[JVDBG] Screenchange ");
                  
                    Screen screen = Screen.getPrimary();
                    Rectangle2D b = screen.getVisualBounds();
                    Label holdon = new Label ("hold on...");
                    scene.setRoot(holdon);
                    scene = new Scene(appPane, b.getWidth(), b.getHeight());
                    scene.setOnKeyPressed(backEvent);

                    stage.setScene(scene);
                }
            }
        });
      layeredMap.setCenter(lat, lon);
   //   URL im = this.getClass().getResource("icons/mylocation.png");
    //  Image image = new Image(im.toString());
    }

    private EventHandler<KeyEvent> createBackEvent() {
        EventHandler<KeyEvent> answer = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                 System.out.println("key pressed: "+t);
                if (t.getCode().equals(KeyCode.ESCAPE)) {
                    System.exit(1);
                }
            }

        };
        return answer;
    }
    
    private void fakeUpdates() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    double lat0 = 50.8456;
                    double lon0 = 4.7238;
                    while (true) {
                        Thread.sleep(10000);
                        lat0 = lat0 + Math.random() * .00001;
                        lon0 = lon0 + Math.random() * .00001;
                        final double lat = lat0;
                        final double lon = lon0;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                layeredMap.setCenter(lat, lon);

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
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

    long last = System.currentTimeMillis();
    private void showMyLocation () {
     URL im = this.getClass().getResource("../icons/mylocation.png");
        Image image = new Image(im.toString());
        PositionLayer positionLayer = new PositionLayer(image);
        layeredMap.getLayers().add(positionLayer);
    }
    @Override
    public void onLocationChanged(Location loc) {
        if (debug) System.out.println("[JVDBG] GOT NEW LOC: " + loc);
        if (loc != null) {
            final double lat = loc.getLatitude();
            final double lon = loc.getLongitude();
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (debug) System.out.println("center map to "+lat+", "+lon);
                    layeredMap.setCenter(lat, lon);
                }
            });
        }
    }

    @Override
    public void onStatusChanged(String v, int i, Bundle bundle) {
        System.out.println("Status changed for " + v + " to " + i + " by " + bundle);
    }

    @Override
    public void onProviderEnabled(String v) {
        System.out.println("PROVIDER ENABLED: " + v);
    }

    @Override
    public void onProviderDisabled(String v) {
        System.out.println("PROVIDER DISABLED: " + v);
    }

    private String getDeviceName(Context ctx) {
        try {
            FileInputStream openFileInput = ctx.openFileInput("mydevice");
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput));
            String entry = br.readLine();
            return entry;
        } catch (FileNotFoundException ex) {
            System.out.println("[JVDBG] MyDevice not found");
            Logger.getLogger(AndroidMapView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("[JVDBG] MyDevice not readable");
            Logger.getLogger(AndroidMapView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
