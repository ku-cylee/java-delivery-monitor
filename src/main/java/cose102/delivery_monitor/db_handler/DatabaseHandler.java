package cose102.delivery_monitor.db_handler;

import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.models.ParcelStatus;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseHandler {
    private Connection connection = null;
    private Statement statement = null;

    public DatabaseHandler(String dbFileName) throws SQLException {
        Path dbFolder;

        if (System.getProperty("os.name").contains("Windows")) {
            dbFolder = Paths.get(System.getenv("APPDATA"), "cose102/delivery_monitor");
        } else {
            dbFolder = Paths.get("/usr/share", "cose102/delivery_monitor");
        }

        new File(dbFolder.toString()).mkdirs();
        Path dbPath = Paths.get(dbFolder.toString(), "db.sqlite3");

        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString());
        statement = connection.createStatement();

        initializeDatabase();
    }

    public void initializeDatabase() throws SQLException {
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
                               "completed INTEGER\n" +
                               ");";

        String parcelStatusSql = "CREATE TABLE IF NOT EXISTS parcel_status (\n" +
                                 "id INTEGER PRIMARY KEY,\n" +
                                 "parcel_id INTEGER,\n" +
                                 "status_time VARCHAR(20),\n" +
                                 "location VARCHAR(25),\n" +
                                 "category VARCHAR(15)\n" +
                                 ")";

        statement.execute(companyListsSql);
        statement.execute(parcelInfoSql);
        statement.execute(parcelStatusSql);
    }

    public void insertCompanyLists(ArrayList<Company> companyList) throws SQLException {
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

        String sql = "SELECT * FROM company WHERE company_id = ?";
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

        ResultSet rs = statement.executeQuery("SELECT * FROM company");
        while (rs.next()) companyList.add(new Company(rs));

        return companyList;
    }

    public ParcelInformation getParcelInformation(int parcelId) throws SQLException {
        String sql = "SELECT * FROM parcel_information WHERE id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, parcelId);
        ResultSet rs = pstmt.executeQuery();

        ParcelInformation parcel = null;
        if (rs.next()) {
            Company company = getCompany(rs.getString("company_id"));
            ArrayList<ParcelStatus> parcelStatusList = getParcelStatusList(parcelId);
            parcel = new ParcelInformation(rs, company, parcelStatusList);
        }

        return parcel;
    }

    public ArrayList<ParcelInformation> getAllParcelInformation() throws SQLException {
        String sql = "SELECT * FROM parcel_information";
        ArrayList<ParcelInformation> parcelList = new ArrayList<>();

        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            parcelList.add(new ParcelInformation(rs));
        }

        return parcelList;
    }

    private ArrayList<ParcelStatus> getParcelStatusList(int parcelId) throws SQLException {
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

        ResultSet rs = statement.executeQuery("SELECT id FROM parcel_information");

        ArrayList<Integer> parcelIdList = new ArrayList<>();
        while (rs.next()) parcelIdList.add(rs.getInt("id"));

        int maxId = Collections.max(parcelIdList);

        for (ParcelStatus status:parcel.getStatusList()) insertParcelStatus(maxId, status);
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
        ParcelInformation originalParcel = getParcelInformation(parcelId);

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

            if (newParcel.isCompleted()) {
                String sql = "UPDATE parcel_status\n" +
                             "SET completed = 1\n" +
                             "WHERE parcel_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, parcelId);
                pstmt.executeUpdate();
            }
        }
    }
}
