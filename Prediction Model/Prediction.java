package org.example;

import weka.classifiers.Classifier;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.SerializationHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Prediction {
    public static void main(String[] args) {
        try {
            // Load trained model
            Classifier rf_model = (Classifier) SerializationHelper.read("rf_model.model path");

            // Load dataset attributes
            DataSource source = new DataSource("datasets.csv file path");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            // Take user input
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Create an empty instance with attributes from the dataset
            Instance user_input = new DenseInstance(data.numAttributes());
            user_input.setDataset(data);

            // Input CropType
            System.out.print("Crop Type: ");
            String cropType = reader.readLine();
            user_input.setValue(data.attribute("CropType"), cropType);

            // Input CropDays
            System.out.print("Crop Days: ");
            int cropDays = Integer.parseInt(reader.readLine());
            user_input.setValue(data.attribute("CropDays"), cropDays);

            // Input SoilMoisture
            System.out.print("Soil Moisture: ");
            int soilMoisture = Integer.parseInt(reader.readLine());
            user_input.setValue(data.attribute("SoilMoisture"), soilMoisture);

            // Input Temperature
            System.out.print("Temperature: ");
            int temperature = Integer.parseInt(reader.readLine());
            user_input.setValue(data.attribute("temperature"), temperature);

            // Input Humidity
            System.out.print("Humidity: ");
            int humidity = Integer.parseInt(reader.readLine());
            user_input.setValue(data.attribute("Humidity"), humidity);

            // Classify user input
            double prediction = rf_model.classifyInstance(user_input);

            float pr = Math.round(prediction);
            System.out.println(pr);
            // Output prediction
            if (pr == 1.0) {
                System.out.println("Start irrigation.");
            } else {
                System.out.println("Do not do irrigation.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
