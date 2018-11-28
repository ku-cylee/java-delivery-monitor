import java.io.File;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement pStatement = null;

    public DatabaseHandler(String dbFileName) {
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
            statement = connection.createStatement();

            initializeDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void initializeDatabase() {
        String companyListsSql = "CREATE TABLE IF NOT EXISTS company_lists (\n" +
                     "id INTEGER PRIMARY KEY,\n" +
                     "code VARCHAR(5),\n" +
                     "company VARCHAR(16)\n" +
                     ");";

        String parcelInfoSql = "CREATE TABLE IF NOT EXISTS pacel_information (\n" +
                               "id INTEGER PRIMARY KEY,\n" +
                               "parcel_name VARCHAR(20),\n" +
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

        try {
            statement.execute(companyListsSql);
            statement.execute(parcelInfoSql);
            statement.execute(parcelStatusSql);
        } catch (SQLException e) {
            // No expected exception
        }
    }

    public ArrayList<ParcelInformation> getAllParcelInformation() {
        String sql = "SELECT * FROM parcel_information";
        ArrayList<ParcelInformation> parcelList = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                parcelList.add(new ParcelInformation(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return parcelList;
    }

    public ParcelInformation getSpecificParcelInformation(int parcelId) {
        String sql = "SELECT * FROM parcel_information WHERE id = ?";

        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, parcelId);
            ResultSet resultSet = pStatement.executeQuery();

            if (resultSet.next()) {
                return new ParcelInformation(resultSet, getParcelStatusList(parcelId));
            } else {
                // no such id exists
            }
        } catch (SQLException e) {
            // exception
        }
    }

    private ArrayList<ParcelStatus> getParcelStatusList(int parcelId) {
        ArrayList<ParcelStatus> statusList = new ArrayList<>();

        String sql = "SELECT * FROM parcel_status WHERE parcel_id = ?";
        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, parcelId);
            ResultSet resultSet = pStatement.executeQuery();

            while(resultSet.next()) statusList.add(new ParcelStatus(resultSet));
        } catch (SQLException e) {
            // exception
        }

        return statusList;
    }
}
