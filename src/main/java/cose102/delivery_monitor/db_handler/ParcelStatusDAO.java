package cose102.delivery_monitor.db_handler;

import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.models.ParcelStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParcelStatusDAO extends DAO {
    public ParcelStatusDAO() throws SQLException {
        super();
    }

    public void initialize() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS parcel_status (\n" +
                     "id INTEGER PRIMARY KEY,\n" +
                     "parcel_id INTEGER,\n" +
                     "status_time VARCHAR(20),\n" +
                     "location VARCHAR(25),\n" +
                     "category VARCHAR(15)\n" +
                     ")";

        connection.createStatement().execute(sql);
    }

    public ArrayList<ParcelStatus> getParcelStatusList(int parcelId) throws SQLException {
        ArrayList<ParcelStatus> statusList = new ArrayList<>();

        String sql = "SELECT * FROM parcel_status WHERE parcel_id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) statusList.add(new ParcelStatus(rs));

        return statusList;
    }

    public void insertParcelStatus(int parcelId, ParcelStatus status) throws SQLException {
        String sql = "INSERT INTO parcel_status (parcel_id, status_time, location, category)\n" +
                     "VALUES (?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        pstmt.setString(2, status.getTimeString());
        pstmt.setString(3, status.getLocation());
        pstmt.setString(4, status.getCategory());
        pstmt.executeUpdate();
    }

    public void updateParcelStatus(int parcelId, ParcelInformation newParcel) throws SQLException {
        ParcelInformation originalParcel = new ParcelInformationDAO().getParcelInformation(parcelId);

        boolean areSizesSame = false;

        if (originalParcel == null) {
            // such parcel does not exist
        } else {
            areSizesSame = originalParcel.getStatusList().size() == newParcel.getStatusList().size();
        }

        if (areSizesSame || !originalParcel.isCompleted()) {
            // no update
        } else {
            for (int idx = originalParcel.getStatusList().size(); idx < newParcel.getStatusList().size(); idx++) {
                ParcelStatus status = newParcel.getStatusList().get(idx);
                insertParcelStatus(parcelId, status);
            }

            if (newParcel.isCompleted()) new ParcelInformationDAO().updateCompleteness(parcelId, true);
        }
    }
}
