package nl.alvinvrolijk.kitpvp.commands;

import net.md_5.bungee.api.ChatColor;
import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.data.KitPvpPlayer;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor {

    private final String commandName;
    private final String permission;
    private final boolean canConsoleUse;

    public AbstractCommand(String commandName, boolean canConsoleUse) {
        this.commandName = commandName;
        this.permission = "kitpvp.command." + commandName;
        this.canConsoleUse = canConsoleUse;
        KitPvP.kitPvP.getCommand(commandName).setExecutor(this);
//        Main.main.getCommand(commandName).setTabCompleter(new TabCompleter());
    }

    public static void registerCommands(){
        new ArenaListCommand();
        new CreateArenaCommand();
        new CreateKitCommand();
        new GetKitCommand();
        new KitListCommand();
        new RemoveArenaCommand();
        new RemoveKitCommand();
        new UpdateKitCommand();
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(!cmd.getLabel().equalsIgnoreCase(commandName)) return true;
        if(!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission for this.");
            return true;
        }
        if(!canConsoleUse && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command sorry!");
            return true;
        }
        if (sender instanceof Player) {
            if (!KitPvpPlayer.containsPlayer(((Player) sender).getUniqueId())) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.playerDataError));
                return true;
            }
        }
        execute(sender, args);
        return true;
    }
}
