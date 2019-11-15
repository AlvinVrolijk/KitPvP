package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class GetKitCommand extends AbstractCommand {

    public GetKitCommand() {
        super("getkit", false);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) { // Check if there are enough arguments given
            if (Kit.containsKit(args[0])) { // Check if kit exists
                Kit kit = Kit.getKit(args[0]); // Get kit object
                try {
                    ((Player) sender).getInventory().setContents(Serializiation.itemStackArrayFromBase64(kit.getItems())); // Set inventory contents
                    ((Player) sender).getInventory().setArmorContents(Serializiation.itemStackArrayFromBase64(kit.getArmor())); // Set armor contents
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You received the &e" + args[0] + " &6kit")); // Inform player
                } catch (IOException e) {
                    e.printStackTrace(); // Error
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Kit not found")); // Inform player
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Specify a kit name")); // Inform player
        }
    }
}
