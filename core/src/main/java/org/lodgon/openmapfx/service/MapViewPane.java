/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.service;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.lodgon.openmapfx.core.LayeredMap;

/**
 *
 * @author joeri
 */
public class MapViewPane extends BorderPane {
    private LayeredMap map;

    public MapViewPane() {
    }

    public MapViewPane(LayeredMap map) {
        super(map);

        this.map = map;
    }

    public LayeredMap getMap() {
        return map;
    }

    public void setActiveNode(Node node) {
        setCenter(node);
    }

    public void showMap() {
        setCenter(this.map);
    }

}
