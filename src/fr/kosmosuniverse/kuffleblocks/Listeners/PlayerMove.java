package fr.kosmosuniverse.kuffleblocks.Listeners;

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
	private int xpAmount;
	
	public PlayerMove(KuffleMain _km) {
		km = _km;
		xpAmount = 10;
	}
	
	@EventHandler
	public void onActivateMultiBlockEvent(PlayerMoveEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Player player = event.getPlayer();
		AMultiblock multiBlock;
		
		if (!km.multiBlock.cores.containsKey(player.getLocation().add(0, -1, 0).getBlock().getType())) {
			return ;
		}
		
		String name = km.multiBlock.cores.get(player.getLocation().add(0, -1, 0).getBlock().getType());
		
		multiBlock = km.multiBlock.findMultiBlockByName(name);
		
		if (multiBlock != null && multiBlock.getMultiblock().checkMultiBlock(player.getLocation().add(0, -1, 0), player)) {
			if (multiBlock.getName().equals("OverWorldTeleporter") && player.getLevel() >= xpAmount) {
				player.setLevel(player.getLevel() - xpAmount);
				xpAmount = xpAmount < 2 ? 2 : xpAmount - 2;
				multiBlock.onActivate(km, player, ActivationType.ACTIVATE);
			} else if (!multiBlock.getName().equals("OverWorldTeleporter")) {
				multiBlock.onActivate(km, player, ActivationType.ACTIVATE);
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
		
		String name = km.multiBlock.cores.get(block.getType());
		
		multiBlock = km.multiBlock.findMultiBlockByName(name);
		
		if (multiBlock != null && multiBlock.getMultiblock().checkMultiBlock(block.getLocation(), player)) {
			multiBlock.onActivate(km, player, ActivationType.ASSEMBLE);
		}
	}
}
