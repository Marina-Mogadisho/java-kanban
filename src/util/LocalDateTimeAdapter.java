package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static util.UtilTime.format;
//Конвертер. Задаем свои правила конвертации объекта

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern(format);

    @Override
    public void write(final JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) jsonWriter.value(""); // метод принимает формат, который должен появится в json
        else
            // метод принимает формат, который должен появится в json
            jsonWriter.value(localDateTime.format(dtformatter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String str = jsonReader.nextString();
        if (str.isEmpty()) return null;
        return LocalDateTime.parse(str, dtformatter);
    }
}
