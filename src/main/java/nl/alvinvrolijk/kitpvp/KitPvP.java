package nl.alvinvrolijk.kitpvp;

import nl.alvinvrolijk.kitpvp.commands.AbstractCommand;
import nl.alvinvrolijk.kitpvp.data.MySQL;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;
import nl.alvinvrolijk.kitpvp.listeners.DeathRespawnListener;
import nl.alvinvrolijk.kitpvp.listeners.InteractionListener;
import nl.alvinvrolijk.kitpvp.listeners.InventoryListeners;
import nl.alvinvrolijk.kitpvp.listeners.JoinLeaveListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class KitPvP extends JavaPlugin {

    public static KitPvP kitPvP; // Instance

    public static Boolean ready = false; // Ready boolean

    public Logger logger = Logger.getLogger("KitPvP"); // Set console logger

    @Override
    public void onEnable() {
        // Plugin startup logic
        kitPvP = this; // Set instance

        new ConfigFile(this, true); // Get config file
        new MySQL().openConnection(); // Open database connection
        AbstractCommand.registerCommands(); // Register commands

        // Register listeners
        getServer().getPluginManager().registerEvents(new DeathRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListeners(), this);

        getServer().setSpawnRadius(0); // Set spawn radius to 0

        logger.info("Plugin enabled"); // Inform console that the plugin is enabled
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        

        // Save and kick all players
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer("Server reload, please rejoin!"); // Kick message
        }

        new MySQL().closeConnection(); // Close database connection

        kitPvP = null; // Set instance to null

        logger.info("Plugin disabled"); // Inform console that the plugin is disabled
    }
}
