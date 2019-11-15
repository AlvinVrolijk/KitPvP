package nl.alvinvrolijk.kitpvp.listeners;

import nl.alvinvrolijk.kitpvp.data.KitPvpPlayer;
import nl.alvinvrolijk.kitpvp.data.MySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class JoinLeaveListener implements Listener {

    public JoinLeaveListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        try {
            if (MySQL.getDatabase().getCurrentConnection().isClosed()) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was a database error"); // Disallow because of closed connection
            }
        } catch (SQLException e1) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was a database error"); // Disallow because of closed connection
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(""); // Turn join messages off

        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation().toCenterLocation()); // Teleport player to spawn

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
