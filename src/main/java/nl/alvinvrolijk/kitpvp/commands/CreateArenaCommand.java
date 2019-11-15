package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Arena;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArenaCommand extends AbstractCommand {

    public CreateArenaCommand() {
        super("createarena", false);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 2) { // Check if there are enough arguments given
            if (!Arena.containsArena(args[0])) { // Check if arena doesn't exist
                try {
                    Arena.createArena(new Arena(args[0], ((Player) sender).getLocation().toCenterLocation(), Integer.parseInt(args[1]))); // Create arena
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Arena &e" + args[0] + " &6successfully created based on your current location")); // Inform player
                } catch (NumberFormatException e) { // Catch NumberFormatException
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Radius needs to be a number")); // Inform player
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Specify a name and a radius for the arena"));
        }
    }
}
