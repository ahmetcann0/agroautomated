package server.ws;
 
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import database.FirebaseStorageInteraction;
import database.RealtimeDatabaseInteraction;
import model.ProcessMessage;
import model.WriteFile;
import entities.Plant;
 
@ServerEndpoint("/websocketendpoint")
public class WsServer {
	 private Session session;
	 
	 Map<Integer, Session> sessions = new ConcurrentHashMap<>();
	 HashMap<Integer, HashMap<String, Plant>> userPlants = new HashMap<Integer, HashMap<String, Plant>>();
	 
	 //Integer: userId, String:plantId, Plant: Plant object.

	 private RealtimeDatabaseInteraction db;
     private ProcessMessage pm = ProcessMessage.getInstance();

    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException, ExecutionException{

    	this.session = session;
    	System.out.println("Client has been connected: "+session.getId());
        session.getBasicRemote().sendText("Websocket Connection Established!");
        
//        JSONObject jo = new JSONObject();
//        jo.put("clientId", session.getId());
//        session.getBasicRemote().sendText(jo.toString());

    }
     
    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");

    }
     
    @OnMessage
    public void onMessage(String message) throws IOException, InterruptedException{
    	
        System.out.println("Message from the client: " + message);
        
        
		JSONObject userMessage = new JSONObject(message); 
        int userId = Integer.parseInt(userMessage.get("clientId").toString());
        
        boolean ifUserIdPresent = sessions.containsKey(Integer.valueOf(userId));
		if(!ifUserIdPresent) {
			sessions.put(Integer.valueOf(userId), session); //Added user with the id to the HashMap.
		}
		boolean isPlantIdPresent = userPlants.containsKey(Integer.valueOf(userId));
		if(!isPlantIdPresent) {
	        userPlants.put(Integer.valueOf(userId), new HashMap<String, Plant>());
		}
   
        long startTime = System.currentTimeMillis();
        String result = pm.process(userId, userPlants, message);
        System.out.println(result);
        long stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time is: " +(stopTime-startTime)+" ms");
           
		//JSONObject jo = new JSONObject(message); 

        session.getBasicRemote().sendText("Process finished client can send message!");
        

//        FirebaseStorageInteraction dbStorage = new FirebaseStorageInteraction();
//    	dbStorage.initialize();
//
//    	dbStorage.close(); 
        	
    }
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
 
}