import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormat {
    public static String Format(String time){

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(time, inputFormat);

        DateTimeFormatter croatianFormat = DateTimeFormatter.ofPattern("d. MMMM yyyy. HH:mm", new Locale("hr", "HR"));
        String formatted = dateTime.format(croatianFormat);

        return formatted;
    }
}
