package database;

import java.io.FileInputStream;
import entities.SensorEntities;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import entities.SensorEntities;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseAccessPoint {
	private static String credentials ;
    private static String DATABASE_URL;
    private static FirebaseApp firebaseAppAccessPoint = null;

    private FirebaseDatabase firebaseDatabase;
    //private Firestore firestoreDatabase;
    
    private FirebaseAccessPoint() throws IOException {
    	credentials = "C:\\\\Users\\\\Deniz\\\\eclipse-workspace\\\\FirebaseInteraction\\\\agroautomated-8f55e-firebase-adminsdk-mdmjm-56a8631d3b.json";
    	DATABASE_URL = "https://agroautomated-8f55e-default-rtdb.firebaseio.com/";
    }
    
    public static synchronized FirebaseApp getInstance() throws IOException {
    	FileInputStream serviceAccount =
    			new FileInputStream(credentials);
    	//Map<String, Object> auth = new HashMap<String, Object>();
    	//auth.put("uid", "backend-server");

    	FirebaseOptions options;
    	options = FirebaseOptions.builder()
			        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			        .setDatabaseUrl(DATABASE_URL)
			        .build();
			 
    	if(firebaseAppAccessPoint == null)
    		firebaseAppAccessPoint = FirebaseApp.initializeApp(options);
    	 return firebaseAppAccessPoint;
    }
    

    public void retrieveCurrentDataRealtime(String plantName) {
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
    public void retrieveCurrentDataFromFirestore(String plantName) throws InterruptedException, ExecutionException {
    	// asynchronously retrieve all users
    	ApiFuture<QuerySnapshot> query = firestoreDatabase.collection("plants").get();
    	// ...
    	// query.get() blocks on response
    	QuerySnapshot querySnapshot = query.get();
    	List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
    	for (QueryDocumentSnapshot document : documents) {
    	  //System.out.println("User: " + document.getId());
    	  System.out.println(document);
    	}
   }

    public void updateHumidityRealtime(int humidity, String plantName) {
        try {
        	DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sensor_data").child(plantName).child("humidity_integer");
            final CountDownLatch latch = new CountDownLatch(1);
            ref.setValue(humidity, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        latch.countDown();
                    } else {
                        System.out.println("Data saved successfully.");
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updateDistanceRealtime(float distance_float, String plantName) {
        try {
            DatabaseReference ref = firebaseDatabase.getReference("sensor_data").child(plantName).child("distance_float");
            final CountDownLatch latch = new CountDownLatch(1);
            ref.setValue(distance_float, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        latch.countDown();
                    } else {
                        System.out.println("Data saved successfully.");
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
        firebaseDatabase.getApp().delete();
    }
}
