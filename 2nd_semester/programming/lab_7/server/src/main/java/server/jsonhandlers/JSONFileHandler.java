package server.jsonhandlers;

import common.data.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class JSONFileHandler {
    public static CopyOnWriteArrayList<Vehicle> readVehiclesFromJSONFile(String filename) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytebuffer = new byte[1024];
        int bytesRead = bufferedInputStream.read(bytebuffer);
        while (bytesRead != -1) {
            stringBuilder.append(new String(bytebuffer, 0, bytesRead));
            bytesRead = bufferedInputStream.read(bytebuffer);
        }
        fileInputStream.close();
        bufferedInputStream.close();
        String json = stringBuilder.toString();
        return JSONParser.vehiclesFromJSON(json);
    }

    public static void writeVehiclesToJSONFile(CopyOnWriteArrayList<Vehicle> vehicles, String filename) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        String json = JSONParser.vehiclesToJSON(vehicles);
        bufferedWriter.write(json, 0, json.length());
        bufferedWriter.flush();
        fileOutputStream.close();
        outputStreamWriter.close();
        bufferedWriter.close();
    }
}
