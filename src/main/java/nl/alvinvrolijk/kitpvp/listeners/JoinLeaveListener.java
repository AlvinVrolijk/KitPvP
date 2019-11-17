package nl.alvinvrolijk.kitpvp.listeners;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.data.KitPvpPlayer;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    public JoinLeaveListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (!KitPvP.ready) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was a error"); // Disallow because of a error
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(""); // Turn join messages off

        ConfigFile configFile = new ConfigFile(KitPvP.kitPvP, false); // Get ConfigFile-object
        String spawnLocationInConfig = configFile.get().getString("spawn"); // Get string from config
        if (spawnLocationInConfig != null && !spawnLocationInConfig.equals("")) { // Check if spawn location isn't null and isn't ""
            Location spawnLocation = Serializiation.getLocationFromString(spawnLocationInConfig); // Get spawn location from config
            if (spawnLocation != null) { // Check if spawn location is valid
                e.getPlayer().teleport(spawnLocation); // Teleport player to spawn
            } else {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "No spawn set! Let a admin set a spawn with /setspawn")); // Inform player
            }
        } else {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "No spawn set! Let a admin set a spawn with /setspawn")); // Inform player
        }

        e.getPlayer().getInventory().clear(); // Clear player's inventory
        e.getPlayer().setHealth(e.getPlayer().getHealthScale()); // Set player's health to max (Heal the player)
        e.getPlayer().setFoodLevel(20); // Set player's food level to max (Feed the player)

        KitPvpPlayer.initializePlayerData(e.getPlayer()); // Initialize player
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(""); // Turn leave messages off

        // Check if KitPvpPlayer object exists for the player
        if (KitPvpPlayer.containsPlayer(e.getPlayer().getUniqueId())) { // Check if player is registered
            KitPvpPlayer.unregisterPlayer(KitPvpPlayer.getPlayer(e.getPlayer().getUniqueId())); // Unregister player
        }
    }
}
