package fr.kosmosuniverse.kuffleblocks.Crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class MossyStoneBrick extends ACrafts {	
	public MossyStoneBrick(KuffleMain _km) {
		name = "MossyStoneBrick";
		recipe = new ShapelessRecipe(new NamespacedKey(_km, name), new ItemStack(Material.MOSSY_STONE_BRICKS));
		
		((ShapelessRecipe) recipe).addIngredient(Material.STONE_BRICKS);
		((ShapelessRecipe) recipe).addIngredient(Material.GRASS);
	
		item = new ItemStack(Material.MOSSY_STONE_BRICKS);
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
			} else if (i == 3) {
				inv.setItem(i, new ItemStack(Material.STONE_BRICKS));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.GRASS));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.MOSSY_STONE_BRICKS));
			} else if (i == 5 || i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
