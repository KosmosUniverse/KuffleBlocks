package fr.kosmosuniverse.kuffleblocks.Crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class WeatheredCopper extends ACrafts {
	public WeatheredCopper(KuffleMain _km) {
		name = "WeatheredCopper";
		
		recipe = new ShapelessRecipe(new NamespacedKey(_km, name), new ItemStack(Material.WEATHERED_COPPER));
		
		((ShapelessRecipe) recipe).addIngredient(Material.EXPOSED_COPPER);
		((ShapelessRecipe) recipe).addIngredient(Material.WATER_BUCKET);
		((ShapelessRecipe) recipe).addIngredient(Material.WATER_BUCKET);
		
		item = new ItemStack(Material.WEATHERED_COPPER);
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
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.EXPOSED_COPPER));
			} else if (i == 3) {
				inv.setItem(i, new ItemStack(Material.WATER_BUCKET));
			} else if (i == 5) {
				inv.setItem(i, new ItemStack(Material.WATER_BUCKET));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.WEATHERED_COPPER));
			} else if (i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
