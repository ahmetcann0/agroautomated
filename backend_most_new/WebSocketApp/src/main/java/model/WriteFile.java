package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

public class WriteFile {
    private String dosyaYolu;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy,HH:mm:ss");  
    
    public WriteFile(String dosyaYolu) {
        this.dosyaYolu = dosyaYolu;
        try {
            File myObj = new File("C:\\Users\\Deniz\\eclipse-workspace\\WebSocketApp\\src\\filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeLineDataAndTimestamp(String[] sensorDataLineArray) {
        LocalDateTime now = LocalDateTime.now();  
        System.out.println();  
        StringBuilder sensorDataLine = new StringBuilder(100);
        sensorDataLine.append(dtf.format(now));

        for(int i = 0; i < sensorDataLineArray.length; i++) 
            sensorDataLine.append(",").append(sensorDataLineArray[i]);
        

        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\Deniz\\OneDrive\\Belgeler\\GitHub\\agroautomated_cloned\\agroautomated\\backend_most_new\\WebSocketApp\\src\\filename.txt",true);
            myWriter.write(sensorDataLine.toString()+"\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
