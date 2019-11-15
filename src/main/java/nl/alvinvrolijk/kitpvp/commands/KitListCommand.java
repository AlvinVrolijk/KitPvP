package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class KitListCommand extends AbstractCommand {

    public KitListCommand() {
        super("kitlist", true);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) { // Check if there are enough arguments given
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lList of Kits")); // Send message

            for (Map.Entry<String, Kit> entry : Kit.getKits().entrySet()) { // Get all Kit-objects
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + entry.getKey())); // Send message
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.tooMuchArguments)); // Inform player
        }
    }
}
