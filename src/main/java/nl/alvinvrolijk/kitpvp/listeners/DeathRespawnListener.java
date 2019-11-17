package nl.alvinvrolijk.kitpvp.listeners;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.data.KitPvpPlayer;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Scoreboard;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class DeathRespawnListener implements Listener {

    public DeathRespawnListener() {
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) { // Check whether the death reason is a player or not
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Messages.prefix + "&6" + e.getEntity().getDisplayName() + " &eis vermoord door &6" + e.getEntity().getKiller().getDisplayName())); // Send death message

            Objects.requireNonNull(KitPvpPlayer.getPlayer(e.getEntity().getKiller().getUniqueId())).setKills(Objects.requireNonNull(KitPvpPlayer.getPlayer(e.getEntity().getKiller().getUniqueId())).getKills() + 1); // Add a kill for the killer
            Scoreboard.updateScoreboard(Objects.requireNonNull(KitPvpPlayer.getPlayer(e.getEntity().getKiller().getUniqueId()))); // Update killer's scoreboard
        } else {
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Messages.prefix + "&6" + e.getEntity().getDisplayName() + " &eis doodgegaan")); // Send death message
        }

        Objects.requireNonNull(KitPvpPlayer.getPlayer(e.getEntity().getUniqueId())).setDeaths(Objects.requireNonNull(KitPvpPlayer.getPlayer(e.getEntity().getUniqueId())).getDeaths() + 1); // Add a death for the death player
        Scoreboard.updateScoreboard(Objects.requireNonNull(KitPvpPlayer.getPlayer(e.getEntity().getUniqueId()))); // Update death player scoreboard

        e.setDroppedExp(0); // Set dropped exp to 0
        e.setKeepInventory(true); // Keep inventory, so that there won't be any item drops

        e.getEntity().spigot().respawn(); // Skip death screen
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.getPlayer().getInventory().clear(); // Clear inventory

        Bukkit.getScheduler().scheduleSyncDelayedTask(KitPvP.kitPvP, () -> {
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
        }, 5L);
    }
}
