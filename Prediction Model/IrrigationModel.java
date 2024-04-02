package org.example;

import weka.core.*;
import weka.classifiers.trees.RandomForest;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.SerializationHelper;


public class IrrigationModel {
    public static void main(String[] args) {
        try {
            // Load dataset
            DataSource source = new DataSource("datasets.csv file path");
            Instances data = source.getDataSet();

            // Check if dataset is loaded
            if (data == null) {
                System.err.println("Failed to load the dataset.");
                System.exit(1);
            }

            // Set class index
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            // Build RandomForest classifier
            RandomForest rf_model = new RandomForest();
            rf_model.setNumIterations(100); // Equivalent to n_estimators in Python
            rf_model.setBagSizePercent(80); // Equivalent to max_samples in Python
            rf_model.setSeed(42); // Equivalent to random_state in Python

            // Train model
            rf_model.buildClassifier(data);

            // Save the trained model to a file
            SerializationHelper.write("rf_model.model", rf_model);
            System.out.println("Model saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
