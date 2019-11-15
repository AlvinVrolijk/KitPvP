package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCommand extends AbstractCommand {

    public CreateKitCommand() {
        super("createkit", false);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 2) { // Check if there are enough arguments given
            Material icon = Material.getMaterial(args[1]); // Get material
            if (icon != null) { // Check if material is valid
                if (!Kit.containsKit(args[0])) { // Check if arena doesn't exist
                    Kit.createKit(new Kit(args[0], icon, Serializiation.itemStackArrayToBase64(((Player) sender).getInventory().getContents()), Serializiation.itemStackArrayToBase64(((Player) sender).getInventory().getArmorContents()))); // Create arena
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Kit &e" + args[0] + " &6successfully created based on your current inventory and armor state")); // Inform player
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Kit name already in use")); // Inform player
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "This is not a valid material name")); // Inform player
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Specify a name and a material for the icon")); // Inform player
        }
    }
}
