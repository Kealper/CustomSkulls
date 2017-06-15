package com.timvisee.customskulls.listener;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.timvisee.customskulls.CustomSkulls;
import com.timvisee.customskulls.until.CustomSkullsUtility;

public class CustomSkullsEntityListener implements Listener {
	public static CustomSkulls plugin;

	public CustomSkullsEntityListener(CustomSkulls instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		
		if(event.getEntityType().equals(EntityType.SKELETON) ||
				event.getEntityType().equals(EntityType.WITHER) ||
				event.getEntityType().equals(EntityType.ZOMBIE) ||
				event.getEntityType().equals(EntityType.CREEPER)) {
			
			String worldName = w.getName();
			String entityName = e.getType().getName();
			
			// Check if the settings thing is enabled
			if(isConfigSettingEnabled(plugin.getConfig(), worldName, entityName, false)) {
				// The setting thing is enabled
				
				// Cancel the skull droping from wither skeletons
				if(e instanceof Skeleton) {
					Skeleton skel = (Skeleton) e;
					
					if(skel.getSkeletonType().equals(SkeletonType.WITHER))
						for(ItemStack entry : event.getDrops())	
							if((entry.getTypeId() == 144 || entry.getTypeId() == 397) && entry.getData().getData() == (byte) 1)
								entry.setAmount(0);
				}
				
				// Should a skull be droped?
				if(getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropSkull", false)) {
					// Get the drop chance
					int chance = getConfigSettingInt(plugin.getConfig(), worldName, entityName, "dropChance", 40);
					
					// Should a skull be droped this time?
					Random rand = new Random();
					int probability = rand.nextInt(101);
				 	boolean dropSkull = (probability <= chance);
					
					// If a skull should be droped, drop one
					if(dropSkull)
						dropSkull(1, e.getLocation(), e);
				}
			}
		}	
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {		
		String worldName = e.getEntity().getWorld().getName();
		String entityName = "Player";
		String playerName = e.getEntity().getName();
		
		// Check if the settings thing is enabled
		if(isConfigSettingEnabled(plugin.getConfig(), worldName, entityName, false)) {
			// The setting thing is enabled
			
			// Cancel the skull drop
			for(ItemStack entry : e.getDrops())	
				if(entry.getTypeId() == 144 || entry.getTypeId() == 397)
					entry.setAmount(0);
			
			// Should a skull be droped?
			if(getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropEntitySkull", false)) {
				// Get the drop chance
				int chance = getConfigSettingInt(plugin.getConfig(), worldName, entityName, "dropChance", 40);
				
				// Should a skull be droped this time?
				Random rand = new Random();
				int probability = rand.nextInt(101);
			 	boolean dropSkull = (probability <= chance);
				boolean dropPlayerSkull = getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropPlayerSkull", true);
				
				// If a skull should be dropped, drop one
				if(dropSkull)
					if(!dropPlayerSkull)
						dropSkull(1, e.getEntity());
					else
						addPlayerSkullDrop(1, e, playerName);
			}
		}
	}
	
	public void dropSkull(int amount, Entity e) {
		World w = e.getWorld();
		Location loc = e.getLocation();
		
		w.dropItemNaturally(loc, CustomSkullsUtility.getSkullItemStack(amount, e));
	}
	
	public void dropSkull(int amount, Location loc, Entity e) {
		World w = e.getWorld();
		
		w.dropItemNaturally(loc, CustomSkullsUtility.getSkullItemStack(amount, e));
	}
	
	public void addPlayerSkullDrop(int amount, PlayerDeathEvent e, String playerName) {
		e.getDrops().add(CustomSkullsUtility.getSkullItemStack(amount, playerName));
	}
	
	public boolean isConfigSettingEnabled(Configuration c, String worldName, String entityName, boolean def) {
		// The configuration file may not be null
		if(c == null)
			return def;
		
		return (c.getBoolean("drops.worlds." + worldName + "." + entityName + ".enabled",
				c.getBoolean("drops.global." + entityName + ".enabled", def)));
	}
	
	public boolean getConfigSettingBoolean(Configuration c, String worldName, String entityName, String node, boolean def) {
		// The configuration file may not be null
		if(c == null)
			return def;
		
		return (c.getBoolean("drops.worlds." + worldName + "." + entityName + "." + node,
				c.getBoolean("drops.global." + entityName + "." + node, def)));
	}
	
	public int getConfigSettingInt(Configuration c, String worldName, String entityName, String node, int def) {
		// The configuration file may not be null
		if(c == null)
			return def;
		
		return (c.getInt("drops.worlds." + worldName + "." + entityName + "." + node,
				c.getInt("drops.global." + entityName + "." + node, def)));
	}
}
