package model;

import database.FirebaseStorageInteraction;
import database.RealtimeDatabaseInteraction;

import java.io.IOException;
import java.util.Map;

import org.json.*;

public class ProcessMessage{
	 private RealtimeDatabaseInteraction rdi;
	 


	public void process(String message) throws IOException {
		rdi = new RealtimeDatabaseInteraction();
		JSONObject jo = new JSONObject(message); 
		if("retrieveAll".compareTo(jo.get("command").toString()) == 0) {
			RealtimeDatabaseInteraction db = new RealtimeDatabaseInteraction();
	    	int ifInitializedSuccessfully = db.initialize();
	    	db.retrieveCurrentData("ImgK7HBzrLhoCm0xoWE2");
	    	db.close(); //Close the connection to not interfere with another connections.
		}
		else if("writeDataToRealtime".compareTo(jo.get("command").toString()) == 0) {
			RealtimeDatabaseInteraction db = new RealtimeDatabaseInteraction();
	    	int ifInitializedSuccessfully = db.initialize();
			rdi.updateHumidity(Integer.parseInt(jo.get("humidity").toString()), jo.get("plantId").toString());
			rdi.updateWaterLevel(Double.parseDouble(jo.get("water_level").toString()), jo.get("plantId").toString());
			rdi.updateSoilMoisture(Integer.parseInt(jo.get("soil_moisture").toString()), jo.get("plantId").toString());
			rdi.updateTemperature(Integer.parseInt(jo.get("temperature").toString()), jo.get("plantId").toString());
			db.close(); //Close the connection to not interfere with another connections.
		}
		else if("writeDataToStorage".compareTo(jo.get("command").toString()) == 0) {
			FirebaseStorageInteraction dbStorage = new FirebaseStorageInteraction();

	        String[] arr = {"humidity:"+jo.get("humidity").toString(), "water_level:"+jo.get("water_level").toString(),"soil_moisture:"+jo.get("soil_moisture").toString(), "temperature:"+jo.get("temperature").toString() };
	    	WriteFile wf = new WriteFile("text.txt");
	        wf.writeLineDataAndTimestamp(arr); 
	        
	    	dbStorage.initialize();

	    	dbStorage.close(); //Close the connection to not interfere with another connections.
		}
//		if(message.compareTo("Retrieve data") == 0)
//        	rdi.retrieveCurrentData("plant_3");
//        else if(message.split(",")[0].compareTo("Update Humidity") == 0) {
//        	rdi.updateHumidity(Integer.parseInt(message.split(",")[1]), "plant_3");
//        }
	}
	
}