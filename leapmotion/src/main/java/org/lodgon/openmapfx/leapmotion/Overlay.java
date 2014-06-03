/**
 * Copyright (c) 2014, OpenMapFX
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
package org.lodgon.openmapfx.leapmotion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author jpereda
 */
public class Overlay extends AnchorPane implements Initializable {
    
    @FXML private Slider vSlider;
    @FXML private ToggleButton handXZTop;
    @FXML private ToggleButton handXZLeft;
    @FXML private ToggleButton handXZRight;
    @FXML private ToggleButton handXZBottom;
    @FXML private ToggleButton handXZ;
    @FXML private ToggleButton handYOut;
    @FXML private ToggleButton handYIn;
    @FXML private ToggleButton handY;
    
    public Overlay(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mymap.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setNavigation(Point3D t1){
        handXZ.setTranslateX(t1.getX()/500*30);
        handXZ.setTranslateY(t1.getZ()/500*30);
        handXZLeft.setSelected(t1.getX()<-10);
        handXZRight.setSelected(t1.getX()>10);
        handXZTop.setSelected(t1.getZ()<-10);
        handXZBottom.setSelected(t1.getZ()>10);
        handXZ.setSelected(handXZLeft.isSelected() || handXZRight.isSelected() ||
                           handXZTop.isSelected() || handXZBottom.isSelected());
        handYIn.setSelected(t1.getY()-200<-40);
        handYOut.setSelected(t1.getY()-200>40);
        handY.setTranslateY((int)Math.min((200-t1.getY())/500*200,60));
        handY.setSelected(handYOut.isSelected() || handYIn.isSelected());
    }
    public void setZoom(int zoom){
        vSlider.setValue(zoom);
    }
}
