package nl.alvinvrolijk.kitpvp.listeners;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.data.KitPvpPlayer;
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

        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation().toCenterLocation()); // Teleport player to spawn
        e.getPlayer().getInventory().clear(); // Clear player's inventory

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
