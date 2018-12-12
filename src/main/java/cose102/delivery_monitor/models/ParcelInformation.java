package cose102.delivery_monitor.models;

import com.google.gson.*;
import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.utils.Shortcuts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ParcelInformation {
    private boolean completed;
    private String parcelName;
    private String invoiceNumber;
    private String receiverName;
    private String receiverAddress;
    private String senderName;
    private ArrayList<ParcelStatus> statusList = new ArrayList<>();
    private Company company;
    private Date createdAt;

    public ParcelInformation(String jsonText, String companyCode) {
        JsonObject parcelObject = new JsonParser().parse(jsonText).getAsJsonObject();

        parcelName = parcelObject.get("itemName").getAsString();
        invoiceNumber = parcelObject.get("invoiceNo").getAsString();
        receiverName = parcelObject.get("receiverName").getAsString();
        receiverAddress = parcelObject.get("receiverAddr").getAsString();
        senderName = parcelObject.get("senderName").getAsString();

        completed = parcelObject.get("complete").getAsBoolean();
        parcelObject.get("trackingDetails").getAsJsonArray()
                    .forEach(statusElement -> statusList.add(new ParcelStatus(statusElement)));

        try {
            company = DatabaseHandler.getInstance().getCompany(companyCode);
        } catch (SQLException e) {
            // no expected exception
        }

        createdAt = new Date();
    }

    public ParcelInformation(ResultSet resultSet) {
        try {
            parcelName = resultSet.getString("parcel_name");
            invoiceNumber = resultSet.getString("invoice_number");
            receiverName = resultSet.getString("receiver_name");
            receiverAddress = resultSet.getString("receiver_address");
            senderName = resultSet.getString("sender_name");
            completed = (resultSet.getInt("completed") != 0);
            createdAt = Shortcuts.stringToDateTime(resultSet.getString("created_at"));

            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            company = dbHandler.getCompany(resultSet.getInt("company_id"));
            statusList = dbHandler.getParcelStatusList(resultSet.getInt("id"));
        } catch (SQLException e) {
            // no expected exception
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getParcelName() {
        return parcelName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getSenderName() {
        return senderName;
    }

    public Company getCompany() {
        return company;
    }

    public ArrayList<ParcelStatus> getStatusList() {
        return statusList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
