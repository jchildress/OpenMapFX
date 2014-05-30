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

import java.util.LinkedList;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.lodgon.openmapfx.core.DefaultBaseMapProvider;
import org.lodgon.openmapfx.core.LayeredMap;
import org.lodgon.openmapfx.core.LicenceLayer;
import org.lodgon.openmapfx.core.TileProvider;
import org.lodgon.openmapfx.service.OpenMapFXService;
import org.lodgon.openmapfx.service.miataru.MiataruService;

public class MapView extends Application {

    LayeredMap map;

    TileProvider[] tileProviders;
    SimpleProviderPicker spp;
    HBox topMenu;
    private final BorderPane bp = new BorderPane();
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
        topMenu = new HBox();
        Region white = new Region();
        HBox.setHgrow(white, Priority.ALWAYS);
        topMenu.getChildren().addAll(spp, white, createServiceMenu());
        map = new LayeredMap(provider);

        BorderPane cbp = new BorderPane();
        cbp.setCenter(map);

        Rectangle clip = new Rectangle(700, 600);
        cbp.setClip(clip);
        clip.heightProperty().bind(cbp.heightProperty());
        clip.widthProperty().bind(cbp.widthProperty());

        bp.setTop(topMenu);
        bp.setCenter(cbp);

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
                public void activate() {
                }

            };
            MiataruService miataru = new MiataruService("desktopdemo");
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
        servicesMenu.valueProperty().addListener((ObservableValue<? extends OpenMapFXService> observable, OpenMapFXService oldValue, OpenMapFXService newService) -> {
            if (newService != null) {
                Node menu = newService.getMenu();
                newService.activate();
                System.out.println("New service detected, menu = " + menu);
                bp.setBottom(menu);
                
            }
        });
        // servicesMenu.getSelectionModel().select(getServices().get(0));
        return servicesMenu;
    }

}
