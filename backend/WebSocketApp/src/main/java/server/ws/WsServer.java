package server.ws;
 
import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import database.RealtimeDatabaseInteraction;
 
@ServerEndpoint("/websocketendpoint")
public class WsServer {
	 private Session session;
	 private RealtimeDatabaseInteraction rdi;
	
    @OnOpen
    public void onOpen(Session session) throws IOException{
    	rdi = new RealtimeDatabaseInteraction();
    	int ifInitializedSuccessfully = rdi.initialize();
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
        if(message.compareTo("Retrieve data") == 0)
        	rdi.retrieveCurrentData("plant_3");
        else if(message.split(",")[0].compareTo("Update Humidity") == 0) {
        	rdi.updateHumidity(Integer.parseInt(message.split(",")[1]), "plant_3");
        }
        session.getBasicRemote().sendText("!!!");
        	
    }
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
 
}