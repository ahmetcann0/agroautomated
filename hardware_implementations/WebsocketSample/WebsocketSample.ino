#include <ArduinoWebsockets.h>
#include <ESP8266WiFi.h>

const char* ssid = "";
const char* password = "";
const char* websockets_server  = "ws://192.168.1.2:8082/WebSocketApp/websocketendpoint"; //localhost yazınca çalışmıyor.

using namespace websockets;

//Globals
bool connectedSuccessfully = false;
bool flag = false;

void onMessageCallback(WebsocketsMessage message) {
    Serial.print("Got Message: ");
    Serial.println(message.data());
}

void onEventsCallback(WebsocketsEvent event, String data) {
    if(event == WebsocketsEvent::ConnectionOpened) {
        Serial.println("Connnection Opened");
        connectedSuccessfully = true;
    } else if(event == WebsocketsEvent::ConnectionClosed) {
        Serial.println("Connnection Closed");
        connectedSuccessfully = false;
    } else if(event == WebsocketsEvent::GotPing) {
        //Serial.println("Got a Ping!");
    } else if(event == WebsocketsEvent::GotPong) {
        //Serial.println("Got a Pong!");
    }
}

WebsocketsClient client;
void setup() {
    Serial.begin(115200);
    WiFi.mode(WIFI_STA);
    WiFi.disconnect();
    delay(1000);

    Serial.println();
    Serial.println();
    Serial.print("Connecting to: ");
    Serial.println(ssid);
    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print("-");
    }
    Serial.println("");
    Serial.println("WiFi Connected");
    Serial.print("IP Address: ");
    Serial.print("http://");
    Serial.println(WiFi.localIP());

    connectWSServer();

    client.onEvent(onEventsCallback);
    client.onMessage(onMessageCallback);

}

void loop() {
    client.poll();
    delay(1000);
    if(!connectedSuccessfully && false)
    Serial.println("Not connected");
      //connectWSServer();
    else{
      client.send("Retrieve data");
      delay(1000);
    }

    if(!flag){
      client.send("Update Humidity,111111");
      flag = true;
    }


    
}

void connectWSServer(){
// Setup Callbacks
      client.onMessage(onMessageCallback);
      
      // Connect to server
      client.connect(websockets_server);

      // Send a message
      client.send("Arduinoyum ben");
}