package com.timvisee.customskulls.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import com.timvisee.customskulls.CustomSkulls;
import com.timvisee.customskulls.until.CustomSkullsUtility;

public class CommandHandler {

	private CustomSkulls plugin;
	private CommandSender sender;
	private String label;
	private String[] args;


	public CommandHandler(CustomSkulls instance, CommandSender sender, String label, String[] args) {
		this.plugin = instance;
		this.sender = sender;
		this.label = label;
		this.args = args;
	}

	public boolean status() {
		if(args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " help " + args[0] + ChatColor.YELLOW + " to view help");
			return true;
		}

		// Check permissions
		if(sender instanceof Player) {
			if(!plugin.getPermissionsManager().hasPermission((Player) sender, "customskulls.command.status")) {
				sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
				return true;
			}
		}

		// Define some variables with plugin data
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		@SuppressWarnings("unused")
		PluginDescriptionFile pdfFile = plugin.getDescription();

		sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS STATUS ]==========");
		sender.sendMessage(ChatColor.GRAY + "Plugin Information:");

		// Return the running time
		long diff = new Date().getTime() - plugin.getEnabledDate().getTime();
		int millis = (int) (diff % 1000);
		diff/=1000;
		int seconds = (int) (diff % 60);
		diff/=60;
		int minutes = (int) (diff % 60);
		diff/=60;
		int hours = (int) diff;
		String t = ChatColor.WHITE + String.valueOf(millis) + ChatColor.YELLOW + " Millis";
		if(seconds > 0 || minutes > 0 || hours > 0) {
			t = ChatColor.WHITE + String.valueOf(seconds) + ChatColor.YELLOW + " Secconds & " + t;
			if(minutes > 0 || hours > 0) {
				t = ChatColor.WHITE + String.valueOf(minutes) + ChatColor.YELLOW + " Minutes, " + t;
				if(hours > 0)
					t = ChatColor.WHITE + String.valueOf(hours) + ChatColor.YELLOW + " Hours, " + t;
			}
		}
		sender.sendMessage(ChatColor.GOLD + "Time Running: " + ChatColor.YELLOW + t);

		sender.sendMessage(ChatColor.GRAY + "Server Information:");

		// Return player info
		sender.sendMessage(ChatColor.GOLD + "Players: " + ChatColor.YELLOW + String.valueOf(plugin.getServer().getOnlinePlayers().size()) + " / " + String.valueOf(plugin.getServer().getMaxPlayers()));
		sender.sendMessage(ChatColor.GOLD + "Running Plugins: " + ChatColor.YELLOW + String.valueOf(pluginManager.getPlugins().length));

		// Return the version info
		sender.sendMessage(ChatColor.GOLD + "Server Version: " + ChatColor.YELLOW + plugin.getServer().getVersion());
		sender.sendMessage(ChatColor.GOLD + "Bukkit Version: " + ChatColor.YELLOW + plugin.getServer().getBukkitVersion());

		// Return the current date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String d = dateFormat.format(date);
		sender.sendMessage(ChatColor.GOLD + "Server Time: " + ChatColor.YELLOW + d);

		sender.sendMessage(ChatColor.GRAY + "Machine Information:");

		// Return server/java info
		sender.sendMessage(ChatColor.GOLD + "OS Name: " + ChatColor.YELLOW + System.getProperty("os.name"));
		sender.sendMessage(ChatColor.GOLD + "OS Version: " + ChatColor.YELLOW + System.getProperty("os.version"));
		sender.sendMessage(ChatColor.GOLD + "Java Version: " + ChatColor.YELLOW + System.getProperty("java.version"));
		return true;
	}

	public boolean reload() {
		if(args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " help " + args[0] + ChatColor.YELLOW + " to view help");
			return true;
		}

		// Check permissions
		if(sender instanceof Player) {
			if(!plugin.getPermissionsManager().hasPermission((Player) sender, "customskulls.command.reload")) {
				sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
				return true;
			}
		}

		sender.sendMessage(ChatColor.YELLOW + "[CustomSkulls] Reloading data...");
		long t = System.currentTimeMillis();
		// Reload all data
		plugin.reloadConfigFiles();
		long duration = System.currentTimeMillis() - t;
		sender.sendMessage(ChatColor.YELLOW + "[CustomSkulls] " + ChatColor.GREEN + "Data succesfuly loaded, took " + ChatColor.GOLD + String.valueOf(duration) + "ms" + ChatColor.GREEN + "!");
		return true;
	}

	public boolean version() {
		if(args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " help " + args[0] + ChatColor.YELLOW + " to view help");
			return true;
		}

		PluginDescriptionFile pdfFile = plugin.getDescription();
		sender.sendMessage(ChatColor.YELLOW + "This server is running Custom Skulls v" + pdfFile.getVersion());
		sender.sendMessage(ChatColor.YELLOW + "Custom Skulls is made by Tim Visee - timvisee.com");
		return true;
	}

	public boolean help() {
		// Help commands (/ss help [sub-categories])
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
			if(args.length == 1) {
				// View the help
				sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " help [command]" + ChatColor.WHITE + " : View help");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " give <player> <type/player> [amount]" + ChatColor.WHITE + " : Give skulls");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " reload" + ChatColor.WHITE + " : Reload all data");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " status" + ChatColor.WHITE + " : View all plugin status");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " version" + ChatColor.WHITE + " : View plugin version");
				return true;

			}
			if(args[1].equals("give") || args[1].equals("g") || args[1].equals("i")) {
				// View the help
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " " + args[1] + " <player> <type/player> [amount]" + ChatColor.WHITE + " : Give skulls");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GOLD + "player" + ChatColor.WHITE + " : Player to give skulls to");
				sender.sendMessage(ChatColor.GOLD + "type/player" + ChatColor.WHITE + " : Skull type or player name");
				sender.sendMessage(ChatColor.GOLD + "amount" + ChatColor.WHITE + " : Amount of skulls to give");
				return true;
			}
			if(args[1].equals("reload") || args[1].equals("load")) {
				// View the help
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " reload" + ChatColor.WHITE + " : Reload all data");
				return true;
			}
			if(args[1].equals("status") || args[1].equals("statics") || args[1].equals("stats") || args[1].equals("s")) {
				// View the help
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " status" + ChatColor.WHITE + " : View major plugin status");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GOLD + "This command is used for troubleshooting problems");
				return true;
			}
			if(args[1].equals("version") || args[1].equals("ver") || args[1].equals("v") ||
					args[1].equals("info") || args[1].equals("i") ||
					args[1].equals("about") || args[1].equals("a")) {
				// View the help
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
				sender.sendMessage(ChatColor.GOLD + "/" + label + " version" + ChatColor.WHITE + " : View plugin version info");
				return true;
			}

			// Check wrong command values
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " help " + ChatColor.YELLOW + "to view help");
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public boolean give() {
		if (sender instanceof Player) {
			if (!plugin.getPermissionsManager().hasPermission((Player) sender, "customskulls.command.give")){
				sender.sendMessage(ChatColor.DARK_RED + "You don't have customskulls.command.give.");
				return true;
			}
		}

		if (args.length == 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " give <player> <type/player> [amount]");
			return true;
		}

		Player player = Bukkit.getPlayer(args[1]);

		if (player == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + args[1] + " is not online");
			return true;
		}

		int amount;

		if (args.length < 3) {
			sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " give <player> <type/player> [amount]");
			return true;
		}

		if (args.length == 3) {
			amount = 1;
		} else {
			if (isInteger(args[3])) {
				amount = Integer.parseInt(args[3]);
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
				sender.sendMessage(ChatColor.YELLOW + args[3] + " is not a valid value");
				return true;
			}
		}

		if(args[2].equalsIgnoreCase("creeper")) {
			player.getInventory().addItem(new ItemStack(Material.CREEPER_HEAD, amount));
			player.sendMessage(ChatColor.GREEN + "You received " + String.valueOf(amount) + " creeper " + (amount==1 ? "skull" : "skulls"));
			return true;
		} else if(args[2].equalsIgnoreCase("zombie")) {
			player.getInventory().addItem(new ItemStack(Material.ZOMBIE_HEAD, amount));
			player.sendMessage(ChatColor.GREEN + "You received " + String.valueOf(amount) + " zombie " + (amount==1 ? "skull" : "skulls"));
			return true;
		} else if(args[2].equalsIgnoreCase("skeleton")) {
			player.getInventory().addItem(new ItemStack(Material.SKELETON_SKULL, amount));
			player.sendMessage(ChatColor.GREEN + "You received " + String.valueOf(amount) + " skeleton " + (amount==1 ? "skull" : "skulls"));
			return true;
		} else if(args[2].equalsIgnoreCase("wither")) {
			player.getInventory().addItem(new ItemStack(Material.WITHER_SKELETON_SKULL, amount));
			player.sendMessage(ChatColor.GREEN + "You received " + String.valueOf(amount) + " wither " + (amount==1 ? "skull" : "skulls"));
			return true;
		} else {
			if (args.length < 3) {
				sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " give <player> <type/player> [amount]");
				return true;
			}

			if (args[2].equalsIgnoreCase("player")) {
				player.getInventory().addItem(CustomSkullsUtility.getSkullItemStack(amount, (byte) 3));
				player.sendMessage(ChatColor.GREEN + "You received " + String.valueOf(amount) + " wither " + (amount==1 ? "skull" : "skulls"));
				return true;
			}

			player.getInventory().addItem(CustomSkullsUtility.getSkullItemStack(amount, args[2]));
			player.sendMessage(ChatColor.GREEN + "You received " + String.valueOf(amount) + " player " + (amount==1 ? "skull" : "skulls") + " of " + args[2]);
			return true;
		}
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}