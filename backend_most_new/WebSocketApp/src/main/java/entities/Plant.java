package entities;

public class Plant{
	private int userId;
	private String plantId;
	private double water_level;
	private int humidity;
	private int soil_moisture;
	private int temperature;
	
	public Plant(int userId, String plantId, double water_level, int humidity, int soil_moisture, int temperature) {
		this.userId = userId;
		this.plantId = plantId;
		this.water_level = water_level;
		this.humidity = humidity;
		this.soil_moisture = soil_moisture;
		this.temperature = temperature;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public double getWater_level() {
		return water_level;
	}

	public void setWater_level(double distance) {
		this.water_level = distance;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getSoil_moisture() {
		return soil_moisture;
	}

	public void setSoil_moisture(int soil_moisture) {
		this.soil_moisture = soil_moisture;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
}