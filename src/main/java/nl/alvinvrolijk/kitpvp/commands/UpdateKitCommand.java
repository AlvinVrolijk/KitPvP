package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateKitCommand extends AbstractCommand {

    public UpdateKitCommand() {
        super("updatekit", false);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) { // Check if there are enough arguments given
            if (Kit.containsKit(args[0])) { // Check if kit is existing
                Kit kit = Kit.getKit(args[0]); // Get Kit-object
                kit.setItems(((Player) sender).getInventory().getContents()); // Set inventory items
                kit.setArmor(((Player) sender).getInventory().getArmorContents()); // Set armor items
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Kit &e" + args[0] + " &6successfully updated")); // Inform player
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Kit not found")); // Inform player
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Specify a kit name")); // Inform player
        }
    }
}
