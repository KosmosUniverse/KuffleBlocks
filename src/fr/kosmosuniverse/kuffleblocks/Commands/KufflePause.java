package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.ActionBar;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;

public class KufflePause implements CommandExecutor {
	private KuffleMain km;
	
	public KufflePause(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kadminsave>");
		
		if (!player.hasPermission("kpause")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			km.logs.writeMsg(player, "You need to first add people with klist command and launch a game with kstart command.");
			return false;
		}
		
		if (!km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "Your game is already paused, you can resume it with kresume command.");
			return false;
		}
		
		for (GameTask gt : km.games) {
			gt.disable();
			ActionBar.sendRawTitle("{\"text\":\"Game Paused..\",\"bold\":true,\"color\":\"dark_purple\"}", gt.getPlayer());
			gt.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 10, false, false, false));
		}
		
		km.paused = true;
		
		return true;
	}

}
