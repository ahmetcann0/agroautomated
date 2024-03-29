#include <Arduino.h>
#include "WebSocketClient.h"
#include "ESP8266WiFi.h"

const char* ssid = "SUPERONLINE_WiFi_1709";
const char* password = "3cdSg3cyexbe";
const char* webSocketServerAddress = "localhost"; //  IP address
const uint16_t webSocketServerPort = 8082; //port
const String webSocketEndpoint = "/WebSocketApp/websocketendpoint"; //endpoint


WebSocketClient ws(true);

void setup() {
	Serial.begin(115200);
	WiFi.begin("MyWifi", "secret");

	Serial.print("Connecting");
	while (WiFi.status() != WL_CONNECTED) {
		delay(500);
		Serial.print(".");
	}
}

void loop() {
	if (!ws.isConnected()) {
		ws.connect("echo.websocket.org", "/", 443);
	} else {
		ws.send("hello");

		String msg;
		if (ws.getMessage(msg)) {
			Serial.println(msg);
		}
	}
	delay(500);
}
