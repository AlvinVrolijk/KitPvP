package nl.alvinvrolijk.kitpvp.data;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Kit {

    private static HashMap<String, Kit> registry = new HashMap<>();

    private String name;
    private Material icon;
    private String items;
    private String armor;

    public Kit(String name, Material icon, String items, String armor) {
        this.name = name;
        this.icon = icon;
        this.items = items;
        this.armor = armor;
    }

    public String getItems() { return this.items; }

    public String getArmor() { return this.armor; }

    public void setItems(ItemStack[] items) {
        this.items = Serializiation.itemStackArrayToBase64(items);
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = Serializiation.itemStackArrayToBase64(armor);
    }

    public Material getIcon() { return this.icon; }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public static void registerKit(Kit kit) {
        registry.put(kit.name, kit);
    }

    public static void unregisterKit(Kit kit) {
        registry.remove(kit.name, kit);
    }

    // Update kits in MySQL
    public static void saveKits() {
        for (Map.Entry<String, Kit> entry : registry.entrySet()) {
            Kit kit = entry.getValue(); // Get Kit-object

            String playerSql = "UPDATE `kits` SET `icon`='" + kit.getIcon().toString() + "',`items`='" + kit.getItems() + "',`armor`='" + kit.getArmor() + "' WHERE name='" + kit.name + "';";
            PreparedStatement playerStmt = null;
            try {
                playerStmt = MySQL.connection.prepareStatement(playerSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (playerStmt != null) {
                    playerStmt.executeUpdate(); // Execute query
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initializeKits() {
        // Retrieve kits from MySQL
        String sql = "SELECT * FROM kits";
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
                    Kit.registerKit(new Kit(results.getString("name"), Material.getMaterial(results.getString("icon")), results.getString("items"), results.getString("armor"))); // Register a new Kit-object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add default kit
        if (!Kit.containsKit("None")) { // Check if Kit is existing
            Kit.createKit(new Kit("None", Material.BARRIER, "rO0ABXcEAAAAKXBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw", "rO0ABXcEAAAABHBwcHA=")); // Create new Kit-object
            KitPvP.kitPvP.logger.info("Default kit created!"); // Inform console
        }
    }

    public static void removeKit(Kit kit) {
        // Delete a kit from the database
        String sql = "DELETE FROM kits WHERE name='" + kit.name + "';";
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

        Kit.unregisterKit(kit);
    }

    public static void createKit(Kit kit) {
        // Insert a new kit in the database
        String playerSql = "INSERT INTO kits(name, icon, items, armor) VALUES (?, ?, ?, ?);";
        PreparedStatement playerStmt = null;
        try {
            playerStmt = MySQL.connection.prepareStatement(playerSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setString(1, kit.name); // Name of the kit
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setString(2, kit.icon.toString()); // Material of the icon
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setString(3, kit.items); // Inventory items
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (playerStmt != null) {
                playerStmt.setString(4, kit.armor); // Armor items
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

        // Register a new kit object
        Kit.registerKit(kit);
    }

    public static Kit getKit(String name) {
        if (registry.containsKey(name)) return registry.get(name);
        return null;
    }

    public static HashMap<String, Kit> getKits() {
        return registry;
    }

    public static Boolean containsKit(String name) {
        return registry.containsKey(name);
    }
}
