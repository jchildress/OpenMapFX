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
 *     * Neither the name of LodgON, OpenMapFX, any associated website, nor the
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

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.lodgon.openmapfx.core.Position;
import org.lodgon.openmapfx.core.PositionProvider;

/**
 *
 * @author johan
 */
public class FakePositionProvider implements PositionProvider {

    boolean activated = false;
    ObjectProperty<Position> positionProperty = new SimpleObjectProperty<>();

    public FakePositionProvider() {
    }
    
    private void activate() {
        activated = true;
        System.out.println("starting fake positionprovider");
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    double lat0 = 50.8456;
                    double lon0 = 4.7238;
                    while (true) {
                        final double lat = lat0;
                        final double lon = lon0;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Position p = new Position(lat, lon);
                                positionProperty.set(p);
                            }
                        });
                        Thread.sleep(10000);
                        lat0 = lat0 + Math.random() * .00001;
                        lon0 = lon0 + Math.random() * .00001;
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

    @Override
    public ObjectProperty<Position> getPositionProperty() {
        if (!activated) activate();
        return positionProperty;
    }

}
