package fr.kosmosuniverse.kuffle.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.MultiBlock.AMultiblock;
import fr.kosmosuniverse.kuffle.MultiBlock.ActivationType;

public class PlayerMove implements Listener {
	private KuffleMain km;
	
	public PlayerMove(KuffleMain _km) {
		km = _km;
	}
	
	@EventHandler
	public void onPauseEvent(PlayerMoveEvent event) {
		if (km.paused) {
//			event.setTo(event.getFrom());
			event.setCancelled(true);
			return ;
		}
		
		Player player = event.getPlayer();
		AMultiblock multiBlock;
		
		if (player.getLocation().add(0, -1, 0).getBlock().getType() == Material.OBSIDIAN) {
			if ((multiBlock = km.multiBlock.findMultiBlockByName("End Teleporter")) != null) {
				if (multiBlock.getMultiblock().checkMultiBlock(player.getLocation().add(0, -1, 0), player)) {
					multiBlock.onActivate(km, player, ActivationType.ACTIVATE);
				}
			}
		} else if (player.getLocation().add(0, -1, 0).getBlock().getType() == Material.END_PORTAL_FRAME) {
			if ((multiBlock = km.multiBlock.findMultiBlockByName("OverWorld Teleporter")) != null) {
				if (multiBlock.getMultiblock().checkMultiBlock(player.getLocation().add(0, -1, 0), player)) {
					multiBlock.onActivate(km, player, ActivationType.ACTIVATE);
				}
			}
		}
	}
	
	@EventHandler
	public void onCorePlacedEvent(BlockPlaceEvent event) {
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