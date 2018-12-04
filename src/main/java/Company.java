import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Company {
    public int id;
    public String companyCode;
    public String companyName;

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
}
