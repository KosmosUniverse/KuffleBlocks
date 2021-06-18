package fr.kosmosuniverse.kuffleblocks.Crafts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class Mycelium extends ACrafts {
	MaterialChoice mc;
	
	public Mycelium(KuffleMain _km) {
		name = "Mycelium";
		
		recipe = new ShapelessRecipe(new NamespacedKey(_km, name), new ItemStack(Material.MYCELIUM));
		
		ArrayList<Material> champs = new ArrayList<Material>();
		
		champs.add(Material.BROWN_MUSHROOM);
		champs.add(Material.RED_MUSHROOM);
		champs.add(Material.CRIMSON_FUNGUS);
		champs.add(Material.WARPED_FUNGUS);
		
		mc = new MaterialChoice(champs);
		
		((ShapelessRecipe) recipe).addIngredient(Material.DIRT);
		((ShapelessRecipe) recipe).addIngredient(mc);
		
		item = new ItemStack(Material.MYCELIUM);
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
				inv.setItem(i, new ItemStack(Material.DIRT));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.BROWN_MUSHROOM));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.MYCELIUM));
			} else if (i == 5 || i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
