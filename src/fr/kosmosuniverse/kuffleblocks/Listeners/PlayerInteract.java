package fr.kosmosuniverse.kuffleblocks.Listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class PlayerInteract implements Listener {
	private KuffleMain km;
	private HashMap<Location, String> shulkers = new HashMap<Location, String>();
	
	public PlayerInteract(KuffleMain _km) {
		km = _km;
	}
	
	@EventHandler
	public void onPlaceShulker(BlockPlaceEvent event) {
		if (!km.gameStarted || !km.config.getPassive()) {
			return ;
		}
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location location = block.getLocation();
		
		if (block.getType().name().toLowerCase().contains("shulker_box")) {
			shulkers.put(location, player.getName());
		}
	}
	
	@EventHandler
	public void onBreakShulker(BlockBreakEvent event) {
		if (!km.gameStarted || !km.config.getPassive()) {
			return ;
		}
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location location = block.getLocation();
		
		if (!shulkers.containsKey(location)) {
			return;
		}
		
		String placerName = shulkers.get(location);
		
		if (!placerName.equals(player.getName()) &&
				(!km.config.getTeam() ||
						!km.games.get(player.getName()).getTeamName().equals(km.games.get(placerName).getTeamName()))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreakSign(BlockBreakEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Block block = event.getBlock();
		
		if (block.getType() != Material.OAK_SIGN) {
			return;
		}
		
		Sign sign = (Sign) block.getState();
		
		if (sign == null ||
				!sign.getLine(0).equals("[KuffleItems]") ||
				!sign.getLine(1).equals("Here dies") ||
				!km.games.containsKey(sign.getLine(2))) {
			return ;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteractShulker(PlayerInteractEvent event) {
		if (!km.gameStarted || !km.config.getPassive()) {
			return ;
		}
		
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		
		if (action == Action.RIGHT_CLICK_BLOCK && block != null) {
			if (block.getType().name().toLowerCase().contains("shulker_box")) {
				if (!shulkers.containsValue(player.getName()) &&
						(!km.config.getTeam() ||
								!km.games.get(player.getName()).getTeamName().equals(km.games.get(shulkers.get(block.getLocation())).getTeamName()))) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Entity tmpDamager = event.getDamager();	
		Entity tmpDamagee = event.getEntity();
		
		if (!(tmpDamager instanceof Player) || !(tmpDamagee instanceof Player)) {
			return;
		}
		
		Player damager = (Player) tmpDamager;
		Player damagee = (Player) tmpDamagee;
		
		if (!km.games.containsKey(damager.getName()) || !km.games.containsKey(damagee.getName())) {
			return ;
		}
		
		if (km.config.getPassive()) {
			event.setCancelled(true);
			
			return ;
		}
		
		if (km.config.getTeam()) {
			if (km.games.get(damager.getName()).getTeamName().equals(km.games.get(damagee.getName()).getTeamName())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onFireWorkThrow(PlayerInteractEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		ItemStack item;
		Action action = event.getAction();
		Player player = event.getPlayer();

		if (!km.games.containsKey(player.getName())) {
			return ;
		}
		
		if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
			return ;
		}
		
		if (event.getItem() != null && event.getItem().getType() == Material.FIREWORK_ROCKET) {
			item = event.getItem();
			
			if (item.getAmount() == 1) {
				item.setAmount(64);
				player.getInventory().setItemInMainHand(item);
			}
		} else if (player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() == Material.FIREWORK_ROCKET) {
			item = player.getInventory().getItemInOffHand();
			
			if (item.getAmount() == 1) {
				item.setAmount(64);
				player.getInventory().setItemInOffHand(item);
			}
		} else {
			return;
		}
	}
}
