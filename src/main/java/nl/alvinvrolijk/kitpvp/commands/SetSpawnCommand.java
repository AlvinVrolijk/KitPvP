package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.files.ConfigFile;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SetSpawnCommand extends AbstractCommand {

    public SetSpawnCommand() {
        super("setspawn", false);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) { // Check if there are enough arguments given
            Location spawnLocation = ((Player) sender).getLocation(); // Get sender's location a.k.a. the new spawn location
            ConfigFile configFile = new ConfigFile(KitPvP.kitPvP, false); // Get ConfigFile-object
            configFile.get().set("spawn", Serializiation.getStringFromLocation(spawnLocation)); // Set spawn location in config
            configFile.save(); // Save the new config
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6New spawn location set"));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.tooMuchArguments)); // Inform player
        }
    }
}
