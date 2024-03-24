package utils.jsonhandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import data.Vehicle;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class JSONParser {
    private static Gson gson = getGSONBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    private static GsonBuilder getGSONBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder;
    }

    public static String toJSON(ArrayList<Vehicle> vehicles) {
        return gson.toJson(vehicles);
    }

    public static ArrayList<Vehicle> fromJSON(String json) throws JsonSyntaxException {
        Type type = new TypeToken<ArrayList<Vehicle>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
