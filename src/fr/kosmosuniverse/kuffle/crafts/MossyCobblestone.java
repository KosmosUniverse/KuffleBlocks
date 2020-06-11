package fr.kosmosuniverse.kuffle.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class MossyCobblestone extends ACrafts {
	private ShapelessRecipe mossyCobblestone;
	
	public MossyCobblestone(KuffleMain _km) {
		name = "MossyCobblestone";
		
		mossyCobblestone = new ShapelessRecipe(new NamespacedKey(_km, name), new ItemStack(Material.MOSSY_COBBLESTONE));
		
		mossyCobblestone.addIngredient(Material.COBBLESTONE);
		mossyCobblestone.addIngredient(Material.GRASS);
		
		_km.getServer().addRecipe(mossyCobblestone);
		
		item = new ItemStack(Material.MOSSY_COBBLESTONE);
	}
	
	public Inventory getInventoryRecipe() {
		Inventory inv = Bukkit.createInventory(null,  27, "§8" + name);
		
		for (int i = 0; i < 27; i++) {
			if (i == 3) {
				inv.setItem(i, new ItemStack(Material.COBBLESTONE));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.GRASS));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.MOSSY_COBBLESTONE));
			} else if (i == 5 || i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
			} else {
				inv.setItem(i, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
			}
		}
		
		return (inv);
	}
}
