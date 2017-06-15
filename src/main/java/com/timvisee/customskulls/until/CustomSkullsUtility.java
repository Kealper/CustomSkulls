package com.timvisee.customskulls.until;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CustomSkullsUtility {

	public static ItemStack getSkullItemStack(int amount, Entity e) {
		// Convert the entity type into a skull data value
		switch (e.getType()) {
		case SKELETON:
			switch(((Skeleton) e).getSkeletonType()){
			case NORMAL:
				return getSkullItemStack(amount, (byte) 0);
			case WITHER:
				return getSkullItemStack(amount, (byte) 1);			
			}
					
		case ZOMBIE:
			return getSkullItemStack(amount, (byte) 2);
		case PLAYER:
			return getSkullItemStack(amount, (byte) 3);
		case CREEPER:
			return getSkullItemStack(amount, (byte) 4);
		default:
			return null;
		}
	}
	
	public static ItemStack getSkullItemStack(int amount, String playerName) {
		ItemStack s = new ItemStack(397, amount);
    	s.setDurability((short)3);
    	SkullMeta meta = (SkullMeta)s.getItemMeta();
        meta.setOwner(playerName);
        s.setItemMeta(meta);
    	return s;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSkullItemStack(int amount, byte data) {
		amount = Math.max(0, amount);
		return (new ItemStack(397, amount, (short) 0, data));
	}
}
