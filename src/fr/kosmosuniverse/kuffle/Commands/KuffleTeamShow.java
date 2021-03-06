package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleTeamShow implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamShow(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteam-show")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (args.length > 1) {
			return false;
		}
		
		if (args.length == 0) {
			player.sendMessage(km.teams.toString());
		} else if (args.length == 1) {
			if (km.teams.hasTeam(args[0])) {
				player.sendMessage(km.teams.printTeam(args[0]));
			} else {
				player.sendMessage("This team does not exists, please choose another one.");
			}
		}
		
		return true;
	}

}
