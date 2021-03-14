package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.SpreadPlayer;

public class KuffleTest implements CommandExecutor {
	private KuffleMain km;

	public KuffleTest(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		
		if (args.length == 0) {
			SpreadPlayer.spreadPlayers(player, (double) km.config.getSpreadDistance(), km.config.getSpreadRadius(), null, null);	
		} else if (args.length == 1) {
			player.sendMessage("Highest block is " + player.getWorld().getHighestBlockYAt(player.getLocation()));
		}
		
		return true;
	}
}
