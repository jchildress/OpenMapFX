package org.lodgon.openmapfx.service.miataru;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import org.datafx.provider.ObjectDataProvider;
import org.datafx.provider.ObjectDataProviderBuilder;
import org.datafx.reader.RestSourceBuilder;
import org.datafx.reader.converter.JsonConverter;

/**
 *
 * @author johan
 */
public class Communicator {

//    private static String SERVER = "http://192.168.1.6:8080/miataru/v1";
    private static String SERVER = "http://service.miataru.com/v1";
   // private static String SERVER = "http://localhost:8080/miataru/v1";
  //  private static String SERVER = "http://lodgon.dyndns.org:9999/miataru/v1";
//
//    public static void retrieveLocation (Device device) {
//        GetLocation gl = new GetLocation().device(device.getName());
//        ObjectProperty<MiataruGetLocationResponse> resultProperty = new SimpleObjectProperty<>();
//        JsonConverter converter = new JsonConverter(MiataruGetLocationResponse.class);
//            RestSourceBuilder rsb = RestSourceBuilder.create();
//            rsb.host(SERVER + "/GetLocation")
//                    .contentType("application/json")
//                    .converter(converter)
//                    .dataString(gl.json());
//            ObjectDataProviderBuilder odb = ObjectDataProviderBuilder.create();
//            ObjectDataProvider provider = odb.dataReader(rsb.build()).resultProperty(resultProperty).build();
//            System.out.println("[JVDBG] sending: "+gl.json());
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    Worker worker = provider.retrieve();
//                    worker.stateProperty().addListener(new ChangeListener<Worker.State>() {
//                        @Override
//                        public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
//                            System.out.println("status of getloc from " + t + " to " + t1);
//                            if (t1.equals(Worker.State.SUCCEEDED)) {
//                                System.out.println("[JVDBG] GETLOCATION result = "+resultProperty.get());
//                                System.out.println("[JVDBG]  WorkerLocationresult= "+worker.getValue());
//                            }
//                        }
//                    }
//                    );
//                }
//            });
//    }
    public static void updateLocation(String deviceName, double lat, double lon) {
        if (deviceName.isEmpty()) {
            deviceName = "anonymous android user";
        }
        System.out.println("update loc for " + deviceName + " to " + lon + ", lat = " + lat+", server = "+SERVER);
        try {
            Config c = new Config().enableLocationHistory("True").locationDataRetentionTime("15");
            Location l = new Location().device(deviceName).timestamp(Long.toString(System.currentTimeMillis()/1000))
                    .longitude(Double.toString(lon)).latitude(Double.toString(lat)).horizontalAccuracy("40.00");
            Location[] la = new Location[1];
            la[0] = l;
            UpdateLocation ul = new UpdateLocation().config(c).location(la);
            ObjectProperty<String> resultProperty = new SimpleObjectProperty<>();
            RestSourceBuilder rsb = RestSourceBuilder.create();
            rsb.host(SERVER + "/UpdateLocation")
                    .contentType("application/json")
                    .dataString(ul.json());
            ObjectDataProviderBuilder odb = ObjectDataProviderBuilder.create();
            ObjectDataProvider provider = odb.dataReader(rsb.build()).resultProperty(resultProperty).build();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Worker worker = provider.retrieve();
                    worker.stateProperty().addListener(new ChangeListener<Worker.State>() {

                        @Override
                        public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                            System.out.println("status of updateloc from " + t + " to " + t1);
                        System.out.println("[JVDBG] GETLOCATION result = "+resultProperty.get());
                                System.out.println("[JVDBG]  WorkerLocationresult= "+worker.getValue());

                        }
                    }
                    );
                }
            });

        } catch (Exception ex) {
            System.out.println("Exception while updating location: ");
            ex.printStackTrace();
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
}
