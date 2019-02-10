package com.fourteener.itemenhance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandProcessor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Did the command get called by a player?
		if (sender instanceof Player && label.equalsIgnoreCase("ieadmin")) {
			Player player = (Player) sender;
			// Is the player an admin or otherwise granted permission?
			if (player.isOp() || player.hasPermission("itemenhance.admin")) {
				// If so, give them the requested item
				if (args[1].equalsIgnoreCase("enhance")) {
					player.getInventory().addItem(ConfigParser.getItemStack("enhance"));
					return true;
				} else if (args[1].equalsIgnoreCase("magic")) {
					player.getInventory().addItem(ConfigParser.getItemStack("magic"));
					return true;
				} else if (args[1].equalsIgnoreCase("lucky")) {
					player.getInventory().addItem(ConfigParser.getItemStack("lucky"));
					return true;
				}
				return false.
			}
			return false;
		}
		return false;
	}
}
