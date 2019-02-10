package com.fourteener.itemenhance;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListeners implements Listener {
	
	@EventHandler
	public void entityDamage (EntityDamageEvent event) {
		// Attacked by an entity, perhaps a player? Should check
		if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			// If it is the player, do player calculation using other sword and this armor
			if (e.getDamager() instanceof Player) {
				// Handle a player attacking a player
				if (e.getEntity() instanceof Player) {
					// Handles calculating the bonus defense of the attacked
					// Need to get the armor data
					Player player = (Player) event.getEntity();
					PlayerInventory inventory = player.getInventory();
					ItemStack[] armor = inventory.getArmorContents();
					// Calculate the bonus defense of the attack
					double defBonus = 0;
					for (ItemStack i : armor) {
						// Gets the item's lore
						ItemMeta meta = i.getItemMeta();
						List<String> lore = meta.getLore();
						// Parses the lore to determine how much defense to add (if any)
						for (String s : lore) {
							// Does the lore contain an armor modifier?
							if (s.contains(ConfigParser.getLangData("armorBonus"))) {
								double d = Double.parseDouble(s.replaceAll("[\\D]", ""));
								if (d >= 0)
									defBonus += d;
							}
						}
					}

					// Handles calculating the bonus damage of the attacker
					// Gets the item in the player's hand
					Player damager = (Player) e.getDamager();
					inventory = damager.getInventory();
					ItemStack i = inventory.getItemInHand();
					// Gets any lore the item may have and parses it for a damage bonus
					ItemMeta meta = i.getItemMeta();
					List<String> lore = meta.getLore();
					double atkBonus = 0;
					for (String s : lore) {
						if (s.contains(ConfigParser.getLangData("damageBonus"))) {
							double d = Double.parseDouble(s.replaceAll("[\\D]", ""));
							if (d >= 0)
								atkBonus += d;
						}
					}
					
					// Finally, update the damage of the attack as needed
					double dmg = e.getDamage();
					dmg += atkBonus;
					dmg -= defBonus;
					if (dmg < 0) dmg = 0;
					event.setDamage(dmg);
				}
				
				// Handle a player attacking something else
				else {
					// Gets the item in the player's hand
					Player damager = (Player) e.getDamager();
					PlayerInventory inventory = damager.getInventory();
					ItemStack i = inventory.getItemInHand();
					// Gets any lore the item may have and parses it for a damage bonus
					ItemMeta meta = i.getItemMeta();
					List<String> lore = meta.getLore();
					double atkBonus = 0;
					for (String s : lore) {
						if (s.contains(ConfigParser.getLangData("damageBonus"))) {
							double d = Double.parseDouble(s.replaceAll("[\\D]", ""));
							if (d >= 0)
								atkBonus += d;
						}
					}
					double dmg = e.getDamage();
					dmg += atkBonus;
					if (dmg < 0) dmg = 0;
					event.setDamage(dmg);
				}
				return;
			}
		}
		// If it wasn't a player dealing damage but a player taking damage, just calculate using the armor
		else if (event.getEntity() instanceof Player) {
			// Need to get the armor data
			Player player = (Player) event.getEntity();
			PlayerInventory inventory = player.getInventory();
			ItemStack[] armor = inventory.getArmorContents();
			// Calculate the bonus defense of the attack
			double defBonus = 0;
			for (ItemStack i : armor) {
				// Gets the item's lore
				ItemMeta meta = i.getItemMeta();
				List<String> lore = meta.getLore();
				// Parses the lore to determine how much defense to add (if any)
				for (String s : lore) {
					// Does the lore contain an armor modifier?
					if (s.contains(ConfigParser.getLangData("armorBonus"))) {
						double d = Double.parseDouble(s.replaceAll("[\\D]", ""));
						if (d >= 0)
							defBonus += d;
					}
				}
			}
			// Modify the damage received
			double dmg = event.getDamage();
			dmg -= defBonus;
			if (dmg < 0) dmg = 0;
			event.setDamage(dmg);
		}
		// Do nothing if a player wasn't involved, just pass it back to Spigot to handle
		else {
			return;
		}
	}
	
	@EventHandler
	public void playerInteract (PlayerInteractEvent event) {
		// Gets the item clicked with
		ItemStack item = event.getItem();
		// If the item clicked isn't an enhance stone, just pass everything back to Spigot
		if (!item.isSimilar(ConfigParser.getItemStack("enhance"))) {
			return;
		}
		// Otherwise, we can begin enhancing the item!
		GUIInventory gui = new GUIInventory ();
		gui.openInventory(event.getPlayer());
		event.setCancelled(true); // No need to process the event if we're enhancing
	}
}
