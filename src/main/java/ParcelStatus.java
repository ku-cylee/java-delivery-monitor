import com.google.gson.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParcelStatus {
    private Date statusTime;
    private String location;
    private String category;

    public ParcelStatus(JsonElement statusElement) {
        JsonObject statusObject = statusElement.getAsJsonObject();
        try {
            String timeString = statusObject.get("timeString").getAsString();
            statusTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(timeString);
        } catch (Exception e) {
            // No chance of exception
            statusTime = new Date(1970, 1, 1);
        }
        location = statusObject.get("where").getAsString();
        category = statusObject.get("kind").getAsString();
    }

    @Override
    public String toString() {
        String LF = "\n";
        String result = "## " + new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(statusTime) + LF;
        result += String.format("## [%s] %s", category, location);

        return result;
    }
}
