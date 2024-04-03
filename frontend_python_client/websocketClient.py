import asyncio
import websockets
import time



async def hello():
    uri = "ws://192.168.16.82:8082/WebSocketApp/websocketendpoint"  # Change this to your WebSocket server URI
    async with websockets.connect(uri) as websocket:
        clientPropertiesJSON = '{ "id":687567, command:"initialize", city:"Ankara", location:"Gölbaşı"}'
        await websocket.send(clientPropertiesJSON)
        while(True):
            clientRetrieveDataJSON = '{ "id":687567, command:"retrieveAll", plantId:"plant_3"}'
            await websocket.send(clientRetrieveDataJSON)
            response = await websocket.recv()
            print(f"Response from server: {response}")
            print("here")
            time.sleep(5)

asyncio.run(hello())