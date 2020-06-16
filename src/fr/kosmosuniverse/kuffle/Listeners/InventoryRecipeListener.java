package fr.kosmosuniverse.kuffle.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Crafts.ACrafts;
import fr.kosmosuniverse.kuffle.MultiBlock.AMultiblock;

public class InventoryRecipeListener implements Listener {
	private KuffleMain km;
	
	public InventoryRecipeListener(KuffleMain _km) {
		this.km = _km;
	}
	
	@EventHandler
	public void onItemClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		Inventory current = event.getClickedInventory();
		AMultiblock multiBlock;
		ACrafts craft;
		Inventory inv;
		
		if (item == null) {
			return;
		}
		
		System.out.println(event.getView().getTitle());
		if (event.getView().getTitle() == "§8AllCustomCrafts") {
			event.setCancelled(true);
			if ((craft = km.crafts.findCraftInventoryByItem(item.getType())) != null) {
				if ((inv = craft.getInventoryRecipe()) != null) {
					player.openInventory(inv);
				}
			}
		} else if (event.getView().getTitle() == "§8AllMultiBlocks") {
			event.setCancelled(true);
			if ((multiBlock = km.multiBlock.findMultiBlockByItem(item.getType())) != null) {
				if ((inv = multiBlock.getInventory(current, item, km.multiBlock.getAllMultiBlocksInventory(), true)) != null) {
					player.openInventory(inv);
				}
			}
		} else if ((craft = km.crafts.findCraftByInventoryName(event.getView().getTitle())) != null) {
			event.setCancelled(true);
			if (item.getItemMeta().getDisplayName().equals("<- Back")) {
				player.openInventory(km.crafts.getAllCraftsInventory());
			}
		} else if ((multiBlock = km.multiBlock.findMultiBlockByInventoryName(event.getView().getTitle())) != null) {
			event.setCancelled(true);
			if ((inv = multiBlock.getInventory(current, item, km.multiBlock.getAllMultiBlocksInventory(), false)) != null) {
				player.openInventory(inv);
			}
		}
	}
}
