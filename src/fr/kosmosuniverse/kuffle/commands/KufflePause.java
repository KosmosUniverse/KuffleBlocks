package fr.kosmosuniverse.kuffle.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KufflePause implements CommandExecutor {
	private KuffleMain km;
	
	public KufflePause(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kpause")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			p.sendMessage("You need to first add people with klist command and launch a game with kstart command.");
			return false;
		}
		
		if (!km.games.get(0).getEnable()) {
			p.sendMessage("Your game is already paused, you can resume it with kresume command.");
			return false;
		}
		
		for (GameTask gt : km.games) {
			gt.disable();
			gt.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 10, false, false, false));
		}
		
		Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"Game Paused...\",\"bold\":true,\"color\":\"dark_purple\"}");
		km.paused = true;
		
		return true;
	}

}
