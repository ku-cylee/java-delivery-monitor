package cose102.delivery_monitor.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cose102.delivery_monitor.utils.Shortcuts;

import java.sql.ResultSet;
import java.util.Date;

public class ParcelStatus {
    private Date statusTime;
    private String location;
    private String category;

    public ParcelStatus(JsonElement statusElement) {
        JsonObject statusObject = statusElement.getAsJsonObject();
        String timeString = "";
        try {
            timeString = statusObject.get("timeString").getAsString();
        } catch (Exception e) { }
        statusTime = Shortcuts.stringToDateTime(timeString);
        location = statusObject.get("where").getAsString();
        category = statusObject.get("kind").getAsString();
    }

    public ParcelStatus(ResultSet resultSet) {
        try {
            statusTime = Shortcuts.stringToDateTime(resultSet.getString("status_time"));
            location = resultSet.getString("location");
            category = resultSet.getString("category");
        } catch (Exception e) {
            // exception
        }
    }

    @Override
    public String toString() {
        String LF = "\n";
        String result = "## " + Shortcuts.dateTimeToString(statusTime) + LF;
        result += String.format("## [%s] %s", category, location);

        return result;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }
}
