package fr.kosmosuniverse.kuffle.crafts;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ACrafts {
	protected String name;
	protected ItemStack item;
	
	protected abstract Inventory getInventoryRecipe();
	
	public String getName() {
		return (name);
	}
	
	public ItemStack getItem() {
		return (item);
	}
}
