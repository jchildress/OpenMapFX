/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lodgon.openmapfx.core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


/**
 *
 * @author Geoff Capper
 */
public class LicenceLayer extends AnchorPane implements MapLayer {
    
    private Label lblLicence;
	
    private BaseMapProvider provider;
    
    private final ChangeListener<TileProvider> tileProviderListener = (ObservableValue<? extends TileProvider> obs, TileProvider o, TileProvider n) -> {
		updateLicence(n);
	};
    
	public LicenceLayer(BaseMapProvider provider) {
		this.provider = provider;
        this.provider.tileProviderProperty().addListener(tileProviderListener);
        
		lblLicence = new Label();
		lblLicence.setText("");
        lblLicence.setStyle("-fx-background-color:rgba(66%,66%,66%,0.5)");
        
		AnchorPane.setLeftAnchor(lblLicence,0.0);
        AnchorPane.setBottomAnchor(lblLicence, 0.0);
        //setRightAnchor(lblLicence, 0.0);
		
		getChildren().add(lblLicence);
        
        updateLicence(provider.tileProviderProperty().get());
	}
	
    public void setBaseMapProvider(BaseMapProvider provider) {
        this.provider.tileProviderProperty().removeListener(tileProviderListener);
        this.provider = provider;
        this.provider.tileProviderProperty().addListener(tileProviderListener);
        updateLicence(provider.tileProviderProperty().get());
    }
    
    private void updateLicence(TileProvider tileProvider) {
        if (tileProvider != null) {
            lblLicence.setText(tileProvider.getAttributionNotice());
        } else {
            lblLicence.setText("");
        }
    }
    
	@Override
	public Node getView() {
		return this;
	}

	@Override
	public void gotLayeredMap(LayeredMap map) {
		this.minWidthProperty().bind(map.widthProperty());
		this.minHeightProperty().bind(map.heightProperty());
	}
    
}
