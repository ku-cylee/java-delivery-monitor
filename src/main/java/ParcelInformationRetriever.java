import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ParcelInformationRetriever {
    private String key;
    private String baseUrl = "http://info.sweettracker.co.kr/api/v1/";

    public ParcelInformationRetriever(String key) {
        this.key = key;
    }

    public HashMap<Integer, String> getCompanyMap() {
        HashMap<Integer, String> companyMap = new HashMap<>();

        try {
            URL url = new URL(baseUrl + "companylist?t_key=" + key);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bReader.readLine()) != null) response.append(inputLine);

            bReader.close();

            JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

            for (JsonElement companyElement:jsonObject.getAsJsonArray("Company")) {
                JsonObject companyObject = companyElement.getAsJsonObject();
                companyMap.put(companyObject.get("Code").getAsInt(), companyObject.get("Name").getAsString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return companyMap;
    }

    public ParcelInformation updateParcelInformation(String companyCode, String invoiceId) {
        try {
            URL url = new URL(baseUrl + "trackingInfo?t_key=" + key + "&t_code=" + companyCode + "&t_invoice=" + invoiceId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bReader.readLine()) != null) response.append(inputLine);

            bReader.close();
            return new ParcelInformation(response.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
