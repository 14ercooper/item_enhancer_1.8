package com.fourteener.itemenhance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static FileConfiguration pluginConfig;
	
	@Override
	public void onEnable () {
		saveDefaultConfig();
		pluginConfig = getConfig();
		getServer().getPluginManager().registerEvents(new EventListeners(), this);
		getServer().getPluginManager().registerEvents(new GUIInventory(), this);
		this.getCommand("ieadmin").setExecutor(new CommandProcessor());
	}
	
	@Override
	public void onDisable () {
		
	}
}
