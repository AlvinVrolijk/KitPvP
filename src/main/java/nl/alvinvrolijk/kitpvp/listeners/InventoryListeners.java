package nl.alvinvrolijk.kitpvp.listeners;

import nl.alvinvrolijk.kitpvp.data.Arena;
import nl.alvinvrolijk.kitpvp.data.Kit;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import nl.alvinvrolijk.kitpvp.utils.Serializiation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;

public class InventoryListeners implements Listener {

    public InventoryListeners() {}

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().equals(e.getView().getTopInventory()) && e.getView().getTitle().contains("Selector")) { // Check if inventory is the Kit Selector
                e.setCancelled(true); // Cancel click event

                if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) { // Check if item isn't null and isn't air
                    Kit kit = Kit.getKit(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())); // Get the kit
                    if (kit != null) { // Check if kit is existing
                        try {
                            e.getWhoClicked().getInventory().setContents(Serializiation.itemStackArrayFromBase64(kit.getItems())); // Set inventory items
                            e.getWhoClicked().getInventory().setArmorContents(Serializiation.itemStackArrayFromBase64(kit.getArmor())); // Set armor items

                            Location center = Arena.getArena(ChatColor.stripColor(e.getView().getTitle().split(": ")[1])).getCenter(); // Get the center of the arena
                            Integer radius = Arena.getArena(ChatColor.stripColor(e.getView().getTitle().split(": ")[1])).getRadius(); // Get spawn radius

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers " + center.getBlockX() + " " + center.getBlockZ() + " 5 " + radius + " false " + ((Player) e.getWhoClicked()).getDisplayName()); // Spread player over the arena
                            e.getView().close(); // Close kit selector
                            ((Player) e.getWhoClicked()).sendTitle(ChatColor.GOLD + "Goodluck!", ChatColor.YELLOW + "and remember, play fair!", 10, 40, 10); // Send title
                        } catch (IOException e1) {
                            e1.printStackTrace(); // Print stacktrace
                        }
                    } else {
                        e.getView().close(); // Close kit selector
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "This is not a valid arena")); // Inform player
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true); // Cancel hunger
    }
}
