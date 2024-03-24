package server.jsonhandlers;

import common.data.Vehicle;

import java.io.*;
import java.util.ArrayList;

public class JSONFileHandler {
    public static ArrayList<Vehicle> readVehiclesFromJSONFile(String filename) throws FileNotFoundException, IOException {
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
        return JSONParser.fromJSON(json);
    }

    public static void writeVehiclesToJSONFile(ArrayList<Vehicle> vehicles, String filename) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        String json = JSONParser.toJSON(vehicles);
        bufferedWriter.write(json, 0, json.length());
        bufferedWriter.flush();
        fileOutputStream.close();
        outputStreamWriter.close();
        bufferedWriter.close();
    }
}
