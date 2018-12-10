package cose102.delivery_monitor.utils;

import com.google.gson.*;
import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ParcelRetriever {
    private String key;
    private String baseUrl = "http://info.sweettracker.co.kr/api/v1/";

    public ParcelRetriever(String key) {
        this.key = key;
    }

    public ArrayList<Company> getCompanyData() throws IOException {
        ArrayList<Company> companyList = new ArrayList<>();

        URL url = new URL(baseUrl + "companylist?t_key=" + key);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = bReader.readLine()) != null) response.append(inputLine);
        bReader.close();

        JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
        new JsonParser().parse(response.toString()).getAsJsonObject().getAsJsonArray("Company")
                        .forEach(companyElement -> companyList.add(new Company(companyElement)));
        return companyList;
    }

    public ParcelInformation getParcelInformation(String companyCode, String invoiceId) throws IOException {
        URL url = new URL(baseUrl + "trackingInfo?t_key=" + key + "&t_code=" + companyCode + "&t_invoice=" + invoiceId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = bReader.readLine()) != null) response.append(inputLine);

        bReader.close();
        return new ParcelInformation(response.toString());
        // how to get company object when generating parcel info?
    }
}
