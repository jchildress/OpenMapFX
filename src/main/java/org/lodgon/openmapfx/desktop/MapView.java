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
package org.lodgon.openmapfx.desktop;

import java.net.URL;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.lodgon.openmapfx.core.LayeredMap;
import org.lodgon.openmapfx.core.PositionLayer;

public class MapView extends Application {

    LayeredMap map;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        map = new LayeredMap();
        Scene scene = new Scene(map, 800, 600);
        stage.setScene(scene);
        stage.show();
        map.setZoom(4);
        map.setCenter(50.2, 4.2);
        showMyLocation();
    }
    
    
    private void showMyLocation() {
        URL im = this.getClass().getResource("../icons/mylocation.png");
        Image image = new Image(im.toString());
        PositionLayer positionLayer = new PositionLayer(image);
        map.getLayers().add(positionLayer);
        positionLayer.updatePosition(51.2, 4.2);
        map.centerLatitudeProperty().addListener (i -> 
        {
            System.out.println("center of map: lat = "+map.centerLatitudeProperty().get()+", lon = "+map.centerLongitudeProperty().get());
        });
    }

}
