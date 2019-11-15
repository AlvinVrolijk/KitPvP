package nl.alvinvrolijk.kitpvp.data;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private Connection connection;
    private KitPvP kitPvP;

    // Instance of ConfigFile
    private ConfigFile configFile = new ConfigFile(kitPvP, false);

    // Dependency Injection
    public Database(KitPvP kitPvP) {
        this.kitPvP = kitPvP;
    }

    // Open a new connection, and return that connection
    public synchronized Connection openConnection() {
        // Attempting to connect
        kitPvP.logger.info("Attempting to connect to the database");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Driver not available
            kitPvP.logger.warning("jdbc driver unavailable!");
            kitPvP.logger.warning("Database connection failed: jdbc driver unavailable");
        }
        try {
            // Variables
            String hostname = configFile.get().getString("database.hostname");
            Integer port = configFile.get().getInt("database.port");
            String username = configFile.get().getString("database.username");
            String password = configFile.get().getString("database.password");
            String database = configFile.get().getString("database.database");
            String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;

            // Open the connection
            this.connection = DriverManager.getConnection(url, username, password);

            // Create tables if not exists
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS stats(uuid varchar(36), kills int, deaths int)");
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS kits(name text(12), inventory varchar(36000), armor varchar(36000))");

            // Inform the console
            kitPvP.logger.info("Database connection successful");

            // Send keep alive
            keepAlive();

            // Return the connection
            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
            // Inform the console
            kitPvP.logger.warning("Database connection failed");
        }
        return null;
    }

    // Close connection if it's opened
    public synchronized void closeConnection() {
        Bukkit.getScheduler().runTaskAsynchronously(kitPvP, () -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    kitPvP.logger.info("Database connection successfully closed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                kitPvP.logger.warning("Database connection close failed");
            }
        });
    }

    // Send a keep alive request
    private synchronized void keepAlive() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(kitPvP, () -> {
            String blockSql = "SELECT * FROM stats";
            PreparedStatement keepAliveStmt = null;
            try {
                keepAliveStmt = connection.prepareStatement(blockSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (keepAliveStmt != null) {
                    keepAliveStmt.executeQuery();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0L, 36000L);
    }
}
