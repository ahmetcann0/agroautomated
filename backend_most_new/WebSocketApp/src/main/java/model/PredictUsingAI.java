package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.netlib.util.doubleW;

import java_cup.internal_error;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class PredictUsingAI {
	
	private String cropType;
	private int cropDays;
	private int humidity;
	private int soilMoisture;
	private int temperature;
	private double Nitrogen;
	private double Phosphorous;
	private double Potassium;
	private double pH; 
	private double Rainfall;

	public PredictUsingAI(String cropType, int cropDays, int soilMoisture, int temperature ,int humidity, double nitrogen, double potassium, double ph, double rainfall) {
		this.cropType = cropType;
		this.cropDays = cropDays;
		this.soilMoisture = soilMoisture;
		this.temperature = temperature;
		this.humidity = humidity;
		this.Nitrogen = nitrogen;
		this.Potassium = potassium;
		this.pH = ph;
		this.Rainfall = rainfall;
	}
	
	public void predictIrrigation(String Croptype, int Cropdays, int Soilmoisture, int Temperature ,int Humidity) {
        try {
            Classifier rf_model = (Classifier) SerializationHelper.read("C:\\Users\\kaan_\\Desktop\\Predict\\wekaproject\\rf_model.model");

            DataSource source = new DataSource("C:\\Users\\kaan_\\Desktop\\untitled\\src\\main\\java\\org\\example\\datasets.csv");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            Instance user_input = new DenseInstance(data.numAttributes());
            user_input.setDataset(data);

            System.out.print("Crop Type: ");
            String cropType = Croptype;
            user_input.setValue(data.attribute("CropType"), cropType);

            System.out.print("Crop Days: ");
            int cropDays = Cropdays;
            user_input.setValue(data.attribute("CropDays"), cropDays);

            System.out.print("Soil Moisture: ");
            //int soilMoisture = Integer.parseInt(reader.readLine());
            int soilMoisture = Soilmoisture;
            user_input.setValue(data.attribute("SoilMoisture"), soilMoisture);

            System.out.print("Temperature: ");
            //int temperature = Integer.parseInt(reader.readLine());
            int temperature = Temperature;
            user_input.setValue(data.attribute("temperature"), temperature);

            System.out.print("Humidity: ");
            //int humidity = Integer.parseInt(reader.readLine());
            int humidity = Humidity;
            user_input.setValue(data.attribute("Humidity"), humidity);

            double prediction = rf_model.classifyInstance(user_input);

            float pr = Math.round(prediction);
            

            if (pr == 1.0) {
                System.out.println("1");
            } else {
                System.out.println("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void predictCrop(double Nitrogen, double Phosphorous, double Potassium, double Temperature, double Humidity, double pH, double Rainfall ) {
		try {
            
            Classifier crf_model = (Classifier) SerializationHelper.read("C:\\Users\\kaan_\\Desktop\\wekaproject\\cropmodel.model");

            
            DataSource source = new DataSource("C:\\Users\\kaan_\\Desktop\\Crop_recommendation.csv");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            
            Instance user_input = new DenseInstance(data.numAttributes());
            user_input.setDataset(data);

            
            System.out.print("Enter Nitrogen content (N): ");
            double N = Nitrogen;
            user_input.setValue(data.attribute("N"), N);

            
            System.out.print("Enter Phosphorous content (P): ");
            double P = Phosphorous;
            user_input.setValue(data.attribute("P"), P);

            
            System.out.print("Enter Potassium content (K): ");
            double K = Potassium;
            user_input.setValue(data.attribute("K"), K);

            
            System.out.print("Enter Temperature (°C): ");
            double temperature = Temperature;
            user_input.setValue(data.attribute("temperature"), temperature);

            
            System.out.print("Enter Humidity (%): ");
            double humidity = Humidity;
            user_input.setValue(data.attribute("humidity"), humidity);

           
            System.out.print("Enter pH value: ");
            double ph = pH;
            user_input.setValue(data.attribute("ph"), ph);

            
            System.out.print("Enter Rainfall (mm): ");
            double rainfall = Rainfall;
            user_input.setValue(data.attribute("rainfall"), rainfall);

            
            double prediction = crf_model.classifyInstance(user_input);

            
            String predictedLabel = data.classAttribute().value((int) prediction);
            System.out.println("Predicted crop label: " + predictedLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public String getCropType() {
		return cropType;
	}

	public void setCropType(String cropType) {
		this.cropType = cropType;
	}

	public int getCropDays() {
		return cropDays;
	}

	public void setCropDays(int cropDays) {
		this.cropDays = cropDays;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getSoilMoisture() {
		return soilMoisture;
	}

	public void setSoilMoisture(int soilMoisture) {
		this.soilMoisture = soilMoisture;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public double getNitrogen() {
		return Nitrogen;
	}

	public void setNitrogen(double nitrogen) {
		Nitrogen = nitrogen;
	}

	public double getPhosphorous() {
		return Phosphorous;
	}

	public void setPhosphorous(double phosphorous) {
		Phosphorous = phosphorous;
	}

	public double getPotassium() {
		return Potassium;
	}

	public void setPotassium(double potassium) {
		Potassium = potassium;
	}

	public double getpH() {
		return pH;
	}

	public void setpH(double pH) {
		this.pH = pH;
	}

	public double getRainfall() {
		return Rainfall;
	}

	public void setRainfall(double rainfall) {
		Rainfall = rainfall;
	}
	
}
