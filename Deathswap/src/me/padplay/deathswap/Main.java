package me.padplay.deathswap;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.plugin.java.JavaPlugin;

import me.padplay.deathswap.command.command;
import me.padplay.deathswap.listener.DeathListener;

public class Main extends JavaPlugin{
	
	public void onEnable() {
		Log.info("");
		Log.info("-------------------------");
		Log.info("This plugin was coded by PadPlay!");
		Log.info("-------------------------");
		Log.info("");
		Log.info("Deathswap is now enabled.");
		Log.info("");
		
		new command(this);
		new DeathListener(this);
		
		loadConfig();
	}

	public void loadConfig() {
		this.getConfig().addDefault("minimum time in seconds", 360);
		this.getConfig().addDefault("maximum time in seconds", 480);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
}
