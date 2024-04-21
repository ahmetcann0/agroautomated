package server.ws;
 
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import database.FirebaseAccessPoint;
import database.FirebaseStorageInteraction;
import database.RealtimeDatabaseInteraction;
import model.ProcessMessage;
import model.WriteFile;
 
@ServerEndpoint("/websocketendpoint")
public class WsServer {
	 private Session session;
	 private RealtimeDatabaseInteraction db;
     private ProcessMessage pm = new ProcessMessage();
     

	
    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException, ExecutionException{

    	this.session = session;
    	System.out.println("Client has been connected: "+session.getId());
        session.getBasicRemote().sendText("Websocket Connection Established!");
       
    }
     
    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");
    }
     
    @OnMessage
    public void onMessage(String message) throws IOException{
        System.out.println("Message from the client: " + message);
        pm.process(message);
        session.getBasicRemote().sendText("!!!");
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