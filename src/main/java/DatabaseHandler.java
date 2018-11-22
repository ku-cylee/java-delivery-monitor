import java.io.File;
import java.nio.file.*;
import java.sql.*;

public class DatabaseHandler {
    private Connection connection = null;
    private Statement statement = null;

    public DatabaseHandler() {
        Path dbFolder;

        if (System.getProperty("os.name").contains("Windows")) {
            dbFolder = Paths.get(System.getenv("APPDATA"), "delivery_monitor");
        } else {
            dbFolder = Paths.get("/usr/share", "delivery_monitor");
        }

        new File(dbFolder.toString()).mkdirs();
    }
}
