package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Arena;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ArenaListCommand extends AbstractCommand {

    public ArenaListCommand() {
        super("arenalist", true);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) { // Check if there are enough arguments given
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lList of Arena's")); // Send message

            for (Map.Entry<String, Arena> entry : Arena.getArenas().entrySet()) { // Get all Arena-objects
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + entry.getKey())); // Send message
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.tooMuchArguments)); // Inform player
        }
    }
}
