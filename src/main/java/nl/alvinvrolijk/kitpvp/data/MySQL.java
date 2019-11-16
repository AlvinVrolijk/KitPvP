package nl.alvinvrolijk.kitpvp.data;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static MySQL database = new MySQL();
    static Connection connection;

    // Instance of KitPvP
    private KitPvP instance = KitPvP.kitPvP;

    // Instance of ConfigFile
    private ConfigFile configFile = new ConfigFile(instance, false);

    // Open/start connection method
    public synchronized void openConnection() {
        if (!configFile.get().contains("database.host")) {
            instance.logger.warning("Specify the database credentials in the config.yml!");
        }

        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            // Try establishing a connection
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                                configFile.get().getString("database.host") + ":" +
                                configFile.get().get("database.port") + "/" +
                                configFile.get().getString("database.database") + "?autoReconnect=false",
                        configFile.get().getString("database.username"),
                        configFile.get().getString("database.password"));

                if (!checkTable()) throw new SQLException();

                if (getCurrentConnection() != null && !getCurrentConnection().isClosed()) {
                    instance.logger.info("Database connection successful"); // Send success message
                    KitPvP.ready = true;
                    Kit.initializeKits(); // Initialize kits
                    Arena.initializeArenas(); // Initialize arena's
                }

            } catch (Exception exception) {
                // Send error message to console
                instance.logger.severe("Database connection error");
                // Print stack trace
                exception.printStackTrace();
            }
        });
    }

    // Close connection method
    public void closeConnection() {
        try {
            // Check whether the connection is already closed or not
            if ((!connection.isClosed()) || (connection != null)) {
                // Close connection
                connection.close();
                KitPvP.ready = false;
            }
        } catch (SQLException exception) {
            // Print stack trace
            exception.printStackTrace();
        }
    }

    private synchronized boolean checkTable() {
        try {
            if (getCurrentConnection() != null && !getCurrentConnection().isClosed()) {
                // Create new tables if they do not exist

                // Table details: id, uuid, kills, deaths
                getCurrentConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS `stats` (" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                        "  `uuid` varchar(36) DEFAULT NULL," +
                        "  `kills` int DEFAULT NULL," +
                        "  `deaths` int DEFAULT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

                // Table details: id, name, spawnLocations
                getCurrentConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS `arenas` (" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                        "  `name` tinytext DEFAULT NULL," +
                        "  `center` mediumtext DEFAULT NULL," +
                        "  `radius` int DEFAULT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

                // Table details: id, name, icon, items, armor
                getCurrentConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS `kits` (" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                        "  `name` tinytext DEFAULT NULL," +
                        "  `icon` tinytext DEFAULT NULL," +
                        "  `items` mediumtext DEFAULT NULL," +
                        "  `armor` mediumtext DEFAULT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            }
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Connection getCurrentConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + configFile.get().getString("database.host") + ":" + configFile.get().get("database.port") + "/" + configFile.get().getString("database.database") + "?autoReconnect=false", configFile.get().getString("database.username"), configFile.get().getString("database.password"));
        } catch (Exception exception) {
            // Print stack trace
            exception.printStackTrace();
        }
        // Return current connection
        return connection;
    }

    // Get instance of MySQL
    public static MySQL getDatabase() {
        return database;
    }
}
