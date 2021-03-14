package fr.kosmosuniverse.kuffle.Listeners;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;
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
		} else if (event.getView().getTitle() == "§8Players") {
			event.setCancelled(true);
			
			if (item.getType() == Material.PLAYER_HEAD) {
				if (!item.getItemMeta().getDisplayName().equals(player.getDisplayName())) {
					Player p = findPlayer(item.getItemMeta().getDisplayName());
					
					if (p != null) {
						if (player.getGameMode() != GameMode.SPECTATOR) {
							player.setGameMode(GameMode.SPECTATOR);
						}
						player.teleport(p);	
					}
				}
			}
		} else if (event.getView().getTitle().contains(" blocks ")) {
			for (String age : km.blocksInvs.keySet()) {
				if (event.getView().getTitle().contains(age)) {
					event.setCancelled(true);

					ArrayList<Inventory> tmpInvs = km.blocksInvs.get(age);
					
					if (tmpInvs.size() > 1) {
						int invIdx = tmpInvs.indexOf(current);
						
						if (item.getItemMeta().getDisplayName().equals("<- Previous")) {
							player.openInventory(tmpInvs.get(invIdx - 1));
						} else if (item.getItemMeta().getDisplayName().equals("Next ->")) {
							player.openInventory(tmpInvs.get(invIdx + 1));
						}
					}
				}
			}
		}
	}
	
	private Player findPlayer(String name) {
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getDisplayName().equals(name)) {
				return gt.getPlayer();
			}
		}
		
		return null;
	}
}
