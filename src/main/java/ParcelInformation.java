import com.google.gson.*;

import java.util.ArrayList;

public class ParcelInformation {
    private boolean completed;
    private String parcelName;
    private String invoiceNumber;
    private String receiverName;
    private String receiverAddress;
    private String senderName;
    private ArrayList<ParcelStatus> statusList = new ArrayList<>();

    public ParcelInformation(String jsonText) {
        JsonObject parcelObject = new JsonParser().parse(jsonText).getAsJsonObject();

        parcelName = parcelObject.get("itemName").getAsString();
        invoiceNumber = parcelObject.get("invoiceNo").getAsString();
        receiverName = parcelObject.get("receiverName").getAsString();
        receiverAddress = parcelObject.get("receiverAddr").getAsString();
        senderName = parcelObject.get("senderName").getAsString();

        completed = parcelObject.get("complete").getAsBoolean();
        parcelObject.get("trackingDetails").getAsJsonArray()
                    .forEach(statusElement -> statusList.add(new ParcelStatus(statusElement)));
    }

    @Override
    public String toString() {
        String result;
        String LF = "\n";

        StringBuilder statusString = new StringBuilder();
        statusList.forEach(status -> statusString.append(LF + LF + status.toString()));

        result = "============ Parcel Information ============" + LF + LF +
                 "Parcel Name        " + parcelName + LF +
                 "Invoice Number     " + invoiceNumber + LF +
                 "Sender Name        " + senderName + LF +
                 "Recipient Name     " + receiverName + LF +
                 "Recipient Address  " + receiverAddress + LF +
                 "Completed          " + (completed ? "Yes" : "No") + LF + LF +
                 "Shipping Status" + statusString + LF + LF +
                 "============================================";

        return result;
    }
}
