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
				if (args.length < 1)
					return false;
				// If so, give them the requested item
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("enhance")) {
						player.getInventory().addItem(ConfigParser.getItemStack("enhance"));
						return true;
					} else if (args[0].equalsIgnoreCase("magic")) {
						player.getInventory().addItem(ConfigParser.getItemStack("magic"));
						return true;
					} else if (args[0].equalsIgnoreCase("lucky")) {
						player.getInventory().addItem(ConfigParser.getItemStack("lucky"));
						return true;
					}
				}
				else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("enhance")) {
						player.getInventory().addItem(ConfigParser.getItemStack("enhance", Integer.parseInt(args[1])));
						return true;
					} else if (args[0].equalsIgnoreCase("magic")) {
						player.getInventory().addItem(ConfigParser.getItemStack("magic", Integer.parseInt(args[1])));
						return true;
					} else if (args[0].equalsIgnoreCase("lucky")) {
						player.getInventory().addItem(ConfigParser.getItemStack("lucky", Integer.parseInt(args[1])));
						return true;
					}
				}
				return false;
			}
			return false;
		}
		return false;
	}
}
