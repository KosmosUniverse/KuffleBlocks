package fr.kosmosuniverse.kuffle.Crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class RedSand extends ACrafts{
	private ShapelessRecipe redSand;
	
	public RedSand(KuffleMain _km) {
		name = "RedSand";
		redSand = new ShapelessRecipe(new NamespacedKey(_km, name), new ItemStack(Material.RED_SAND));
		
		redSand.addIngredient(Material.SAND);
		redSand.addIngredient(Material.RED_DYE);
		
		_km.getServer().addRecipe(redSand);

		item = new ItemStack(Material.RED_SAND);
	}
	
	public Inventory getInventoryRecipe() {
		Inventory inv = Bukkit.createInventory(null,  27, "§8" + name);
		
		for (int i = 0; i < 27; i++) {
			if (i == 3) {
				inv.setItem(i, new ItemStack(Material.SAND));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.RED_DYE));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.RED_SAND));
			} else if (i == 5 || i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
			} else {
				inv.setItem(i, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
			}
		}
		
		return (inv);
	}
}
