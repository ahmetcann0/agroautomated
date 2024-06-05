package utilities;
import java.util.HashMap;
import java.util.Map;

import database.FCMSender;
import entities.Plant;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SensorNotifier {
    private static final double SOIL_MOISTURE_THRESHOLD = 300.0;
    private static final double SOIL_TEMPERATURE_THRESHOLD = 15;

    private static final long NOTIFICATION_INTERVAL_HOURS = 8;

    private Map<String, LocalDateTime> lastNotificationTimes = new HashMap<>();

    public SensorNotifier() {
    	
        lastNotificationTimes.put("soil_moisture", LocalDateTime.MIN);
        lastNotificationTimes.put("soil_temperature", LocalDateTime.MIN);
    }

    public void checkAndSendNotifications(Plant plant) {
        double soilMoisture = plant.getSoil_moisture();
        double soil_temperature = plant.getTemperature();
        
        if (soilMoisture < SOIL_MOISTURE_THRESHOLD) {
            sendNotificationIfAllowed("soil_moisture");
            
        }else if(soilMoisture < SOIL_TEMPERATURE_THRESHOLD){
            sendNotificationIfAllowed("soil_temperature");
        }

    }

    private void sendNotificationIfAllowed(String sensorName) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastNotificationTime = lastNotificationTimes.get(sensorName);

        if (ChronoUnit.HOURS.between(lastNotificationTime, now) >= NOTIFICATION_INTERVAL_HOURS) {
            try {
                FCMSender.sendMessageToFcmRegistrationToken("Water level is low!");
                System.out.println("Sended Notification for " + sensorName + "!!!");
                lastNotificationTimes.put(sensorName, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Notification for " + sensorName + " already sent today.");
        }
    }

}



