package nl.alvinvrolijk.kitpvp.commands;

import nl.alvinvrolijk.kitpvp.data.Arena;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveArenaCommand extends AbstractCommand {

    public RemoveArenaCommand() {
        super("removearena", true);
    }

    public final void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) { // Check if there are enough arguments given
            if (Arena.containsArena(args[0])) { // Check if arena exists
                Arena.removeArena(Arena.getArena(args[0])); // Remove arena
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cArena &4" + args[0] + " &csuccessfully removed")); // Inform player
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Arena not found")); // Inform player
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.incorrectUsage + "Specify a arena name")); // Inform player
        }
    }
}
