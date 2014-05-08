package org.lodgon.openmapfx.core;

import static java.lang.Math.floor;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;

/**
 *
 * @author johan
 */
public class MapArea extends Group {

    /**
     * When the zoom-factor is less than TIPPING below an integer, we will use
     * the higher-level zoom and scale down.
     */
    public static final double TIPPING = 0.2;

    /**
     * The maximum zoom level this map supports.
     */
    public static final int MAX_ZOOM = 20;
    private final Map<Long, SoftReference<MapTile>>[] tiles = new HashMap[MAX_ZOOM];

    private int nearestZoom;

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty();

    private boolean debug = true;
    
    public MapArea() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new HashMap<>();
        }
    }

    public void setCenter (double lon, double lat) {
        if (getScene() == null) {
            if (debug) System.out.println("Ignore setting center since scene is null.");
            return;
        }
        double activeZoom = zoomProperty.get();
        double n = Math.pow(2, activeZoom);
        double lat_rad = Math.PI * lat / 180;
        double id = n / 360. * (180 + lon);
        double jd = n * (1 - (Math.log(Math.tan(lat_rad) + 1 / Math.cos(lat_rad)) / Math.PI)) / 2;
        double mex = (double) id * 256;
        double mey = (double) jd * 256;
        double ttx = mex -this.getScene().getWidth()/2;
        double tty = mey - this.getScene().getHeight()/2;
        setTranslateX(-1* ttx);
        setTranslateY(-1* tty);
         zoomProperty.addListener((ov, t, t1) -> 
            nearestZoom = (Math.min((int) floor(t1.doubleValue() + TIPPING),MAX_ZOOM-1)));
        if (debug)  System.out.println("setCenter, tx = "+this.getTranslateX()+", with = "+this.getScene().getWidth()/2+", mex = "+mex);
        loadTiles();  
    }
    
    /**
     * Move the center of the map horizontally by a number of pixels.
     * After this operation, it will be checked if new tiles need to be downloaded
     * @param dx the number of pixels
     */
    public void moveX(double dx) {
        setTranslateX(getTranslateX() - dx);
        loadTiles();
    }
    
    /**
     * Move the center of the map vertically by a number of pixels.
     * After this operation, it will be checked if new tiles need to be downloaded
     * @param dx the number of pixels
     */
    public void moveY(double dy) {
        double zoom = zoomProperty.get();
        double maxty = 256*Math.pow(2,zoom)-this.getScene().getHeight();
        System.out.println("ty = "+getTranslateY()+" and dy = "+dy);
        if (getTranslateY() <= 0) {
            if (getTranslateY() + maxty >=0) {
                setTranslateY(Math.min(0, getTranslateY() - dy));
            }
            else {
                setTranslateY(-maxty+1);
            }
        } else {
            setTranslateY(0);
        }
        loadTiles();
    }
    
    public void setZoom (double z) {
        zoomProperty.set(z);
    }
     
    public void zoom(double delta, double pivotX, double pivotY) {
        double dz = delta> 0 ? .1: -.1;
        double zp = zoomProperty.get();
        if (debug) System.out.println("Zoom called, zp = "+zp+", delta = "+delta+", px = "+pivotX+", py = "+pivotY);    
        double txold = getTranslateX();
        double t1x = pivotX - getTranslateX();
        double t2x = 1. - Math.pow(2, dz);
        double totX = t1x * t2x;
        double tyold = getTranslateY();
        double t1y = pivotY - tyold;
        double t2y = 1. - Math.pow(2, dz);
        double totY = t1y * t2y;
        System.out.println("zp = "+zp+", txold = "+txold+", totx = "+totX+", tyold = "+tyold+", toty = "+totY);
        if ((delta > 0)) {
            if (zp < MAX_ZOOM) {
                setTranslateX(txold + totX);
                setTranslateY(tyold + totY);
                zoomProperty.set(zp + .1);
                loadTiles();
            }
        } else {
            if (zp > 1) {
                double nz = zp - .1;
                if (Math.pow(2,nz)*256 > this.getScene().getHeight()) {
                // also, we need to fit on the current screen
                    setTranslateX(txold + totX);
                    setTranslateY(tyold + totY);
                    zoomProperty.set(zp - .1);
                    loadTiles();
                }
                else {
                    System.out.println("sorry, would be too small");
                }
            }
        }
        System.out.println("after, zp = "+zoomProperty.get());
    }

    
    public DoubleProperty zoomProperty() {
        return zoomProperty;
    }

    public final void loadTiles() {
        if (getScene() == null) return;
        double activeZoom = zoomProperty.get();
        double deltaZ = nearestZoom - activeZoom;
        long i_max = 1 << nearestZoom;
        long j_max = 1 << nearestZoom;
        double tx = getTranslateX();
        double ty = getTranslateY();
        double width = getScene().getWidth();
        double height = getScene().getHeight();
        long imin = Math.max(0, (long) (-tx * Math.pow(2, deltaZ) / 256) - 1);
        long jmin = Math.max(0, (long) (-ty * Math.pow(2, deltaZ) / 256));
        long imax = Math.min(i_max,imin + (long) (width * Math.pow(2, deltaZ) / 256) + 3);
        long jmax = Math.min(j_max, jmin + (long) (height * Math.pow(2, deltaZ) / 256) + 3);
//        if (debug) {
//            System.out.println("j_max = "+j_max+", "+(long) (height * Math.pow(2, deltaZ) / 256));
//            System.out.println("jmax = "+jmax);
//            System.out.println("zoom = "+nearestZoom+", loadtiles, check i-range: " + imin + ", " + imax + " and j-range: " + jmin + ", " + jmax);
//        }
        for (long i = imin; i < imax; i++) {
            for (long j = jmin; j < jmax; j++) {
                Long key = i * i_max + j;
               // LongTuple it = new LongTuple(i,j);
                SoftReference<MapTile> ref = tiles[nearestZoom].get(key);
                if ((ref == null) || (ref.get() == null)) {
                    if (ref != null) {
                        System.out.println("RECLAIMED: z=" + nearestZoom + ",i=" + i + ",j=" + j);
                    }
                    MapTile tile = new MapTile(this, nearestZoom, i, j);
                    tiles[nearestZoom].put(key, new SoftReference<>(tile));
//                    MapTile covering = tile.getCovering();
//                    if (covering != null) {
//                        if (!getChildren().contains(covering)) {
//                            getChildren().add(covering);
//                        }
//                    }

                    getChildren().add(tile);
                } else {
                    MapTile tile = ref.get();
                    if (!getChildren().contains(tile)) {
                        getChildren().add(tile);
                    }
                }
            }
        }
        // cleanupTiles();
    }

}
