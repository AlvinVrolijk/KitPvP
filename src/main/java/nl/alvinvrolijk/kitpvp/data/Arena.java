package nl.alvinvrolijk.kitpvp.data;

import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Arena {

    private static HashMap<String, Arena> registry = new HashMap<>();

    private String name;
    private Location center;
    private Integer radius;

    public Arena(String name, Location center, Integer radius) {
        this.name = name;
        this.center = center;
        this.radius = radius;
    }

    public Location getCenter() { return this.center; }

    public Integer getRadius() { return this.radius; }

    public static void registerArena(Arena arena) {
        registry.put(arena.name, arena);
    }

    public static void unregisterArena(Arena arena) {
        registry.remove(arena.name, arena);
    }

    // Retrieve arenas from MySQL
    public static void initializeArenas() {
        String sql = "SELECT * FROM arenas";
        PreparedStatement stmt = null;
        try {
            stmt = MySQL.connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet results = null;
        try {
            if (stmt != null) {
                results = stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (results != null) {
                while (results.next()) {
                    Arena.registerArena(new Arena(results.getString("name"), Serializiation.getLocationFromString(results.getString("center")), results.getInt("radius"))); // Register a new Arena-object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeArena(Arena arena) {
        // Delete arena from the database
        String sql = "DELETE FROM arenas WHERE name='" + arena.name + "';";
        PreparedStatement stmt = null;
        try {
            stmt = MySQL.connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Arena.unregisterArena(arena);
    }

    public static void createArena(Arena arena) {
        // Insert a new arena in the database
        String playerSql = "INSERT INTO arenas(name, center, radius) VALUES (?, ?, ?);";
        PreparedStatement playerStmt = null;
        try {
            playerStmt = MySQL.connection.prepareStatement(playerSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setString(1, arena.name); // Name of the arena
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setString(2, Serializiation.getStringFromLocation(arena.center)); // Center of the arena
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setInt(3, arena.radius); // Radius for the spawn system in this arena
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Register a new arena object
        Arena.registerArena(arena);
    }

    public static Arena getArena(String name) {
        if (registry.containsKey(name)) return registry.get(name);
        return null;
    }

    public static HashMap<String, Arena> getArenas() {
        return registry;
    }

    public static Boolean containsArena(String name) {
        return registry.containsKey(name);
    }
}
