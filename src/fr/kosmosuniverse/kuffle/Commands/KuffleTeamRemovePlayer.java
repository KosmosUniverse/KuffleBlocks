package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;
public class KuffleTeamRemovePlayer implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamRemovePlayer(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteam-remove-player")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
				
		if (args.length != 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			sender.sendMessage("Team <" + args[0] + "> does not exist, please choose another name.");
			return true;
		}
		
		if (!km.teams.getTeam(args[0]).hasPlayer(args[1])) {
			sender.sendMessage("This player is not in this Team.");
			return true;
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getDisplayName().equals(args[1])) {
				km.teams.removePlayer(args[0], gt.getPlayer());
				sender.sendMessage("Player <" + args[1] + "> was removed from team <" + args[0] + ">.");
				
				return true;
			}
		}
		
		return true;
	}

}
