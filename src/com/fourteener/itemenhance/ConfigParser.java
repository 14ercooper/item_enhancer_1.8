package com.fourteener.itemenhance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigParser {
	public static ItemStack getItemStack (String key) { // Returns the item specified by the key by parsing the config file
		// Get data from the config file
		String name = ChatColor.translateAlternateColorCodes('&', Main.pluginConfig.getString(key + ".name"));
		int unbreaking = Main.pluginConfig.getInt(key + ".unbreaking");
		List<String> lore = new ArrayList<String>(), oldLore = Main.pluginConfig.getStringList(key + ".lore");
		for (String s : oldLore) {
			lore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		
		// Initialize the item stack
		ItemStack item = null;
		if (key.equalsIgnoreCase("enhance"))
			item = new ItemStack(Material.QUARTZ);
		else if (key.equalsIgnoreCase("magic") || key.equalsIgnoreCase("lucky"))
			item = new ItemStack(Material.PAPER);
		
		// Add the enchantment
		item.addUnsafeEnchantment(Enchantment.DURABILITY, unbreaking);
		
		// Add the name and lore
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		
		// Return the item stack
		return item;
	}
	
	public static ItemStack getItemStack (String key, int stackSize) { // Gets multiple of the item
		// Get data from the config file
		String name = ChatColor.translateAlternateColorCodes('&', Main.pluginConfig.getString(key + ".name"));
		int unbreaking = Main.pluginConfig.getInt(key + ".unbreaking");
		List<String> lore = new ArrayList<String>(), oldLore = Main.pluginConfig.getStringList(key + ".lore");
		for (String s : oldLore) {
			lore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		
		// Initialize the item stack
		ItemStack item = null;
		if (key.equalsIgnoreCase("enhance"))
			item = new ItemStack(Material.QUARTZ, stackSize);
		else if (key.equalsIgnoreCase("magic") || key.equalsIgnoreCase("lucky"))
			item = new ItemStack(Material.PAPER, stackSize);
		
		// Add the enchantment
		item.addUnsafeEnchantment(Enchantment.DURABILITY, unbreaking);
		
		// Add the name and lore
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		
		// Return the item stack
		return item;
	}
	
	public static float getRawFailChance (int level) { // Returns the raw fail chance of the enhancement (no modifiers)
		String failChance = Main.pluginConfig.getString("chance.default");
		try {
			failChance = Main.pluginConfig.getString("chance.l" + level);
		} catch (Exception e) {
			// So it isn't in the config file, load the default
			System.out.println("Failed");
			failChance = Main.pluginConfig.getString("chance.default");
		}
		return Float.parseFloat(failChance);
	}
	
	// Parses the amount a lucky scroll increases the odds of success
	public static float getLuckIncrease () {
		try {
			String luckBonus = Main.pluginConfig.getString("LuckyIncrease");
			return Float.parseFloat(luckBonus);
		} catch (Exception e) {
			return 0.1f;
		}
	}
	
	public static float getDowngradeNumber (int level) { // Returns the raw fail chance of the enhancement (no modifiers)
		String downgrade = Main.pluginConfig.getString("downgrade.default");
		try {
			downgrade = Main.pluginConfig.getString("downgrade.l" + level);
		} catch (Exception e) {
			downgrade = Main.pluginConfig.getString("downgrade.default");
		}
		return Float.parseFloat(downgrade);
	}
	
	// Gets if a level should be broadcast
	public static boolean getBroadcast (int level) {
		return false;
		/*
		boolean broadcast = true;
		try {
			broadcast = Main.pluginConfig.getBoolean("broadcast.l" + level);
		} catch (Exception e) {
			broadcast = Main.pluginConfig.getBoolean("broadcast.default");
		}
		return broadcast; */
	}
	
	// Parses the lang data for the translated string
	public static String getLangData (String key) {
		String langData = "";
		try {
			langData = Main.pluginConfig.getString("lang." + key);
		} catch (Exception e) {
			// This should never be called unless the config file is messed up
			e.printStackTrace();
		}
		if (langData == null)
			return langData;
		else
			return ChatColor.translateAlternateColorCodes('&', langData);
	}
	
	// Constructs the status broadcast that is sent to the entire world
	// <player> <status> <item> to <level>
	public static String constructWorldBroadcast (HumanEntity player, boolean status, ItemStack item, double level) {
		String text = getLangData("broadcastMessage");
		text = text.replace("<player>", player.getName());
		if (status)
			text = text.replace("<status>", getLangData("statusSuccess"));
		else
			text = text.replace("<status>", getLangData("statusFail"));
		if (item.getItemMeta().hasDisplayName())
			text = text.replace("<item>", item.getItemMeta().getDisplayName());
		else {
			String itemName = item.getType().name();
			itemName = itemName.replace("_", " ");
			itemName = WordUtils.capitalizeFully(itemName);
			text = text.replace("<item>", itemName);
		}
		text = text.replace("<level>", (Double.toString(level)).replace(".0", ""));
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}
