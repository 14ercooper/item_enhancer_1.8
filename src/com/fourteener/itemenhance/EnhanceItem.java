package com.fourteener.itemenhance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

public class EnhanceItem {
	
	private static final Material[] weapons = {Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.GOLD_SWORD,
			Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.GOLD_AXE, Material.BOW};
	private static final Material[] armors = {Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
			Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
			Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
			Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
			Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS};
	
	// All the logic for enhancing an item
	public static void enhanceItem (ItemStack item, ItemStack magic, ItemStack lucky, HumanEntity player) {
		// First, we need to figure out if this is a weapon or armor
		boolean isArmor = false;
		if (Arrays.asList(armors).contains(item.getType())) { // Is it armor?
			isArmor = true;
		} else if (Arrays.asList(weapons).contains(item.getType())) { // Is it a weapon?
			isArmor = false;
		} else { // If it's something else, it really can't be enhanced. Let the player know and return.
			((Player) player).sendMessage(ConfigParser.getLangData("badItem"));
			player.getInventory().addItem(item); // Give them back the item, though
			return;
		}
		
		// Is the enhancement lucky or magical?
		boolean isLucky = false;
		if (lucky.equals(ConfigParser.getItemStack("lucky")))
			isLucky = true;
		boolean isMagic = false;
		if (magic.equals(ConfigParser.getItemStack("maigc")))
			isMagic = true;
		
		// Handles if it is a weapon
		if (!isArmor) {
			// First, figure out the level of enhancement the item has, if any
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = itemMeta.getLore();
			// Parse the lore for any enhancements
			int enhanceLevel = 0;
			for (String s : lore) {
				if (s.contains(ConfigParser.getLangData("damageBonus"))) {
					enhanceLevel = Integer.parseInt(s.replaceAll("[\\D]", ""));
				}
			}
			
			// Next, calculate the odds of the enhancement succeeding
			enhanceLevel++;
			double failChance = ConfigParser.getRawFailChance(enhanceLevel);
			if (failChance == 2) { // If the enhancement is disabled for this level, let the player know
				((Player) player).sendMessage(ConfigParser.getLangData("notAllowed"));
				return;
			}
			if (isLucky) { // Is the player lucky?
				failChance -= ConfigParser.getLuckIncrease();
				if (failChance < 0) failChance = 0;
			}
				
			// Does the enhancement succeed?
			double randNum = Math.random();
			boolean success = true;
			if (randNum <= failChance)
				success = false;
			
			// If it does, enhance the item and broadcast (if needed)
			if (success) {
				// This is a new enhancement, so we add the lore
				if (enhanceLevel == 1) {
					lore.add(ConfigParser.getLangData("damageBonus") + enhanceLevel + ConfigParser.getLangData("damageBonusPost"));
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
				}
				// This is an existing enhancement, so we update the lore
				else {
					List<String> newLore = new ArrayList<String>();
					for (String s : lore) {
						if (s.contains(ConfigParser.getLangData("damageBonus"))) {
							newLore.add(ConfigParser.getLangData("damageBonus") + enhanceLevel + ConfigParser.getLangData("damageBonusPost"));
						}
						else {
							newLore.add(s);
						}
					}
				}
				// Broadcast if needed
				if (ConfigParser.getBroadcast(enhanceLevel)) {
					Bukkit.getServer().broadcastMessage(ConfigParser.constructWorldBroadcast(player, success, item, enhanceLevel));
				}
				// Player always gets a broadcast
				((Player) player).sendMessage(ConfigParser.getLangData("enhanceSuccess"));
			}
			// Otherwise, level down the item as needed and broadcast
			else {
				// Update the lore
				enhanceLevel--; // Since we incremented this above
				if (!isMagic) { // If there wasn't a magic scroll, de-enhance the item
					enhanceLevel -= ConfigParser.getDowngradeNumber(enhanceLevel);
					if (enhanceLevel <= 0) { // This item is no longer enhanced
						List<String> newLore = new ArrayList<String>();
						for (String s : lore) {
							if (s.contains(ConfigParser.getLangData("damageBonus"))) {
								// Skip adding this line of lore back in
							}
							else {
								newLore.add(s);
							}
						}
					}
					else { // Just update the lore
						List<String> newLore = new ArrayList<String>();
						for (String s : lore) {
							if (s.contains(ConfigParser.getLangData("damageBonus"))) {
								newLore.add(ConfigParser.getLangData("damageBonus") + enhanceLevel + ConfigParser.getLangData("damageBonusPost"));
							}
							else {
								newLore.add(s);
							}
						}
					}
				}
				// Broadcast if needed
				if (ConfigParser.getBroadcast(enhanceLevel++)) {
					Bukkit.getServer().broadcastMessage(ConfigParser.constructWorldBroadcast(player, success, item, enhanceLevel));
				}
				// Player always gets a broadcast
				((Player) player).sendMessage(ConfigParser.getLangData("enhanceFailed"));
			}
		}
		
		// Handles if it is armor
		else {
			// First, figure out the level of enhancement the item has, if any
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = itemMeta.getLore();
			// Parse the lore for any enhancements
			int enhanceLevel = 0;
			for (String s : lore) {
				if (s.contains(ConfigParser.getLangData("armorBonus"))) {
					enhanceLevel = Integer.parseInt(s.replaceAll("[\\D]", ""));
				}
			}
			
			// Next, calculate the odds of the enhancement succeeding
			enhanceLevel++;
			double failChance = ConfigParser.getRawFailChance(enhanceLevel);
			if (failChance == 2) { // If the enhancement is disabled for this level, let the player know
				((Player) player).sendMessage(ConfigParser.getLangData("notAllowed"));
				return;
			}
			if (isLucky) { // Is the player lucky?
				failChance -= ConfigParser.getLuckIncrease();
				if (failChance < 0) failChance = 0;
			}
				
			// Does the enhancement succeed?
			double randNum = Math.random();
			boolean success = true;
			if (randNum <= failChance)
				success = false;
			
			// If it does, enhance the item and broadcast (if needed)
			if (success) {
				// This is a new enhancement, so we add the lore
				if (enhanceLevel == 1) {
					lore.add(ConfigParser.getLangData("armorBonus") + enhanceLevel + ConfigParser.getLangData("armorBonusPost"));
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
				}
				// This is an existing enhancement, so we update the lore
				else {
					List<String> newLore = new ArrayList<String>();
					for (String s : lore) {
						if (s.contains(ConfigParser.getLangData("armorBonus"))) {
							newLore.add(ConfigParser.getLangData("armorBonus") + enhanceLevel + ConfigParser.getLangData("armorBonusPost"));
						}
						else {
							newLore.add(s);
						}
					}
				}
				// Broadcast if needed
				if (ConfigParser.getBroadcast(enhanceLevel)) {
					Bukkit.getServer().broadcastMessage(ConfigParser.constructWorldBroadcast(player, success, item, enhanceLevel));
				}
				// Player always gets a broadcast
				((Player) player).sendMessage(ConfigParser.getLangData("enhanceSuccess"));
			}
			// Otherwise, level down the item as needed and broadcast
			else {
				// Update the lore
				enhanceLevel--; // Since we incremented this above
				if (!isMagic) { // If there wasn't a magic scroll, de-enhance the item
					enhanceLevel -= ConfigParser.getDowngradeNumber(enhanceLevel);
					if (enhanceLevel <= 0) { // This item is no longer enhanced
						List<String> newLore = new ArrayList<String>();
						for (String s : lore) {
							if (s.contains(ConfigParser.getLangData("armorBonus"))) {
								// Skip adding this line of lore back in
							}
							else {
								newLore.add(s);
							}
						}
					}
					else { // Just update the lore
						List<String> newLore = new ArrayList<String>();
						for (String s : lore) {
							if (s.contains(ConfigParser.getLangData("armorBonus"))) {
								newLore.add(ConfigParser.getLangData("armorBonus") + enhanceLevel + ConfigParser.getLangData("armorBonusPost"));
							}
							else {
								newLore.add(s);
							}
						}
					}
				}
				// Broadcast if needed
				if (ConfigParser.getBroadcast(enhanceLevel++)) {
					Bukkit.getServer().broadcastMessage(ConfigParser.constructWorldBroadcast(player, success, item, enhanceLevel));
				}
				// Player always gets a broadcast
				((Player) player).sendMessage(ConfigParser.getLangData("enhanceFailed"));
			}
		}
		
		// Stuff like removing the scrolls and enhance stone that's the same for both cases
		ItemStack enhanceStone = player.getInventory().getItemInHand();
		if (enhanceStone.getAmount() > 1) {
			enhanceStone.setAmount(enhanceStone.getAmount() - 1);
			player.setItemInHand(enhanceStone);
		} else {
			player.setItemInHand(new ItemStack(Material.AIR));
		}
		// Give the updated item to the player
		player.getInventory().addItem(item);
	}
}
