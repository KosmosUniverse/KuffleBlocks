package fr.kosmosuniverse.kuffle.crafts;

import org.bukkit.inventory.Inventory;

public abstract class ACrafts {
	protected String name;
	
	protected abstract Inventory getInventoryRecipe();
	
	public String getName() {
		return (name);
	}
}
