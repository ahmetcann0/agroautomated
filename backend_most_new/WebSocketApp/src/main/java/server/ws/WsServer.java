package server.ws;
 
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
 
@ServerEndpoint("/websocketendpoint")
public class WsServer {
	 private Session session;
	 private RealtimeDatabaseInteraction db;
     private ProcessMessage pm = ProcessMessage.getInstance();

	
    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException, ExecutionException{

    	this.session = session;
    	System.out.println("Client has been connected: "+session.getId());
        session.getBasicRemote().sendText("Websocket Connection Established!");
        
        JSONObject jo = new JSONObject();
        jo.put("clientId", session.getId());
        session.getBasicRemote().sendText(jo.toString());

    }
     
    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");

    }
     
    @OnMessage
    public void onMessage(String message) throws IOException, InterruptedException{
        System.out.println("Message from the client: " + message);
        long startTime = System.currentTimeMillis();
        pm.process(message);
        long stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time is: " +(stopTime-startTime)+" ms");
        Thread.sleep(1000);
		JSONObject jo = new JSONObject(message); 

        session.getBasicRemote().sendText("Process finished client can send message!");

        
        
//        session.getBasicRemote().sendText("Process finished client can send message!");
    	//wf.writeLineDataAndTimestamp("Bu bir örnek satır.");
    	//wf.writeLineDataAndTimestamp("Başka bir örnek satır.");

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