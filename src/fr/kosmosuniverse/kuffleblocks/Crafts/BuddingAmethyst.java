package fr.kosmosuniverse.kuffleblocks.Crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class BuddingAmethyst extends ACrafts {
	public BuddingAmethyst(KuffleMain _km) {
		name = "BuddingAmethyst";
		
		recipe = new ShapedRecipe(new NamespacedKey(_km, name), new ItemStack(Material.BUDDING_AMETHYST));
		
		
		((ShapedRecipe) recipe).shape("ARA", "RAR", "ARA");
		((ShapedRecipe) recipe).setIngredient('A', Material.AMETHYST_BLOCK);
		
		item = new ItemStack(Material.BUDDING_AMETHYST);
	}
	
	public Inventory getInventoryRecipe() {
		Inventory inv = Bukkit.createInventory(null,  27, "§8" + name);
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
			} else if (i == 3 || i == 5 || i == 13 || i == 21 || i == 23) {
				inv.setItem(i, new ItemStack(Material.AMETHYST_BLOCK));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.BUDDING_AMETHYST));
			} else if (i == 4 || i == 12 || i == 13 || i == 22) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
