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
package org.lodgon.openmapfx.desktop;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.lodgon.openmapfx.core.DefaultBaseMapProvider;
import org.lodgon.openmapfx.core.LayeredMap;
import org.lodgon.openmapfx.core.LicenceLayer;
import org.lodgon.openmapfx.core.TileProvider;
import org.lodgon.openmapfx.service.MapViewPane;
import org.lodgon.openmapfx.service.OpenMapFXService;
import org.lodgon.openmapfx.service.miataru.MiataruService;

public class MapView extends Application {

    LayeredMap map;

    TileProvider[] tileProviders;
    SimpleProviderPicker spp;
    GridPane topMenu;
    private final BorderPane bp = new BorderPane();
    private MapViewPane centerPane;
    private ObservableList<OpenMapFXService> services;
    LicenceLayer licenceLayer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DefaultBaseMapProvider provider = new DefaultBaseMapProvider();

        spp = new SimpleProviderPicker(provider);

        HBox servicesMenu = new HBox(4.0, new Label("Services: "), createServiceMenu());
        servicesMenu.setStyle("-fx-padding:4px");
        servicesMenu.setAlignment(Pos.CENTER_LEFT);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints();

        topMenu = new GridPane();
        topMenu.getColumnConstraints().addAll(column1, column2);
        topMenu.add(spp, 0, 0);
        topMenu.add(servicesMenu, 1, 0);

        map = new LayeredMap(provider);
        centerPane = new MapViewPane(map);

        Rectangle clip = new Rectangle(700, 600);
        centerPane.setClip(clip);
        clip.heightProperty().bind(centerPane.heightProperty());
        clip.widthProperty().bind(centerPane.widthProperty());

        bp.setTop(topMenu);
        bp.setCenter(centerPane);

        Scene scene = new Scene(bp, 800, 650);
        stage.setScene(scene);
        stage.show();
        map.setZoom(4);
        map.setCenter(50.2, 4.2);
        //    showMyLocation();

        licenceLayer = new LicenceLayer(provider);
        map.getLayers().add(licenceLayer);
    }
//
//    private void showMyLocation() {
//        URL im = this.getClass().getResource("../icons/mylocation.png");
//        Image image = new Image(im.toString());
//        PositionLayer positionLayer = new PositionLayer(image);
//        map.getLayers().add(positionLayer);
//        positionLayer.updatePosition(51.2, 4.2);
//    }

    private ObservableList<OpenMapFXService> getServices() {
        if (services == null) {
            OpenMapFXService nothing = new OpenMapFXService() {

                @Override
                public String getName() {
                    return "no service";
                }

                @Override
                public Node getMenu() {
                    return null;
                }

                @Override
                public void activate(MapViewPane pane) {
                }

                @Override
                public void deactivate() {
                }

            };
            MiataruService miataru = new MiataruService();
            services = FXCollections.observableArrayList();
            services.addAll(nothing, miataru);
        }
        return services;
    }

    private Node createServiceMenu() {
        ComboBox<OpenMapFXService> servicesMenu = new ComboBox<>(FXCollections.observableArrayList(getServices()));

        servicesMenu.setConverter(new StringConverter<OpenMapFXService>() {

            @Override
            public String toString(OpenMapFXService service) {
                return service.getName();
            }

            @Override
            public OpenMapFXService fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        servicesMenu.valueProperty().addListener((observable, oldService, newService) -> {
            if (oldService != null) {
                oldService.deactivate();
            }

            if (newService != null) {
                newService.activate(centerPane);
                bp.setBottom(newService.getMenu());
            }
        });
        return servicesMenu;
    }

}
