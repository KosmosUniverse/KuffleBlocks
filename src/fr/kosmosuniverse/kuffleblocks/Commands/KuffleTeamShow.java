package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

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
		
		km.logs.logMsg(player, "achieved command <kteam-show>");
		
		if (!player.hasPermission("kteam-show")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (args.length > 1) {
			return false;
		}
		
		if (args.length == 0) {
			km.logs.writeMsg(player, km.teams.toString());
		} else if (args.length == 1) {
			if (km.teams.hasTeam(args[0])) {
				km.logs.writeMsg(player, km.teams.printTeam(args[0]));
			} else {
				km.logs.writeMsg(player, "This team does not exists, please choose another one.");
			}
		}
		
		return true;
	}

}
