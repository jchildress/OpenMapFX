/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.lodgon.openmapfx.core.TileProvider;
import org.lodgon.openmapfx.core.TileType;

/**
 *
 * @author Geoff Capper
 */
public class SimpleProviderPicker extends HBox {
    
    private final ComboBox<TileProvider> cmbProviders;
    private final HBox buttonBox;
    
    private final ObjectProperty<TileType> selectedTileType = new SimpleObjectProperty<>();
    
    public SimpleProviderPicker(TileProvider[] providers) {
        super(4);
        this.setStyle("-fx-padding:4px");
        
        if (providers == null || providers.length == 0) {
            throw new IllegalArgumentException("Providers array passed to SimpleProviderPicker cannot be null or empty.");
        }
        
        cmbProviders = new ComboBox<>(FXCollections.observableArrayList(providers));
        cmbProviders.valueProperty().addListener((ObservableValue<? extends TileProvider> obs, TileProvider o, TileProvider n) -> {
            setCurrentTileProvider(n);
        });
        
        buttonBox = new HBox(4);
        
        getChildren().addAll(cmbProviders, buttonBox);
        
        cmbProviders.getSelectionModel().select(providers[0]);
        
    }
    
    private void setCurrentTileProvider(TileProvider tp) {
        buttonBox.getChildren().clear();
        
        final ToggleGroup group = new ToggleGroup();
        
        for (TileType tt : tp.getTileTypes()) {
            ToggleButton tb = new ToggleButton(tt.getTypeName());
            tb.setUserData(tt);
            tb.setToggleGroup(group);
            if (tt.equals(tp.getDefaultType())) {
                tb.setSelected(true);
                selectedTileType.set(tt);
            }
            buttonBox.getChildren().add(tb);
        }
        
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle o, Toggle n) -> {
            if (n == null) {
                // ignore - but we should reset the button.
            } else {
                selectedTileType.set((TileType) n.getUserData());
            }
        });
        
    }
    
    public ObjectProperty<TileType> selectedTileTypeProperty() {
        return selectedTileType;
    }
    
}
