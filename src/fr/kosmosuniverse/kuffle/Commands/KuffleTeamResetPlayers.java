package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleTeamResetPlayers implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamResetPlayers(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteam-reset-players")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
				
		if (args.length != 1) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			sender.sendMessage("The team :<" + args[0] + "> does not exist, please choose another name.");
		} else {
			km.teams.getTeam(args[0]).players.clear();
		}
		
		return true;
	}

}
