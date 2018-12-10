package cose102.delivery_monitor.db_handler;

import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.models.ParcelStatus;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DAO {
    public Connection connection = null;

    public DAO() throws SQLException {
        Path dbFolder;

        if (System.getProperty("os.name").contains("Windows")) {
            dbFolder = Paths.get(System.getenv("APPDATA"), "cose102/delivery_monitor");
        } else {
            dbFolder = Paths.get("/usr/share", "cose102/delivery_monitor");
        }

        new File(dbFolder.toString()).mkdirs();
        Path dbPath = Paths.get(dbFolder.toString(), "db.sqlite3");

        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString());
    }
}
