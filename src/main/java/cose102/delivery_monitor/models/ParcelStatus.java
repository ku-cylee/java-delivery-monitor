package cose102.delivery_monitor.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public ParcelStatus(ResultSet resultSet) throws SQLException {
        try {
            String timeString = resultSet.getString("status_time");
            statusTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(timeString);
            location = resultSet.getString("location");
            category = resultSet.getString("category");
        } catch (Exception e) {
            // exception
        }
    }

    @Override
    public String toString() {
        String LF = "\n";
        String result = "## " + getTimeString() + LF;
        result += String.format("## [%s] %s", category, location);

        return result;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public String getTimeString() {
        return new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(statusTime);
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }
}
