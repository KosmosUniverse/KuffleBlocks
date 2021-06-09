package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleStop implements CommandExecutor {
	private KuffleMain km;

	public KuffleStop(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-stop>"));
		
		if (!player.hasPermission("kb-stop")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (!km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_NOT_LAUNCHED"));
			return false;
		}
		
		for (String playerName : km.games.keySet()) {
			for (PotionEffect pe : km.games.get(playerName).getPlayer().getActivePotionEffects()) {
				km.games.get(playerName).getPlayer().removePotionEffect(pe.getType());
			}
			
			km.games.get(playerName).resetBar();
		}

		Utils.removeTemplates(km);
		km.scores.clear();
		
		km.teams.resetAll();
		
		km.games.clear();
		km.loop.kill();
		
		km.gameStarted = false;
		km.paused = false;
		km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_STOPPED"));
		
		return true;
	}

}
