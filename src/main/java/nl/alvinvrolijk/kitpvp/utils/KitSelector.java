package nl.alvinvrolijk.kitpvp.utils;

import net.md_5.bungee.api.ChatColor;
import nl.alvinvrolijk.kitpvp.data.Kit;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

public class KitSelector {

    public static Inventory getInventory(String arena) {
        // Create the inventory
        Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.GOLD + "Kit Selector: " + ChatColor.YELLOW + arena);

        // Get all kits
        for (Map.Entry<String, Kit> entry : Kit.getKits().entrySet()) {
            ItemStack itemStack = new ItemStack(entry.getValue().getIcon(), 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.YELLOW + entry.getKey());
            ArrayList<String> lores = new ArrayList<>();
            lores.add(ChatColor.GRAY + "Choose this kit");
            itemMeta.setLore(lores);
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }

        // Return the inventory
        return inventory;
    }
}
