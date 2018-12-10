package cose102.delivery_monitor.db_handler;

import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.models.ParcelStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class ParcelInformationDAO extends DAO {
    public ParcelInformationDAO() throws SQLException {
        super();
    }

    public void initialize() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS parcel_information (\n" +
                     "id INTEGER PRIMARY KEY,\n" +
                     "parcel_name VARCHAR(20),\n" +
                     "company_id INTEGER,\n" +
                     "invoice_number VARCHAR(20),\n" +
                     "receiver_name VARCHAR(20),\n" +
                     "receiver_address TEXT,\n" +
                     "sender_name VARCHAR(20),\n" +
                     "completed INTEGER\n" +
                     ");";

        connection.createStatement().execute(sql);
    }

    public ParcelInformation getParcelInformation(int parcelId) throws SQLException {
        String sql = "SELECT * FROM parcel_information WHERE id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        ResultSet rs = pstmt.executeQuery();

        ParcelInformation parcel = null;
        if (rs.next()) {
            Company company = new CompanyDAO().getCompany(rs.getString("company_id"));
            ArrayList<ParcelStatus> parcelStatusList = new ParcelStatusDAO().getParcelStatusList(parcelId);
            parcel = new ParcelInformation(rs, company, parcelStatusList);
        }

        return parcel;
    }

    public ArrayList<ParcelInformation> getAllParcelInformation() throws SQLException {
        String sql = "SELECT * FROM parcel_information";
        ArrayList<ParcelInformation> parcelList = new ArrayList<>();

        ResultSet rs = connection.createStatement().executeQuery(sql);

        while (rs.next()) {
            parcelList.add(new ParcelInformation(rs));
        }

        return parcelList;
    }

    public void insertParcelInformation(ParcelInformation parcel) throws SQLException {
        String sql = "INSERT INTO parcel_information " +
                "(parcel_name, company_id, invoice_number, receiver_name, " +
                "receiver_address, sender_name, completed)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, parcel.getParcelName());
        pstmt.setInt(2, parcel.getCompany().getId());
        pstmt.setString(3, parcel.getInvoiceNumber());
        pstmt.setString(4, parcel.getReceiverName());
        pstmt.setString(5, parcel.getReceiverAddress());
        pstmt.setString(6, parcel.getSenderName());
        pstmt.setInt(7, parcel.isCompleted() ? 1 : 0);
        pstmt.executeUpdate();

        ResultSet rs = connection.createStatement().executeQuery("SELECT id FROM parcel_information");

        ArrayList<Integer> parcelIdList = new ArrayList<>();
        while (rs.next()) parcelIdList.add(rs.getInt("id"));

        int maxId = Collections.max(parcelIdList);

        for (ParcelStatus status:parcel.getStatusList()) new ParcelStatusDAO().insertParcelStatus(maxId, status);
    }

    public void updateCompleteness(int parcelId, boolean completed) throws SQLException {
        String sql = "UPDATE parcel_status\n" +
                     "SET completed = ?\n" +
                     "WHERE parcel_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, completed ? 1 : 0);
        pstmt.setInt(2, parcelId);
        pstmt.executeUpdate();
    }
}
