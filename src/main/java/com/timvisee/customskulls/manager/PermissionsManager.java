package com.timvisee.customskulls.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermissionsManager {
	
	private PermissionsSystemType permsType = PermissionsSystemType.NONE;
	private Server s;
	private Plugin p;
	
	// Vault
	public Permission vaultPerms = null;
	
	/**
	 * Constructor
	 * @param s server
	 * @param logPrefix log prefix (plugin name)
	 */
	public PermissionsManager(Plugin p) {
		this.s = p.getServer();
		this.p = p;
	}
	
	/**
	 * Return the permissions system where the permissions manager is currently hooked into
	 * @return permissions system type
	 */
	public PermissionsSystemType getUsedPermissionsSystemType() {
		return this.permsType;
	}
	
	/**
	 * Check if the permissions manager is currently hooked into any of the supported permissions systems
	 * @return false if there isn't any permissions system used
	 */
	public boolean isEnabled() {
		return !permsType.equals(PermissionsSystemType.NONE);
	}
	
	/**
	 * Setup and hook into the permissions systems
	 * @return the detected permissions system
	 */
	public PermissionsSystemType setup() {
		// Define the plugin manager
		final PluginManager pm = this.s.getPluginManager();
		
		// Reset used permissions system type
		permsType = PermissionsSystemType.NONE;
		
		// VAULT PERMISSIONS
		final Plugin vaultPlugin = pm.getPlugin("Vault");
		if (vaultPlugin != null && vaultPlugin.isEnabled()) {
			RegisteredServiceProvider<Permission> permissionProvider = this.s.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	            vaultPerms = permissionProvider.getProvider();
	            if(vaultPerms.isEnabled()) {
	            	permsType = PermissionsSystemType.VAULT;
	            	System.out.println("[" + p.getName() + "] Hooked into Vault Permissions!");
	    		    return permsType;
	            } else {
	            	System.out.println("[" + p.getName() + "] Not using Vault Permissions, Vault Permissions is disabled!");
	            }
	        }
		}
	    
	    // No recognized permissions system found
	    permsType = PermissionsSystemType.NONE;
	    System.out.println("[" + p.getName() + "] No supported permissions system found! Permissions disabled!");
	    
	    return PermissionsSystemType.NONE;
    }
	
	/**
	 * Check if the player has permission. If no permissions system is used, the player has to be OP
	 * @param p player
	 * @param permsNode permissions node
	 * @return true if the player is permitted
	 */
	public boolean hasPermission(Player p, String permsNode) {
		return hasPermission(p, permsNode, p.isOp());
	}
	
	/**
	 * Check if a player has permission
	 * @param player player
	 * @param permissionNode permission node
	 * @param def default if no permissions system is used
	 * @return true if the player is permitted
	 */
	public boolean hasPermission(Player p, String permsNode, boolean def) {
		if(!isEnabled()) {
			// No permissions system is used, return default
			return def;
		}
		
		switch (this.permsType) {
		case VAULT:
			// Vault
			return vaultPerms.has(p, permsNode);
			
		case NONE:
			// Not hooked into any permissions system, return default
			return def;
			
		default:
			// Something went wrong, return false to prevent problems
			return false;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getGroups(Player p) {
		if(!isEnabled()) {
			// No permissions system is used, return an empty list
			return new ArrayList<String>();
		}
		
		switch (this.permsType) {	
		case VAULT:
			// Vault
			return Arrays.asList(vaultPerms.getPlayerGroups(p));
	
		case NONE:
			// Not hooked into any permissions system, return an empty list
			return new ArrayList<String>();
			
		default:
			// Something went wrong, return an empty list to prevent problems
			return new ArrayList<String>();
		}
	}
	
	public enum PermissionsSystemType {
		NONE("None"),
		VAULT("Vault");
		
		public String name;
		
		PermissionsSystemType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
}
