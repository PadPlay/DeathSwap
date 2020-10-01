package me.padplay.deathswap.listener;

import me.padplay.deathswap.Main;
import me.padplay.deathswap.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

	public DeathListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		Player p = (Player) e.getEntity();

		if (Command.p1 == null) return;
		if (Command.p2 == null) return;

		Command.main.cancel();
		Command.cdown.cancel();
		Command.executer.cancel();
		if (p.getUniqueId().equals(Command.p1.getUniqueId())) {
			Command.p1.sendMessage("§7[§cDeathSwap§7] You died so §e" + Command.p2.getName() + "§7 won!");
			Command.p2.sendMessage("§7[§cDeathSwap§7] §e" + Command.p1.getName() + "§7 died so you won!");
		} else if (p.getUniqueId().equals(Command.p2.getUniqueId())) {
			Command.p2.sendMessage("§7[§cDeathSwap§7] You died so §e" + Command.p1.getName() + "§7 won!");
			Command.p1.sendMessage("§7[§cDeathSwap§7] §e" + Command.p2.getName() + "§7 died so you won!");
		}

		Command.p1 = null;
		Command.p2 = null;

	}
}