package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseStorageInteraction{
	private String credentials ;
    private String DATABASE_URL;

    private FirebaseDatabase firebaseDatabase;

    public FirebaseStorageInteraction() throws IOException {
    	credentials = "C:\\\\Users\\\\Deniz\\\\eclipse-workspace\\\\FirebaseInteraction\\\\agroautomated-8f55e-firebase-adminsdk-mdmjm-56a8631d3b.json";
    	DATABASE_URL = "https://agroautomated-8f55e-default-rtdb.firebaseio.com/";
    }
    public void initialize() throws FileNotFoundException {
    	FileInputStream serviceAccount =
    			new FileInputStream(credentials);
    	Map<String, Object> auth = new HashMap<String, Object>();

    	FirebaseOptions options;
		try {
			options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .setStorageBucket("agroautomated-8f55e.appspot.com")
                    .build();
			FirebaseApp fireApp = FirebaseApp.initializeApp(options);
			StorageClient storageClient = StorageClient.getInstance(fireApp);
			InputStream testFile = new FileInputStream("C:\\Users\\Deniz\\eclipse-workspace\\WebSocketApp\\src\\filename.txt");
			String blobString = "userImages/" + "filename.txt";        

			storageClient.bucket().create(blobString, testFile , Bucket.BlobWriteOption.userProject("agroautomated-8f55e"));
     

        storageClient.bucket().create(blobString, testFile , Bucket.BlobWriteOption.userProject("agroautomated-8f55e"));
		} catch (IOException e) {
		}
        
		               
    }
    public void close() {
    	FirebaseDatabase.getInstance().getApp().delete();
    }

    
}
