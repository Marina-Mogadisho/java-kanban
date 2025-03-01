package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

import static util.UtilTime.*;
//Конвертер. Задаем свои правила конвертации объекта

public class DurationAdapter extends TypeAdapter<Duration> {
    //private static final DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern(format);

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) jsonWriter.value("");
        else jsonWriter.value(duration.toMinutes());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return stringOfDuration(jsonReader.nextString());
    }
}
