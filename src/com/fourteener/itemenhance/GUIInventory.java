package com.fourteener.itemenhance;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIInventory implements Listener {
	private final Inventory inv; // This instance of the enhancement inventory
	
	// Allows for a new enhancement inventory to be created
	public GUIInventory () {
		inv = Bukkit.createInventory (null, 27, ConfigParser.getLangData("guiTitle"));
		fillInventory();
	}
	
	// Fills in the inventory with various items, and sets any applicable data
	public void fillInventory () {
		inv.setItem(0, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(1, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(4, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(5, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(6, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(7, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(8, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(9, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(10, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(11, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(12, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(13, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(14, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(17, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(18, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(19, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(22, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(23, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(24, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(25, createGuiItem(" ", null, Material.STAINED_GLASS_PANE, 15));
		inv.setItem(2, createGuiItem(ConfigParser.getLangData("guiMagic"), null, Material.STAINED_GLASS_PANE, 4));
		inv.setItem(20, createGuiItem(ConfigParser.getLangData("guiLucky"), null, Material.STAINED_GLASS_PANE, 4));
		inv.setItem(15, createGuiItem(ConfigParser.getLangData("guiItem"), null, Material.STAINED_GLASS_PANE, 4));
		inv.setItem(26, createGuiItem(ConfigParser.getLangData("guiConfirm"), null, Material.STAINED_GLASS_PANE, 5));
	}
	
	// Quick function to create a new ItemStack for the inventory
	public ItemStack createGuiItem (String name, List<String> lore, Material mat, int dataValue) {
		ItemStack item = new ItemStack(mat, 1, (short) dataValue); // Create the item
		ItemMeta itemMeta = item.getItemMeta(); // Gets ItemMeta
		itemMeta.setDisplayName(name); // Updates ItemMeta
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta); // Sets ItemMeta
		return item; // And Returns the item
	}
	
	// Causes the player to open this inventory
	public void openInventory (Player p) {
		p.openInventory(inv);
		return;
	}
	
	// Handles players clicking in the inventory
	@EventHandler
	public void onInventoryClick (InventoryClickEvent e) {
		String invName = e.getInventory().getName(); // Gets the name of the inventory clicked in
		// If it isn't an instance of this inventory, do nothing
		if (!invName.equals(inv.getName())) {
			return;
		}
		// If the player used a number key to move something to the hotbar, cancel it
		if (e.getClick().equals(ClickType.NUMBER_KEY)) {
			e.setCancelled(true);
		}
		// Set cancelled by default so that players can't take items out of the inventory
		e.setCancelled(true);
		
		// If the player is interacting with one of the three item slots, do nothing
		if (e.getRawSlot() == 3 || e.getRawSlot() == 16 || e.getRawSlot() == 21) {
			e.setCancelled(false); // Un-cancel the event
			return; // Pass it back to Spigot to handle
		// Handles the player clicking the confirm button
		} else if (e.getRawSlot() == 26) {
			e.setCancelled(true); // Cancels the click event
			EnhanceItem.enhanceItem(inv.getItem(16), inv.getItem(3), inv.getItem(21), e.getWhoClicked()); // Enhance the item (or attempt to)
			e.getWhoClicked().closeInventory(); // This inventory isn't needed any more, close it
		}
	}
}
