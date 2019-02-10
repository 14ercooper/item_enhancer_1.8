package com.fourteener.itemenhance;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigParser {
	public static ItemStack getItemStack (String key) { // Returns the item specified by the key by parsing the config file
		// Get data from the config file
		String name = Main.pluginConfig.getString(key + ".name");
		int unbreaking = Main.pluginConfig.getInt(key + ".unbreaking");
		List<String> lore = Main.pluginConfig.getStringList(key + ".lore");
		
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
	
	public static float getRawFailChance (int level) { // Returns the raw fail chance of the enhancement (no modifiers)
		String failChance = Main.pluginConfig.getString("chance.default");
		try {
			failChance = Main.pluginConfig.getString("chance.l" + level);
		} catch (Exception e) {
			// Yeah, let's just ignore this since this just means it isn't in the config and that's fine
		}
		return Float.parseFloat(failChance);
	}
	
	// Parses the amount a lucky scroll increases the odds of success
	public static float getLuckIncrease () {
		String luckBonus = Main.pluginConfig.getString("LuckyIncrease");
		return Float.parseFloat(luckBonus);
	}
	
	public static float getDowngradeNumber (int level) { // Returns the raw fail chance of the enhancement (no modifiers)
		String downgrade = Main.pluginConfig.getString("downgrade.default");
		try {
			downgrade = Main.pluginConfig.getString("downgrade.l" + level);
		} catch (Exception e) {
			// Yeah, let's just ignore this since this just means it isn't in the config and that's fine
		}
		return Float.parseFloat(downgrade);
	}
	
	// Gets if a level should be broadcase
	public static boolean getBroadcast (int level) {
		boolean broadcast = Main.pluginConfig.getBoolean("broadcast.default");
		try {
			broadcast = Main.pluginConfig.getBoolean("broadcast.l" + level);
		} catch (Exception e) {
			// Same deal as above, not typing it a third time
		}
		return broadcast;
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
		return langData;
	}
	
	// Constructs the status broadcast that is sent to the entire world
	// <player> <status> <item> to <level>
	public static String constructWorldBroadcast (HumanEntity player, boolean status, ItemStack item, double level) {
		String text = getLangData("broadcastMessage");
		text.replace("<player>", player.getCustomName());
		if (status)
			text.replace("<status>", getLangData("statusSuccess"));
		else
			text.replace("<status>", getLangData("statusFail"));
		text.replace("<item>", item.getItemMeta().getDisplayName());
		text.replace("<level>", Double.toString(level));
		return text;
	}
}
