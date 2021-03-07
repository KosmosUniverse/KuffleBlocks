package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleTeamDelete implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamDelete(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteam-delete")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
				
		if (args.length != 1) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			sender.sendMessage("Team <" + args[0] + "> does not exist, please choose another name.");
		} else {
			km.teams.deleteTeam(args[0]);
			
			sender.sendMessage("Team <" + args[0] + "> was deleted.");
		}
		
		return true;
	}

}
