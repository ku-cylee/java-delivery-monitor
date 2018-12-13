package cose102.delivery_monitor.utils;

import com.google.gson.*;
import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ParcelRetriever {
    private String key;
    private String baseUrl = "http://info.sweettracker.co.kr/api/v1/";

    public ParcelRetriever(String key) {
        this.key = key;
    }

    public ArrayList<Company> getCompanyData() throws IOException {
        ArrayList<Company> companyList = new ArrayList<>();

        String response = getResponse(baseUrl + "companylist?t_key=" + key);

        new JsonParser().parse(response.toString()).getAsJsonObject().getAsJsonArray("Company")
                        .forEach(companyElement -> companyList.add(new Company(companyElement)));
        return companyList;
    }

    public ParcelInformation getParcelInformation(String companyCode, String invoiceId) throws IOException {
        String urlString = baseUrl + "trackingInfo?t_key=" + key + "&t_code=" + companyCode + "&t_invoice=" + invoiceId;
        String response = getResponse(urlString);
        return new ParcelInformation(response, companyCode);
    }

    private String getResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = bReader.readLine()) != null) response.append(inputLine);
        bReader.close();

        return response.toString();
    }
}
