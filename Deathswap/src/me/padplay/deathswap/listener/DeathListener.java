package me.padplay.deathswap.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.padplay.deathswap.Main;
import me.padplay.deathswap.command.command;

public class DeathListener implements Listener {
	
	public DeathListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		Player p = (Player) e.getEntity();
		
		if (command.p1 == null) return;
		if (command.p2 == null) return;
		
		if (p.getUniqueId().equals(command.p1.getUniqueId())) {
			command.main.cancel();
			command.cdown.cancel();
			command.executer.cancel();
			command.p1.sendMessage("§7[§cDeathSwap§7] You died so §e" + command.p2.getName() + "§7 won!");
			command.p2.sendMessage("§7[§cDeathSwap§7] §e" + command.p1.getName() + "§7 died so you won!");
			command.p1 = null;
			command.p2 = null;
			return;
		}

		if (p.getUniqueId().equals(command.p2.getUniqueId())) {
			command.main.cancel();
			command.cdown.cancel();
			command.executer.cancel();
			command.p2.sendMessage("§7[§cDeathSwap§7] You died so §e" + command.p1.getName() + "§7 won!");
			command.p1.sendMessage("§7[§cDeathSwap§7] §e" + command.p2.getName() + "§7 died so you won!");
			command.p1 = null;
			command.p2 = null;
			return;
		}
	}

}
