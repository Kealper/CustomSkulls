package com.timvisee.customskulls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.customskulls.Metrics.Graph;
import com.timvisee.customskulls.command.CommandHandler;
import com.timvisee.customskulls.listener.CustomSkullsEntityListener;
import com.timvisee.customskulls.manager.PermissionsManager;

public class CustomSkulls extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");
	
	private final CustomSkullsEntityListener entityListener = new CustomSkullsEntityListener(this);
	
	// Permissions and Economy manager
	private PermissionsManager permissionsManager;
	
	// Enabled date
	private Date enabledDate = new Date();
	
	public void onEnable() {
		// Reset the enabled date, to calculate the 'running time'
		enabledDate = new Date();
		
		PluginManager pm = getServer().getPluginManager();
		
		// Register the event listeners
		pm.registerEvents(this.entityListener, this);
		
		// Generate the  default config files if they aren't available
		generateDefaultConfigs();
						
		// Load the config files
		reloadConfigFiles();
				
		// Setup the permission manager
		setupPermissionsManager();
				
		// Setup Metrics
		setupMetrics();
		
		// Plugin has been enabled. Show a message in the console
		PluginDescriptionFile pdfFile = getDescription();
		log.info("[CustomSkulls] Custom Skulls v" + pdfFile.getVersion() + " Started");
	}

	public void onDisable() {
		log.info("[CustomSkulls] Custom Skulls Disabled");
	}
	
	/**
	 * Setup the metrics statics feature
	 * @return false if an error occurred
	 */
	public boolean setupMetrics() {
		try {
		    Metrics metrics = new Metrics(this);
		    // Construct a graph, which can be immediately used and considered as valid
		    // Amount of kills and drops
		    /*Graph graph = metrics.createGraph("Deaths and Drops");
		    graph.addPlotter(new Metrics.Plotter("Deaths") {
	            @Override
	            public int getValue() {
	            	return getShopManager().countShops();
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("Kills") {
	            @Override
	            public int getValue() {
	            	return getBoothManager().countBooths();
	            }
		    });*/
		    
		    // Used permissions systems
		    Graph graph2 = metrics.createGraph("Permisison Plugin Usage");
		    graph2.addPlotter(new Metrics.Plotter(getPermissionsManager().getUsedPermissionsSystemType().getName()) {
	            @Override
	            public int getValue() {
	            	return 1;
	            }
		    });
		    metrics.start();
		    return true;
		} catch (IOException e) {
		    // Failed to submit the statics :-(
			e.printStackTrace();
			return false;
		}
	}

	public void generateDefaultConfigs() {
		if(!getDataFolder().exists()) {
			log.info("[CustomSkulls] Creating new CustomSkulls folder");
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			log.info("[CustomSkulls] Generating default config file");
			this.saveDefaultConfig();
		}
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.permissionsManager = new PermissionsManager(this);
		this.permissionsManager.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public PermissionsManager getPermissionsManager() {
		return this.permissionsManager;
	}
	
	public boolean reloadConfigFiles() {
		File configFile = new File(getDataFolder(), "config.yml");
		try {
			getConfig().load(configFile);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Get the date when the plugin was enabled
	 * @return the enabled date
	 */
	public Date getEnabledDate() {
		return this.enabledDate;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("customskulls") || label.equalsIgnoreCase("customskull") || label.equalsIgnoreCase("skulls")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + label + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}

			CommandHandler ch = new CommandHandler(this, sender, label, args);
			if(args[0].equals("status") || args[0].equals("statics") || args[0].equals("stats") || args[0].equals("s")) {
				return ch.status();
			} else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("load")) {
				return ch.reload();
			} else if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v") ||
					args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("a")) {
				return ch.version();
			} else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {			
				return ch.help();
			} else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g") || args[0].equalsIgnoreCase("i")) {
				return ch.give();
			}
		}
		return false;
	}
}
