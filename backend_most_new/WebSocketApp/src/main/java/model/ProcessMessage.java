package model;

import database.FCMSender;
import database.FirebaseStorageInteraction;
import database.RealtimeDatabaseInteraction;
import entities.Plant;
import server.ws.WsServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
							Double.parseDouble(jo.get("soil_moisture").toString()), 
							Double.parseDouble(jo.get("temperature").toString()), 
							Integer.parseInt(jo.get("conductivity").toString()),
							Double.parseDouble(jo.get("temperature").toString()), 
							Integer.parseInt(jo.get("nitrogen").toString()),
							Integer.parseInt(jo.get("phosporus").toString()),
							Integer.parseInt(jo.get("potasium").toString()),
							Double.parseDouble(jo.get("weather_humidity").toString()), 
							Double.parseDouble(jo.get("weather_temperature").toString()));
					
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
					currentUserPlant.setSoil_moisture(Double.parseDouble(jo.get("soil_moisture").toString()));
					currentUserPlant.setWater_level(Double.parseDouble(jo.get("water_level").toString()));
					currentUserPlant.setTemperature(Double.parseDouble(jo.get("temperature").toString()));
					currentUserPlant.setConductivity(Integer.parseInt(jo.get("conductivity").toString()));
					currentUserPlant.setPh(Double.parseDouble(jo.get("ph").toString()));
					currentUserPlant.setNitrogen(Integer.parseInt(jo.get("nitrogen").toString()));
					currentUserPlant.setPhosporus(Integer.parseInt(jo.get("phosporus").toString()));
					currentUserPlant.setPotasium(Integer.parseInt(jo.get("potasium").toString()));
					currentUserPlant.setWeather_humidity(Double.parseDouble(jo.get("weather_humidity").toString()));
					currentUserPlant.setWeather_temperature(Double.parseDouble(jo.get("weather_temperature").toString()));

					int nitrogen = currentUserPlant.getNitrogen();
					int phosporus = currentUserPlant.getPhosporus();
					int potasium = currentUserPlant.getPotasium();
					double soil_temperature = currentUserPlant.getTemperature();
					double soil_humidity = currentUserPlant.getSoil_moisture();
					double ph = currentUserPlant.getPh();
					Random r = new Random();
					//double rainfall = 20 + (300 - 20) * r.nextDouble();
					double rainfall = 100.0;
					System.out.println("rainfall value:" +rainfall);
					
					

					String cropRecommendationFromAi = PredictUsingAI.predictCrop(nitrogen, phosporus, potasium, soil_temperature, soil_humidity, ph, rainfall);
					System.out.println(cropRecommendationFromAi);
					db.updaterDriver(userId, plantId,userPlantsObj, cropRecommendationFromAi);
					
					String[] arr = {jo.get("weather_humidity").toString().trim()+","+jo.get("weather_temperature").toString().trim()+","+jo.get("soil_moisture").toString().trim()+","+jo.get("water_level").toString().trim()+","+jo.get("temperature").toString().trim() +","+
							jo.get("conductivity").toString() +","+ jo.get("ph").toString().trim() +","+ jo.get("nitrogen").toString() +","+ jo.get("phosporus").toString() +","+ jo.get("potasium").toString()};
			    	WriteFile wf = new WriteFile(
			    			filePath,
			    			fileName);
			        wf.writeLineDataAndTimestamp(arr); 
			        
			        if(Integer.parseInt(min) % 1 == 0 && Double.parseDouble(sec) < 10) {						
			        	System.out.println("Written existing file to Firebase Storage!!!");			        
				        dbStorage.uploadAFileToStorage(filePath, fileName);
					}
			        
			        if(Double.parseDouble(jo.get("soil_moisture").toString()) < 300.0) {
			        	try {
//							FCMSender.sendMessageToFcmRegistrationToken();
							System.out.println("Sended Notification!!!");
						} catch (Exception e) {
							e.printStackTrace();
						}
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