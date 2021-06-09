package fr.kosmosuniverse.kuffleblocks.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.MultiBlock.AMultiblock;
import fr.kosmosuniverse.kuffleblocks.MultiBlock.ActivationType;

public class PlayerMove implements Listener {
	private KuffleMain km;
	
	public PlayerMove(KuffleMain _km) {
		km = _km;
	}
	
	@EventHandler
	public void onActivateMultiBlockEvent(PlayerMoveEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Player player = event.getPlayer();
		AMultiblock multiBlock;
		
		if (player.getLocation().add(0, -1, 0).getBlock().getType() == Material.OBSIDIAN) {
			if ((multiBlock = km.multiBlock.findMultiBlockByCore(Material.OBSIDIAN)) != null) {
				if (multiBlock.getMultiblock().checkMultiBlock(player.getLocation().add(0, -1, 0), player)) {
					multiBlock.onActivate(km, player, ActivationType.ACTIVATE);
				}
			}
		} else if (player.getLocation().add(0, -1, 0).getBlock().getType() == Material.END_PORTAL_FRAME) {
			if ((multiBlock = km.multiBlock.findMultiBlockByCore(Material.END_PORTAL_FRAME)) != null) {
				if (multiBlock.getMultiblock().checkMultiBlock(player.getLocation().add(0, -1, 0), player)) {
					multiBlock.onActivate(km, player, ActivationType.ACTIVATE);
				}
			}
		}
	}
	
	@EventHandler
	public void onCorePlacedEvent(BlockPlaceEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		AMultiblock multiBlock;
		
		if ((multiBlock = km.multiBlock.findMultiBlockByCore(block.getType())) != null) {
			if (multiBlock.getMultiblock().checkMultiBlock(block.getLocation(), player)) {
				multiBlock.onActivate(km, player, ActivationType.ASSEMBLE);
			}
		}
	}
}
