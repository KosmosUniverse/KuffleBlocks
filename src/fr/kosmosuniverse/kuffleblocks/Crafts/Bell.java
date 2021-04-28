package fr.kosmosuniverse.kuffleblocks.Crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class Bell extends ACrafts {
	public Bell(KuffleMain _km) {
		name = "Bell-StoneCutter";
		
		recipe = new StonecuttingRecipe(new NamespacedKey(_km, name), new ItemStack(Material.BELL), Material.GOLD_BLOCK);
		
		item = new ItemStack(Material.BELL);
	}
	
	public Inventory getInventoryRecipe() {
		Inventory inv = Bukkit.createInventory(null,  27, "§8" + name);
		ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta itM = limePane.getItemMeta();
		
		itM.setDisplayName(" ");
		limePane.setItemMeta(itM);
		itM = redPane.getItemMeta();
		itM.setDisplayName("<- Back");
		redPane.setItemMeta(itM);
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 13) {
				inv.setItem(i, new ItemStack(Material.GOLD_BLOCK));
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.BELL));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
