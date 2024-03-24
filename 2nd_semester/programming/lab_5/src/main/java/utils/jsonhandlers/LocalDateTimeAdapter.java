package utils.jsonhandlers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value.format(dateTimeFormatter));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString(), dateTimeFormatter);
    }
}
