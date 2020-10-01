package me.padplay.deathswap.command;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.padplay.deathswap.Main;

public class Command implements CommandExecutor {

	private Main plugin;
	private int sec = 10;
	public static Player p1;
	public static Player p2;
	public static BukkitTask main;
	public static BukkitTask cdown;
	public static BukkitTask executer;
	private int newTime;

	private final HashMap<UUID, Location> locSave = new HashMap<>();

	public Command(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("deathswap").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("start")) {
				if (!sender.hasPermission("deathswap.start")) {
					sender.sendMessage("�7[�cDeathSwap�7] You do not have permission to perform this command!");
				}

				if (
						Bukkit.getScheduler().getPendingTasks().contains(cdown)
								|| Bukkit.getScheduler().getPendingTasks().contains(main)
								|| Bukkit.getScheduler().getPendingTasks().contains(executer)
				) {
					try {
						p1.sendMessage("�7[�cDeathSwap�7] A game is currently running so you cannot run another.");
					} catch (Exception ignored) { }
					return true;
				}
				p1 = (Player) sender;
				p2 = Bukkit.getPlayerExact(args[1]);
				if (p2 == null) {
					p1.sendMessage("�7[�cDeathSwap�7] �e" + args[1] + " �7does not seem to be online.");
					return true;
				} else {
					if (p1.getUniqueId() == p2.getUniqueId()) {
						p1.sendMessage("�7[�cDeathSwap�7] You cannot play with yourself :(.");
						return true;
					}
					if (!(sender instanceof Player)) {
						sender.sendMessage("Only player may execute this command.");
						return true;
					}
					p1.sendMessage("�7[�cDeathSwap�7] The Deathswap between �eyou �7and �e" + p2.getName()
							+ " �7will start �esoon�7.Every �e" + (int) plugin.getConfig().get("minimum time in seconds") / 60
							+ " - " + (int) plugin.getConfig().get("maximum time in seconds") / 60 + " minutes �7/�e "
							+ plugin.getConfig().get("minimum time in seconds") + " - "
							+ plugin.getConfig().get("maximum time in seconds") + " seconds �7your positions will be swapped. Kill your �eenemie�7 as fast as possible to win!");
					p2.sendMessage("�7[�cDeathSwap�7] The Deathswap between �eyou �7and �e" + p1.getName()
							+ " �7will start �esoon�7. Every �e" + (int) plugin.getConfig().get("minimum time in seconds") / 60
							+ " - " + (int) plugin.getConfig().get("maximum time in seconds") / 60 + " minutes �7/�e "
							+ plugin.getConfig().get("minimum time in seconds") + " - "
							+ plugin.getConfig().get("maximum time in seconds") + " seconds �7you will be swapped. Kill your �eenemie�7 as fast as possible to win!");

					executer = Bukkit.getScheduler().runTaskLater(plugin, (Runnable) () -> swap(p1, p2), 100);
				}
			} else if (args[0].equalsIgnoreCase("mintime")) {
				if (!sender.hasPermission("deathswap.settings")) {
					sender.sendMessage("�7[�cDeathSwap�7] You do not have permission to perform this command!");
				}
				if (Bukkit.getScheduler().getPendingTasks().contains(cdown)
						|| Bukkit.getScheduler().getPendingTasks().contains(main)) {
					sender.sendMessage("�7[�cDeathSwap�7] You can not change settings midgame!");
					return true;
				}
				try {
					newTime = Integer.parseInt(args[1]);
					if ((int) plugin.getConfig().get("maximum time in seconds") <= newTime) {
						sender.sendMessage(
								"�7[�cDeathSwap�7] The minimum time cannot be bigger or the same as the maximum time!");
						return true;
					}
					sender.sendMessage(
							"�7[�cDeathSwap�7] The new minimum time of �e" + newTime + "s �7was �eset�7!");
				} catch (NumberFormatException e) {
					sender.sendMessage("�7[�cDeathSwap�7] Your input was not an integer!");
				}
				plugin.getConfig().set("minimum time in seconds", newTime);
				plugin.saveConfig();
			} else if (args[0].equalsIgnoreCase("maxtime")) {
				if (!sender.hasPermission("deathswap.settings")) {
					sender.sendMessage("�7[�cDeathSwap�7] You do not have permission to perform this command!");
				}
				try {
					newTime = Integer.parseInt(args[1]);
					if ((int) plugin.getConfig().get("minimum time in seconds") >= newTime) {
						sender.sendMessage(
								"�7[�cDeathSwap�7] The maximum time cannot be smaller or the same as the minimum time!");
						return true;
					}
					sender.sendMessage(
							"�7[�cDeathSwap�7] The new maximimum time of �e" + newTime + "s �7was �eset�7!");
				} catch (NumberFormatException e) {
					sender.sendMessage("�7[�cDeathSwap�7] Your input was not an integer!");
				}
				plugin.getConfig().set("maximum time in seconds", newTime);
				plugin.saveConfig();
			} else {
				sender.sendMessage(
						"�7[�cDeathSwap�7] Unknown command. Use �e/deathswap help �7for a list of all commands.");
			}
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("stop")) {
				if (!sender.hasPermission("deathswap.start")) {
					sender.sendMessage("�7[�cDeathSwap�7] You do not have permission to perform this command!");
					return true;
				}
				if (!(Bukkit.getScheduler().getPendingTasks().contains(cdown)
						|| Bukkit.getScheduler().getPendingTasks().contains(main)
						|| Bukkit.getScheduler().getPendingTasks().contains(executer))) {
					sender.sendMessage("�7[�cDeathSwap�7] No deathswap is currently running.");
					return true;
				}
				try {
					p1.sendMessage("�7[�cDeathSwap�7] The deathswap was cancelled!");
					p2.sendMessage("�7[�cDeathSwap�7] The deathswap was cancelled!");
					executer.cancel();
					main.cancel();
					cdown.cancel();
					p1 = null;
					p2 = null;
				} catch (Exception e) {
				}
			} else if (args[0].equalsIgnoreCase("help")) {
				if (sender.hasPermission("deathswap.help")) {
					sender.sendMessage("\n");
					sender.sendMessage("\n");
					sender.sendMessage("\n");
					sender.sendMessage("\n");
					sender.sendMessage("\n");

					sender.sendMessage("�7------------------ �bDeathSwap-Help �7------------------");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap help");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap start [name]");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap stop");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap minTime [duration in seconds]");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap maxTime [duration in seconds]");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap resetSettings");
					sender.sendMessage("\n");
					sender.sendMessage("�7\u2192 �e/deathswap getTime");
					sender.sendMessage("\n");
					sender.sendMessage("�7--------------------------------------------------");

				} else {
					sender.sendMessage("�7[�cDeathSwap�7] You do not have permission to perform this command!");
				}
			} else if (args[0].equalsIgnoreCase("resetsettings")) {
				if (sender.hasPermission("deathswap.settings")) {
					plugin.getConfig().set("minimum time in seconds", 360);
					plugin.getConfig().set("maximum time in seconds", 480);
					plugin.saveConfig();
					sender.sendMessage("�7[�cDeathSwap�7] The settings were resetted.");
				}
			} else if (args[0].equalsIgnoreCase("gettime")) {
				sender.sendMessage(
						"�7[�cDeathSwap�7] Every �e" + (int) plugin.getConfig().get("minimum time in seconds") / 60
								+ " - " + (int) plugin.getConfig().get("maximum time in seconds") / 60 + " minutes �7/�e "
								+ plugin.getConfig().get("minimum time in seconds") + " - "
								+ plugin.getConfig().get("maximum time in seconds") + " seconds �7you will be swapped.");
			} else {
				sender.sendMessage(
						"�7[�cDeathSwap�7] Unknown command. Use �e/deathswap help �7for a list of all commands.");
			}
		} else {
			sender.sendMessage(
					"�7[�cDeathSwap�7] Unknown command. Use �e/deathswap help �7for a list of all commands.");
		}

		return false;
	}

	public void swap(Player p1, Player p2) {
		main = Bukkit.getScheduler().runTaskLater(plugin, (Runnable) () -> {

			int rand = new Random().nextInt(((int) plugin.getConfig().get("maximum time in seconds") * 20
					- (int) plugin.getConfig().get("minimum time in seconds") * 20) + 1)
					+ (int) plugin.getConfig().get("minimum time in seconds") * 20;
			p1.sendMessage("�7[�cDeathSwap�7] A new �etimer�7 was set!");
			p2.sendMessage("�7[�cDeathSwap�7] A new �etimer�7 was set!");

			cdown = Bukkit.getScheduler().runTaskTimer(plugin, (Runnable) () -> {

				if (sec > 0) {
					p1.sendMessage("�7[�cDeathSwap�7] Swap in �e" + sec + "�7 seconds!");
					p2.sendMessage("�7[�cDeathSwap�7] Swap in �e" + sec + "�7 seconds!");
				}
				sec--;
				if (sec == -1) {
					p1.sendMessage("�7[�cDeathSwap�7] Swap!");
					p2.sendMessage("�7[�cDeathSwap�7] Swap!");

					locSave.put(p1.getUniqueId(), p1.getLocation());
					locSave.put(p2.getUniqueId(), p2.getLocation());

					p1.teleport(locSave.get(p2.getUniqueId()));
					p2.teleport(locSave.get(p1.getUniqueId()));

					locSave.clear();

					sec = 10;
					cdown.cancel();
					swap(p1, p2);
				}
			}, rand, 20);
		}, 0);
	}

}