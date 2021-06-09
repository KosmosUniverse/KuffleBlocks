package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.ActionBar;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

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
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-pause>"));
		
		if (!player.hasPermission("kb-pause")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (!km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_NOT_LAUNCHED"));
			return false;
		}
		
		if (km.paused) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_ALREADY_PAUSED"));
			return false;
		}
		
		km.paused = true;
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).pause();
			ActionBar.sendRawTitle("{\"text\":\"" + Utils.getLangString(km, player.getName(), "GAME_PAUSED") + "..\",\"bold\":true,\"color\":\"dark_purple\"}", km.games.get(playerName).getPlayer());
			km.games.get(playerName).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 10, false, false, false));
		}
		
		return true;
	}

}
