package model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProvideWeatherInformation {
	
    public void getRainfallInfo() {
    	OkHttpClient client = new OkHttpClient();

        String location = "42.3478,-71.0466";
        String apiKey = "DiHBac7ALdvvJIfc4VtIw6eJVmPc6rb1";
        Request request = new Request.Builder()
                .url("https://api.tomorrow.io/v4/weather/forecast?location=" + location + "&apikey=" + apiKey)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful() && response.body() != null) {

                String responseData = response.body().string();


                JSONObject jsonObject = new JSONObject(responseData);
                JSONObject timelines = jsonObject.getJSONObject("timelines");
                JSONArray minutely = timelines.getJSONArray("minutely");

               
                JSONObject minuteData = minutely.getJSONObject(1);
                String time = minuteData.getString("time");
                JSONObject values = minuteData.getJSONObject("values");
                double temperature = values.getDouble("temperature");
                double humidity = values.getDouble("humidity");
                double precipitation = values.getDouble("precipitationProbability");
                    
                System.out.println("Time: " + time + ", Temperature: " + temperature + ", Humidity: " + humidity + ", Precipitation Probability: " + precipitation);
                
            }else 
                System.out.println("Request not successful: " + response);
            
            
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}