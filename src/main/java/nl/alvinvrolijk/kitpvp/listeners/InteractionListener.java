package nl.alvinvrolijk.kitpvp.listeners;

import nl.alvinvrolijk.kitpvp.utils.KitSelector;
import nl.alvinvrolijk.kitpvp.data.Arena;
import nl.alvinvrolijk.kitpvp.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractionListener implements Listener {

    public InteractionListener() {
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.HAND)) { // Check if clicked hand is EquipmentSlot.HAND
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { // Check if action is right click on a block
                if (e.getClickedBlock().getState() instanceof Sign) { // Check if clicked block is a sign
                    Sign sign = (Sign) e.getClickedBlock().getState(); // Get sign data
                    if (sign.getLine(0).contains("[KitPvP]")) { // Check if line 0 contains [KitPvP]
                        if (Arena.containsArena(ChatColor.stripColor(sign.getLine(1)))) { // Check if arena exists
                            e.getPlayer().openInventory(KitSelector.getInventory(ChatColor.stripColor(sign.getLine(1)))); // Opens the Kit Selector
                        } else {
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "This is not a valid arena")); // Inform player
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0) != null && e.getLine(0).contains("[KitPvP]")) { // Check if line 0 contains [KitPvP]
            if (Arena.containsArena(e.getLine(1))) { // Check if arena is existing
                // Color-formatting
                e.setLine(0, ChatColor.translateAlternateColorCodes('&', "&2&l[KitPvP]"));
                e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&a" + e.getLine(1)));
            } else {
                // Set block to AIR and inform player
                e.getBlock().setType(Material.AIR);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.error + "This is not a valid arena"));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) { // Check if entity is a player
            if (e.getEntity().getLocation().distance(e.getEntity().getWorld().getSpawnLocation()) < 25) { // If distance from spawn is less than 25, the damage event will be canceled
                e.setCancelled(true); // Cancel event
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true); // Cancel drop item event
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupArrow(PlayerPickupArrowEvent e) {
        e.setCancelled(true); // Cancel pickup arrow event
    }
}