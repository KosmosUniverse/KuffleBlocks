package fr.kosmosuniverse.kuffle.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class KuffleAddPlayer implements CommandExecutor {
	private KuffleMain km;

	public KuffleAddPlayer(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kadd")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			player.sendMessage("No game launched, you can add this player with klist command.");
			return false;
		} else if (!km.games.get(0).getEnable()) {
			player.sendMessage("No game launched, you can add this player with klist command.");
			return false;
		}
		
		if (args.length != 1) {
			player.sendMessage("This command takes 1 argument: the name of the player to add to the game.");
			return false;
		}
		
		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		boolean isHere = false;
		
		for (Player p : players) {
			if (p.getDisplayName().equals(args[0])) {
				km.games.add(new GameTask(km, p));
				isHere = true;
			}
		}
		
		if (!isHere) {
			player.sendMessage("Player [" + args[0] + "] does not exists.");
			return false;	
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getDisplayName().equals(args[0])) {
				if (km.config.getSaturation()) {
					gt.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
				}
				
				km.playerRank.put(gt.getPlayer().getDisplayName(), false);
				km.playersHeads.addItem(Utils.getHead(gt.getPlayer()));
				gt.setDeathLoc(null);
				
				Location spawn = gt.getPlayer().getLocation().getWorld().getSpawnLocation();
				
				gt.setSpawnLoc(spawn);
				spawn.add(0, -1, 0).getBlock().setType(Material.BEDROCK);
				
				gt.startRunnable();
				gt.enable();
				
				return true;
			}
		}
		
		return true;
	}
}
