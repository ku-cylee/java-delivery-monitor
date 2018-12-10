package cose102.delivery_monitor.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Company {
    private int id;
    private String companyCode;
    private String companyName;

    public Company(ResultSet resultSet) {
        try {
            id = resultSet.getInt("id");
            companyCode = resultSet.getString("code");
            companyName = resultSet.getString("company");
        } catch (SQLException e) {
            // No expected exception
        }
    }

    public Company(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        id = 0;
        companyCode = jsonObject.get("Code").getAsString();
        companyName = jsonObject.get("Name").getAsString();
    }

    public int getId() {
        return id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }
}
