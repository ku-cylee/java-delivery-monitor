package cose102.delivery_monitor.db_handler;

import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.models.ParcelStatus;
import cose102.delivery_monitor.utils.Shortcuts;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseHandler {
    private Connection connection = null;

    public static DatabaseHandler getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        static final DatabaseHandler INSTANCE = new DatabaseHandler();
    }

    private DatabaseHandler() {
        Path dbFolder;

        if (System.getProperty("os.name").contains("Windows")) {
            dbFolder = Paths.get(System.getenv("APPDATA"), "delivery_monitor");
        } else {
            dbFolder = Paths.get("/usr/share", "delivery_monitor");
        }

        new File(dbFolder.toString()).mkdirs();
        Path dbPath = Paths.get(dbFolder.toString(), "db.sqlite3");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString());
        } catch (SQLException e) { }
    }

    public void initialize() throws SQLException {
        String companyListsSql = "CREATE TABLE IF NOT EXISTS company (\n" +
                                 "id INTEGER PRIMARY KEY,\n" +
                                 "code VARCHAR(5),\n" +
                                 "company VARCHAR(16)\n" +
                                 ");";

        String parcelInfoSql = "CREATE TABLE IF NOT EXISTS parcel_information (\n" +
                               "id INTEGER PRIMARY KEY,\n" +
                               "parcel_name VARCHAR(20),\n" +
                               "company_id INTEGER,\n" +
                               "invoice_number VARCHAR(20),\n" +
                               "receiver_name VARCHAR(20),\n" +
                               "receiver_address TEXT,\n" +
                               "sender_name VARCHAR(20),\n" +
                               "completed INTEGER,\n" +
                               "is_active INTEGER DEFAULT 1,\n" +
                               "created_at VARCHAR(20)\n" +
                               ");";

        String parcelStatusSql = "CREATE TABLE IF NOT EXISTS parcel_status (\n" +
                                 "id INTEGER PRIMARY KEY,\n" +
                                 "parcel_id INTEGER,\n" +
                                 "status_time VARCHAR(20),\n" +
                                 "location VARCHAR(25),\n" +
                                 "category VARCHAR(15)\n" +
                                 ")";

        Statement statement = connection.createStatement();
        statement.execute(companyListsSql);
        statement.execute(parcelInfoSql);
        statement.execute(parcelStatusSql);
    }

    public void refreshCompanyLists(ArrayList<Company> companyList) throws SQLException {
        connection.createStatement().execute("DELETE FROM company");

        String sql = "INSERT INTO company (code, company) VALUES (?, ?)";

        for (Company company:companyList) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1 ,company.getCompanyCode());
            pstmt.setString(2, company.getCompanyName());
            pstmt.executeUpdate();
        }
    }

    public Company getCompany(int companyId) throws SQLException {
        Company company = null;

        String sql = "SELECT * FROM company WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, companyId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) company = new Company(rs);

        return company;
    }

    public Company getCompany(String companyCode) throws SQLException {
        Company company = null;

        String sql = "SELECT * FROM company WHERE code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, companyCode);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) company = new Company(rs);

        return company;
    }

    public ArrayList<Company> getCompanyList() throws SQLException {
        ArrayList<Company> companyList = new ArrayList<>();

        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM company");
        while (rs.next()) companyList.add(new Company(rs));

        return companyList;
    }

    public ParcelInformation getParcelInformation(int parcelId) throws SQLException {
        String sql = "SELECT * FROM parcel_information WHERE id = ? AND is_active = 1";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        ResultSet rs = pstmt.executeQuery();

        ParcelInformation parcel = null;
        if (rs.next()) {
            Company company = getCompany(rs.getInt("company_id"));
            ArrayList<ParcelStatus> parcelStatusList = getParcelStatusList(parcelId);
            parcel = new ParcelInformation(rs);
        }

        return parcel;
    }

    public ArrayList<ParcelInformation> getActiveParcels() throws SQLException {
        String sql = "SELECT * FROM parcel_information WHERE is_active = 1";
        ArrayList<ParcelInformation> parcelList = new ArrayList<>();

        ResultSet rs = connection.createStatement().executeQuery(sql);

        while (rs.next()) parcelList.add(new ParcelInformation(rs));
        return parcelList;
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

    public void insertParcelInformation(ParcelInformation parcel) throws SQLException {
        String sql = "INSERT INTO parcel_information " +
                     "(parcel_name, company_id, invoice_number, receiver_name, " +
                     "receiver_address, sender_name, completed, created_at)\n" +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, parcel.getParcelName());
        pstmt.setInt(2, parcel.getCompany().getId());
        pstmt.setString(3, parcel.getInvoiceNumber());
        pstmt.setString(4, parcel.getReceiverName());
        pstmt.setString(5, parcel.getReceiverAddress());
        pstmt.setString(6, parcel.getSenderName());
        pstmt.setInt(7, parcel.isCompleted() ? 1 : 0);
        pstmt.setString(8, Shortcuts.dateTimeToString(parcel.getCreatedAt()));
        pstmt.executeUpdate();

        ResultSet rs = connection.createStatement().executeQuery("SELECT id FROM parcel_information");

        ArrayList<Integer> parcelIdList = new ArrayList<>();
        while (rs.next()) parcelIdList.add(rs.getInt("id"));

        int maxId = Collections.max(parcelIdList);

        for (ParcelStatus status:parcel.getStatusList()) insertParcelStatus(maxId, status);
    }

    public void disableParcel(int parcelId) throws SQLException {
        String sql = "UPDATE parcel_information SET is_active = 0 WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        pstmt.executeUpdate();
    }

    public void insertParcelStatus(int parcelId, ParcelStatus status) throws SQLException {
        String sql = "INSERT INTO parcel_status (parcel_id, status_time, location, category)\n" +
                     "VALUES (?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        pstmt.setString(2, Shortcuts.dateTimeToString(status.getStatusTime()));
        pstmt.setString(3, status.getLocation());
        pstmt.setString(4, status.getCategory());
        pstmt.executeUpdate();
    }

    public void updateParcelStatus(int parcelId, ParcelInformation newParcel) throws SQLException {
        ParcelInformation originalParcel = getParcelInformation(parcelId);

        boolean areSizesSame = false;

        if (originalParcel == null) {
            // such parcel does not exist
        } else {
            areSizesSame = originalParcel.getStatusList().size() == newParcel.getStatusList().size();
        }

        if (areSizesSame || originalParcel.isCompleted()) {
            // no update
        } else {

            for (int idx = originalParcel.getStatusList().size(); idx < newParcel.getStatusList().size(); idx++) {
                ParcelStatus status = newParcel.getStatusList().get(idx);
                insertParcelStatus(parcelId, status);
            }

            if (newParcel.isCompleted()) {
                String sql = "UPDATE parcel_information SET completed = 1 WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, parcelId);
                pstmt.executeUpdate();
            }
        }
    }
}
