#include <dht11.h>
dht11 DHT;
#define DHT11_PIN 4
#include <SoftwareSerial.h>
SoftwareSerial ArduinoUno(8,9);

char* getMoistureLevel();
void sendDataToNodeMCU();
float get_distance();

char* moisture_indicator;
const int trigPin = 3;
const int echoPin = 2;

void setup() {
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(10,OUTPUT);
  Serial.begin(9600);
  ArduinoUno.begin(4800);


  //Send data to NodeMCU initially.
  //sendDataToNodeMCU();
}

void loop() {
  digitalWrite(10, HIGH);
  delay(1000);
  int nodemcu_data = ArduinoUno.parseInt();
  if(nodemcu_data == 1){

  }
  
  int chk = DHT.read(DHT11_PIN);
  String temperature = String(DHT.temperature);
  delay(100);
  String humidity = String(DHT.humidity);
  delay(100);
  String distance = String(get_distance());
  
  Serial.print("Distance: ");
  delay(100);
  Serial.print(distance+" / ");
  Serial.print("Temperature = " + temperature+ " / ");
  Serial.print("Humidity = " + humidity+"\n");
}

char* getMoistureLevel()
{
  int soilMoistureValue =analogRead(0);
  char *moisture_indicator;
  if (soilMoistureValue > 900) {
    // Very low soil moisture
    Serial.println();
    moisture_indicator = malloc( sizeof "Water level is critically low! Please water the plants."); 
    moisture_indicator = "Water level is critically low! Please water the plants.";
  } else if (soilMoistureValue > 800) {
    // Low soil moisture
    moisture_indicator = malloc( sizeof "Soil moisture is low. Consider watering the plants." ); 
    moisture_indicator = "Soil moisture is low. Consider watering the plants.";
  } else if (soilMoistureValue > 600) {
    // Moderate soil moisture
    moisture_indicator = malloc( sizeof "Soil moisture is at a moderate level." ); 
    moisture_indicator = "Soil moisture is at a moderate level.";
  } else {
    // Adequate soil moisture
    moisture_indicator = malloc( sizeof "Soil moisture is sufficient." ); 
    moisture_indicator = "Soil moisture is sufficient.";
  }
 return moisture_indicator;

}
float get_distance(){
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  float duration = pulseIn(echoPin, HIGH);
  return (duration*.0343)/2;
}

void sendDataToNodeMCU()
{
  int chk = DHT.read(DHT11_PIN);
  String temperature = String(DHT.temperature);
  delay(100);
  String humidity = String(DHT.humidity);
  delay(100);
  //int soil_moisture_level = analogRead(0);
  String distance = String(get_distance());
  /* String condition_soil_string;
  if (soil_moisture_level > 900) {
    // Very low soil moisture
    Serial.println();
    condition_soil_string = "very low";
  } else if (soil_moisture_level > 800) {
    // Low soil moisture
    condition_soil_string = "low";
  } else if (soil_moisture_level > 600) {
    // Moderate soil moisture
    condition_soil_string = "moderate";
  } else {
    // Adequate soil moisture
    condition_soil_string = "sufficient";
  } */
  
  
    Serial.print("Distance: ");
    delay(100);
    Serial.print(distance+" / ");

    Serial.print("Temperature = " + temperature+ " / ");
    Serial.print("Humidity = " + humidity+"\n");
    delay(100);

    //Serial.print(String(getMoistureLevel())+ "\n");
    delay(100);

    //Send sensor data to NodeMCU Module.
    //String sendSensorData = dis + "|" + String(DHT.temperature) + "|" + String(DHT.humidity) + "|" + String(getMoistureLevel())+"/n";


    //String sendSensorData = distance + "|" + temperature + "|" + humidity + "|" + soil_moisture_level;


    /*String sendSensorData = "Distance:" +dis+ "Temperature = " + String(DHT.temperature)+ " / ""Humidity = " + String(DHT.humidity)+"\n"+String(getMoistureLevel());*/
    //ArduinoUno.print(sendSensorData); //10, Software Serial protokolü ile yollanacak
    //ArduinoUno.print("\n"); //nodemcu kısmındaki nodemcu.read() == '\n' satisfy olacak.
    free(moisture_indicator); 
}
