package util;

import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilTime {
    public static String format = "HH:mm dd.MM.yyyy";

    public static LocalDateTime stringOfLocalTime(String time){

        if (time == null || time.length() == 0) return null;
        else if (time.equals("0")) return null;;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return  LocalDateTime.parse(time, formatter);
        } catch (Exception e) {
            return null;
        }

    }
    public static Duration stringOfDuration(String duration){
        if (duration == null || duration.length() == 0) return  null;
        else if (duration.equals("0")) return null;
        else {
            try {
                return Duration.ofMinutes(Long.parseLong(duration));
            } catch (Exception e) {
                return null;
            }
        }
    }
}
