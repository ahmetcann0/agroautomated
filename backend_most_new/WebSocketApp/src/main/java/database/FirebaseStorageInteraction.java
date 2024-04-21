package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
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
    public void initialize() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(credentials);

        FirebaseOptions options =  FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(DATABASE_URL)
                .setStorageBucket("agroautomated-8f55e.appspot.com") // Firebase Storage kova adı
                .build();

        FirebaseApp.initializeApp(options);

        // Firebase Storage istemcisini oluştur
        StorageClient storageClient = StorageClient.getInstance();

        // Yüklenecek dosyanın yolu ve adı
        String filePath = "C:\\Users\\Deniz\\OneDrive\\Belgeler\\GitHub\\agroautomated_cloned\\agroautomated\\backend_most_new\\WebSocketApp\\src\\filename.txt";
        String destinationPath = "data/filename.txt";

        // Dosyayı yükle
        try (InputStream testFile = new FileInputStream(filePath)) {

      	  Bucket bucket = storageClient.bucket();
      	  bucket.create(destinationPath, testFile);
      	  System.out.println("Dosya başarıyla yüklendi.");
      	} catch (Exception e) {
      	  System.err.println("Dosya yüklenirken bir hata oluştu: " + e.getMessage());
      	}
    }
    public void close() {
    	FirebaseDatabase.getInstance().getApp().delete();
    }

    
}
