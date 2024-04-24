package model;

import database.FirebaseStorageInteraction;
import database.RealtimeDatabaseInteraction;
import entities.Plant;
import server.ws.WsServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
	
	public String process(int userId, HashMap<Integer, HashMap<String, Plant>> userPlantsObj, String message) throws IOException, InterruptedException {
		HashMap<String, Plant> tempForPlantIdPlant = new HashMap<String, Plant>();
		JSONObject jo = new JSONObject(message); 
		
		if("retrieveAll".compareTo(jo.get("command").toString()) == 0) {
	    	int ifInitializedSuccessfully = db.initialize();
	    	db.retrieveCurrentData("ImgK7HBzrLhoCm0xoWE2");
	    	db.close(); //Close the connection to not interfere with another connections.
		}
		else if("addNewPlantToUser".compareTo(jo.get("command").toString()) == 0) {
			
			String plantId = jo.get("plantId").toString();
			
			boolean ifUserIdPresent = userPlantsObj.containsKey(userId);
			if(ifUserIdPresent) {
				boolean isPlantWithItsPlantIdPresent = (userPlantsObj.get(userId)).containsKey(plantId);
				if(!isPlantWithItsPlantIdPresent) {
					Plant userPlant = new Plant(
							userId, 
							plantId, 
							Double.parseDouble(jo.get("water_level").toString()), 
							Integer.parseInt(jo.get("humidity").toString()),
							Integer.parseInt(jo.get("soil_moisture").toString()), 
							Integer.parseInt(jo.get("temperature").toString()));
					
					tempForPlantIdPlant.put(plantId, userPlant);
					
					userPlantsObj.put(userId, tempForPlantIdPlant);
				}
					
			}
			return(userId + "'s plant with id:"+plantId+" was successfully added!");
			

			//db.close(); //Close the connection to not interfere with another connections.
			//db = null;
		}
		else if("updateExistingPlantValuesRealtime".compareTo(jo.get("command").toString()) == 0) {
			
			String plantId = jo.get("plantId").toString();
			String filePath = "C:\\Users\\Deniz\\OneDrive\\Belgeler\\GitHub\\agroautomated_cloned\\agroautomated\\backend_most_new\\WebSocketApp\\src\\main\\java\\UserPlantRecordFiles\\";
		    String fileName = plantId+".txt";
		    
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy,HH:mm:ss");
	        LocalDateTime now = LocalDateTime.now();
	        String currentTimeAndDate = dtf.format(now);
	        String time = currentTimeAndDate.split(",")[1];
	        String min = time.split(":")[1];
	        String sec = time.split(":")[2];
			
			boolean ifUserIdPresent = userPlantsObj.containsKey(userId);
			if(ifUserIdPresent) {
				boolean isPlantWithItsPlantIdPresent = (userPlantsObj.get(userId)).containsKey(jo.get("plantId").toString());
				if(isPlantWithItsPlantIdPresent) {

					//If Plant With Its PlantId Present
					Plant currentUserPlant = (userPlantsObj.get(userId)).get(jo.get("plantId").toString());
					currentUserPlant.setHumidity(Integer.parseInt(jo.get("humidity").toString()));
					currentUserPlant.setSoil_moisture(Integer.parseInt(jo.get("soil_moisture").toString()));
					currentUserPlant.setWater_level(Double.parseDouble(jo.get("water_level").toString()));
					currentUserPlant.setTemperature(Integer.parseInt(jo.get("temperature").toString()));

					
					db.updaterDriver(userId, plantId,userPlantsObj);
					
					String[] arr = {"humidity:"+jo.get("humidity").toString(), "water_level:"+jo.get("water_level").toString(),"soil_moisture:"+jo.get("soil_moisture").toString(), "temperature:"+jo.get("temperature").toString() };
			    	WriteFile wf = new WriteFile(
			    			filePath,
			    			fileName);
			        wf.writeLineDataAndTimestamp(arr); 
			        
			        if(Integer.parseInt(min) % 1 == 0 && Double.parseDouble(sec) < 10) {						
						System.out.println("hereeee");			        
				        dbStorage.uploadAFileToStorage(filePath, fileName);
					}

					return(userId + "'s plant with id:"+plantId+" realtime values was successfully updated and written to local file!");
				}		
			}
			return(userId + "'s "+plantId+" plant's values has been updated on the Realtime Database!");
			
			//db.close(); //Close the connection to not interfere with another connections.
		}

		else if("writeDataToStorage".compareTo(jo.get("command").toString()) == 0) {			

	        //dbStorage.downloadAFile("data", jo.get("plantId").toString());
	        //Thread.sleep(2000);

	    	//dbStorage.close(); //Close the connection to not interfere with another connections.
		}
		

		return "Operation was not successful!";
//		if(message.compareTo("Retrieve data") == 0)
//        	rdi.retrieveCurrentData("plant_3");
//        else if(message.split(",")[0].compareTo("Update Humidity") == 0) {
//        	rdi.updateHumidity(Integer.parseInt(message.split(",")[1]), "plant_3");
//        }
	}
	
}