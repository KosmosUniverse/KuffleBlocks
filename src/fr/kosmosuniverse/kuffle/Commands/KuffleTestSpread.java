package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.SpreadPlayer;

public class KuffleTestSpread implements CommandExecutor {
	private KuffleMain km;

	public KuffleTestSpread(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		SpreadPlayer.spreadPlayers(player, (double) km.config.getSpreadDistance(), km.config.getSpreadRadius(), null, null);
		
		return true;
	}
}
