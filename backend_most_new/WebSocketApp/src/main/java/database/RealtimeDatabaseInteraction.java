package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import entities.Plant;
import entities.SensorEntities;

public class RealtimeDatabaseInteraction {

    private String credentials ;
    private String DATABASE_URL;

    private FirebaseDatabase firebaseDatabase;
    
    private static RealtimeDatabaseInteraction realtimeDatabaseInteractionObject = null;

    private RealtimeDatabaseInteraction() throws IOException {
    	credentials = "C:\\\\Users\\\\Deniz\\\\eclipse-workspace\\\\FirebaseInteraction\\\\agroautomated-8f55e-firebase-adminsdk-mdmjm-56a8631d3b.json";
    	DATABASE_URL = "https://agroautomated-8f55e-default-rtdb.firebaseio.com/";
    }
    public synchronized static RealtimeDatabaseInteraction getInstance() {
    	if(realtimeDatabaseInteractionObject == null) {
    		try {
				realtimeDatabaseInteractionObject = new RealtimeDatabaseInteraction();
			} catch (IOException e) {
				System.out.println("Exception in RealtimeDatabaseInteraction>getInstance()");
				e.printStackTrace();
			}
    	}
    	return realtimeDatabaseInteractionObject;
    	
    }
    public int initialize() throws FileNotFoundException {
    	FileInputStream serviceAccount =
    			new FileInputStream(credentials);

    	FirebaseOptions options;
		try {
			options = FirebaseOptions.builder()
			        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			        .setDatabaseUrl(DATABASE_URL)
			        .build();
			 FirebaseApp.initializeApp(options);
			 return 1;
		} catch (IOException e) {
			return -1;
		}
                       
    }
    public void retrieveCurrentData(String plantName) {
    	 DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sensor_data").child(plantName);
      
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
 			
			  @Override
			  public void onDataChange(DataSnapshot dataSnapshot) {
				  
				    Object document = dataSnapshot.getValue();
	                SensorEntities sensorData = dataSnapshot.getValue(SensorEntities.class);
	                System.out.println("Veri Alındı:");
	                System.out.println("Distance Float: " + sensorData.distance_float);
	                System.out.println("Humidity Integer: " + sensorData.humidity_integer);
	                System.out.println("Information String: " + sensorData.information_string);
	                System.out.println("Soil Moisture Integer: " + sensorData.soil_moisture_integer);
	                System.out.println("Temperature Integer: " + sensorData.temperature_integer);
                
			  }

			  @Override
			  public void onCancelled(DatabaseError error) {
			  }
			});
    }
    
    public void updaterDriver(int userId, String plantId,  HashMap<Integer, HashMap<String, Plant>> userId_PlantId_Plant_HashMap) {
    	Plant correspondingUsersPlant = (userId_PlantId_Plant_HashMap.get(userId)).get(plantId); 
    	
    	updateHumidity(plantId, correspondingUsersPlant.getHumidity());
		updateWaterLevel(plantId, correspondingUsersPlant.getWater_level());
		updateSoilMoisture(plantId, correspondingUsersPlant.getSoil_moisture());
		updateTemperature(plantId, correspondingUsersPlant.getTemperature());
    }

    public void updateHumidity(String plantId, int humidity) {
        try {
        	DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sensor_data").child(plantId).child("humidity_integer");
            final CountDownLatch latch = new CountDownLatch(1);
            ref.setValue(humidity, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void updateWaterLevel(String plantId,double distance_float) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sensor_data").child(plantId).child("distance_float");
            final CountDownLatch latch = new CountDownLatch(1);
            ref.setValue(distance_float, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        latch.countDown();
                    } 
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updateTemperature(String plantId, int temperature_integer) {
        try {
        	DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sensor_data").child(plantId).child("temperature_integer");
            final CountDownLatch latch = new CountDownLatch(1);
            ref.setValue(temperature_integer, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        latch.countDown();
                    } 
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updateSoilMoisture(String plantId, int soil_moisture_integer) {
        try {
        	DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sensor_data").child(plantId).child("soil_moisture_integer");
            final CountDownLatch latch = new CountDownLatch(1);
            ref.setValue(soil_moisture_integer, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
    	FirebaseDatabase.getInstance().getApp().delete();
    }
}