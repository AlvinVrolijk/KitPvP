package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends AbstractCommand {

    public SpawnCommand() {
        super("spawn", false);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) { // Check if there are enough arguments given
            ConfigFile configFile = new ConfigFile(KitPvP.kitPvP, false); // Get ConfigFile-object
            String spawnLocationInConfig = configFile.get().getString("spawn"); // Get string from config
            if (spawnLocationInConfig != null && !spawnLocationInConfig.equals("")) { // Check if spawn location isn't null and isn't ""
                Location spawnLocation = Serializiation.getLocationFromString(spawnLocationInConfig); // Get spawn location from config
                if (spawnLocation != null) { // Check if spawn location is valid
                    ((Player) sender).teleport(spawnLocation); // Teleport player to spawn
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Teleported to spawn")); // Inform player
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "No spawn set! Let a admin set a spawn with /setspawn")); // Inform player
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "No spawn set! Let a admin set a spawn with /setspawn")); // Inform player
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.tooMuchArguments)); // Inform player
        }
    }
}
