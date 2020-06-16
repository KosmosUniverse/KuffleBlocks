package fr.kosmosuniverse.kuffle.Crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class RedSand extends ACrafts {
	
	public RedSand(KuffleMain _km) {
		name = "RedSand";
		recipe = new ShapedRecipe(new NamespacedKey(_km, name), new ItemStack(Material.RED_SAND, 8));
		
		((ShapedRecipe) recipe).shape("SSS", "SRS", "SSS");
		((ShapedRecipe) recipe).setIngredient('S', Material.SAND);
		((ShapedRecipe) recipe).setIngredient('R', Material.RED_DYE);

		item = new ItemStack(Material.RED_SAND);
	}
	
	public Inventory getInventoryRecipe() {
		Inventory inv = Bukkit.createInventory(null,  27, "�8" + name);
		ItemStack grayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta itM = grayPane.getItemMeta();
		
		itM.setDisplayName(" ");
		grayPane.setItemMeta(itM);
		itM = limePane.getItemMeta();
		itM.setDisplayName(" ");
		limePane.setItemMeta(itM);
		itM = redPane.getItemMeta();
		itM.setDisplayName("<- Back");
		redPane.setItemMeta(itM);
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 3 || i == 4 || i == 5 || i == 12 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(Material.SAND));
			} else if (i == 13) {
				inv.setItem(i, new ItemStack(Material.RED_DYE));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.RED_SAND, 8));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
