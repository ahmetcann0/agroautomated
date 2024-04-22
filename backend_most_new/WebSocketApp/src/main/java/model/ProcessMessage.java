package model;

import database.FirebaseStorageInteraction;
import database.RealtimeDatabaseInteraction;

import java.io.IOException;
import java.util.Map;

import org.json.*;

public class ProcessMessage{
	private RealtimeDatabaseInteraction rdi;
	private RealtimeDatabaseInteraction db;
	private FirebaseStorageInteraction dbStorage;
	
	private static ProcessMessage processMessageObject = null;

	private ProcessMessage() {
		try {
			db = RealtimeDatabaseInteraction.getInstance();
			db.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		try {
			dbStorage = FirebaseStorageInteraction.getInstance();
			dbStorage.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static ProcessMessage getInstance() {
		if(processMessageObject == null)
			processMessageObject = new ProcessMessage();
		return processMessageObject;	
	}
	
	public void process(String message) throws IOException, InterruptedException {
		JSONObject jo = new JSONObject(message); 
		if("retrieveAll".compareTo(jo.get("command").toString()) == 0) {
	    	int ifInitializedSuccessfully = db.initialize();
	    	db.retrieveCurrentData("ImgK7HBzrLhoCm0xoWE2");
	    	db.close(); //Close the connection to not interfere with another connections.
		}
		else if("writeDataToRealtime".compareTo(jo.get("command").toString()) == 0) {
			db.updateHumidity(Integer.parseInt(jo.get("humidity").toString()), jo.get("plantId").toString());
			db.updateWaterLevel(Double.parseDouble(jo.get("water_level").toString()), jo.get("plantId").toString());
			db.updateSoilMoisture(Integer.parseInt(jo.get("soil_moisture").toString()), jo.get("plantId").toString());
			db.updateTemperature(Integer.parseInt(jo.get("temperature").toString()), jo.get("plantId").toString());

			//db.close(); //Close the connection to not interfere with another connections.
			//db = null;
		}
		else if("writeDataToStorage".compareTo(jo.get("command").toString()) == 0) {			

	        String[] arr = {"humidity:"+jo.get("humidity").toString(), "water_level:"+jo.get("water_level").toString(),"soil_moisture:"+jo.get("soil_moisture").toString(), "temperature:"+jo.get("temperature").toString() };
	    	WriteFile wf = new WriteFile(jo.get("plantId").toString()+".txt");
	        wf.writeLineDataAndTimestamp(arr); 
	        
	        String filePath = "C:\\Users\\Deniz\\OneDrive\\Belgeler\\GitHub\\agroautomated_cloned\\agroautomated\\backend_most_new\\WebSocketApp\\src\\filename.txt";
	        dbStorage.uploadAFileToStorage(filePath,"data", jo.get("plantId").toString()+".txt");

	        //dbStorage.downloadAFile("data", jo.get("plantId").toString());
	        //Thread.sleep(2000);

	    	//dbStorage.close(); //Close the connection to not interfere with another connections.
		}
//		if(message.compareTo("Retrieve data") == 0)
//        	rdi.retrieveCurrentData("plant_3");
//        else if(message.split(",")[0].compareTo("Update Humidity") == 0) {
//        	rdi.updateHumidity(Integer.parseInt(message.split(",")[1]), "plant_3");
//        }
	}
	
}