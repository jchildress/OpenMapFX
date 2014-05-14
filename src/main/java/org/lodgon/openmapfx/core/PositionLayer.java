package org.lodgon.openmapfx.core;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author johan
 */
public class PositionLayer extends Parent implements MapLayer {
    
    private double lat;
    private double lon;
    private final ImageView imageView;
    private double imageWidth, imageHeight;
    private LayeredMap layeredMap;
    
    /**
     * Create a PositionLayer with an image that will be shown on the map at the
     * position of this PositionLayer. 
     * The image is always rendered on the position maintained in this layer.
     * Once created, a PositionLayer should be added to a LayeredMap.
     * The LayeredMap is responsible for doing the required calculations.
     * @param image the image that will be shown at the position of this layer
     */
    public PositionLayer (Image image) {
        imageView = new ImageView(image);
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        imageView.setVisible(false);
        getChildren().add(imageView);
    }
    
    @Override
    public Node getView() {
        return this;
    }
    
        
    public void updatePosition(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        refreshLayer();
    }
    
     protected void refreshLayer() {
        Point2D cartPoint = this.layeredMap.getMapPoint(lat, lon);
        if (cartPoint == null) {
            System.out.println("[JVDBG] Null cardpoint, probably no scene, dont show.");
            return;
        }
        imageView.setVisible(true);
        imageView.setTranslateX(cartPoint.getX() - imageWidth/2);
        imageView.setTranslateY(cartPoint.getY()- imageHeight/2);
    }

    @Override
    public void gotLayeredMap(LayeredMap map) {
        this.layeredMap = map;
        this.layeredMap.zoomProperty().addListener(e -> refreshLayer());
        this.layeredMap.xShiftProperty().addListener(e -> refreshLayer());
        this.layeredMap.yShiftProperty().addListener(e -> refreshLayer());
        refreshLayer();
    }
}
