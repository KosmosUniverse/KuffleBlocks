package fr.kosmosuniverse.kuffle.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Crafts.ACrafts;

public class InventoryRecipeListener implements Listener {
	private KuffleMain km;
	
	public InventoryRecipeListener(KuffleMain _km) {
		this.km = _km;
	}
	
	@EventHandler
	public void onItemClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		
		if (item == null) {
			return;
		}
		
		if (event.getView().getTitle() == "§8AllCustomCrafts") {
			event.setCancelled(true);
			for (ACrafts craft : km.crafts.getRecipeList()) {
				if (craft.getItem().equals(item)) {
					player.openInventory(craft.getInventoryRecipe());
					return;
				}
			}
		} else if (isKcraftInventory(event.getView().getTitle())) {
			event.setCancelled(true);
			if (item.getItemMeta().getDisplayName().equals("<- Back")) {
				player.openInventory(km.crafts.getAllCraftsInventory());
			}
		}
	}
	
	private boolean isKcraftInventory(String invName) {
		for (ACrafts craft : km.crafts.getRecipeList()) {
			String tmpCraft = "§8" + craft.getName();

			if (invName.equals(tmpCraft)) {
				return true;
			}
		}
		return false;
	}
}
