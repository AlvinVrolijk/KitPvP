package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveKitCommand extends AbstractCommand {

    public RemoveKitCommand() {
        super("removekit", true);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) { // Check if there are enough arguments given
            if (Kit.containsKit(args[0])) { // Check if arena exists
                Kit.removeKit(Kit.getKit(args[0])); // Remove kit
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cKit &4" + args[0] + " &csuccessfully removed")); // Inform player
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Kit not found")); // Inform player
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Specify a kit name")); // Inform player
        }
    }
}
