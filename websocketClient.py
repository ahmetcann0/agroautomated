import asyncio
import json
import random
import websockets
import time



async def hello():
    uri = "ws://192.168.56.1:8085/WebSocketApp/websocketendpoint"  # Change this to your WebSocket server URI
    clientId = 0
    async with websockets.connect(uri) as websocket:
        humidity = 40
        temperature = 25
        water_level = 200000.9
        soil_moisture = 200
        plantId = "ImgK7HBzrLhoCm0xoWE2"

        addPlantToClientJSON = json.dumps({
            "clientId": 128094712,
            "command": "addNewPlantToUser",
            "humidity": humidity,
            "temperature": temperature,
            "water_level": water_level,
            "soil_moisture": soil_moisture,
            "plantId": plantId
        })

        await websocket.send(addPlantToClientJSON)
        response = await websocket.recv()

        while(True):
            response = await websocket.recv()
            print(f"Response from server: {response}")

            if(response == "Process finished client can send message!"):
                time.sleep(10)
                #clientRetrieveDataJSON = '{ "clientId":687567, command:"retrieveAll", plantId:"ImgK7HBzrLhoCm0xoWE2"}'
                #await websocket.send(clientRetrieveDataJSON)
                clientId = 128094712
                humidity = random.randint(0, 100)
                temperature = random.randint(0, 40)
                water_level = round(random.uniform(0, 5), 1)
                soil_moisture = random.randint(0, 2000)
                plantId = "ImgK7HBzrLhoCm0xoWE2"

                updateExistingPlantValuesRealtime = json.dumps({
                    "clientId": clientId,
                    "command": "updateExistingPlantValuesRealtime",
                    "humidity": humidity,
                    "temperature": temperature,
                    "water_level": water_level,
                    "soil_moisture": soil_moisture,
                    "plantId": plantId
                })

                await websocket.send(updateExistingPlantValuesRealtime)

asyncio.run(hello())