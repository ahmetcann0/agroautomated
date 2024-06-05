package utilities;

import database.RealtimeDatabaseInteraction;
import entities.Plant;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class CheckMoistureLevelIrrigateIfNecessary {
	static RealtimeDatabaseInteraction db = RealtimeDatabaseInteraction.getInstance();
	
	public static String CheckMoistureLevelChangeDatabaseVariable(Plant plantObj) {
		String plantID = plantObj.getPlantId();
		double soil_moisture = plantObj.getSoil_moisture();
		String result="";
		
		  if (soil_moisture < 10) {// Very low soil moisture
		    result = "Water level is critically low! Please water the plants: "+soil_moisture+"%";
			db.updateifIrrigationMustOccur(plantID, true);

		  } 
		  else if (soil_moisture >= 10 && soil_moisture < 30) {// Low soil moisture
		    result = "Soil moisture is low. Consider watering the plants: "+soil_moisture+"%";
			db.updateifIrrigationMustOccur(plantID, true);

		  } 
		  else if (soil_moisture >= 30 && soil_moisture < 50) {// Moderate soil moisture
		    result = "Soil moisture is at a moderate level: "+soil_moisture+"%";
			db.updateifIrrigationMustOccur(plantID, true);

		  }
		  else if (soil_moisture >= 50 && soil_moisture < 70) {// Moderate soil moisture
			result = "Soil moisture is at a moderate level: "+soil_moisture+"%";
			db.updateifIrrigationMustOccur(plantID, false);

		  }
		  else if(soil_moisture >= 70 && soil_moisture < 100) { // Adequate soil moisture
		    result = "Water level is above %70. Might be risky!";
			db.updateifIrrigationMustOccur(plantID, false);

		  }
		 return result;
    }
	
	
	
//	  if (soilMoistureValue > 900) {
//	    // Very low soil moisture
//	    Serial.println();
//	    moisture_indicator = malloc( sizeof "Water level is critically low! Please water the plants."); 
//	    moisture_indicator = "Water level is critically low! Please water the plants.";
//	  } else if (soilMoistureValue > 800) {
//	    // Low soil moisture
//	    moisture_indicator = malloc( sizeof "Soil moisture is low. Consider watering the plants." ); 
//	    moisture_indicator = "Soil moisture is low. Consider watering the plants.";
//	  } else if (soilMoistureValue > 600) {
//	    // Moderate soil moisture
//	    moisture_indicator = malloc( sizeof "Soil moisture is at a moderate level." ); 
//	    moisture_indicator = "Soil moisture is at a moderate level.";
//	  } else {
//	    // Adequate soil moisture
//	    moisture_indicator = malloc( sizeof "Soil moisture is sufficient." ); 
//	    moisture_indicator = "Soil moisture is sufficient.";
//	  }
//	 return moisture_indicator;
}

