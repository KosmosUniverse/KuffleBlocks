package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class KuffleTeamAffectPlayer implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamAffectPlayer(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteam-affect-player")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
				
		if (args.length != 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			sender.sendMessage("Team :<" + args[0] + "> does not exist, please choose another name.");
			return true;
		}
		
		if (km.teams.getTeam(args[0]).players.size() == km.config.getTeamSize()) {
			sender.sendMessage("This team is already full, please add him in another team or change team size with kconfig command.");
			return true;
		}
		
		if (!Utils.getPlayerNames(km.games).contains(args[1])) {
			sender.sendMessage("This player is not in the game, please add him in the game list with klist command.");
			return true;
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getDisplayName().equals(args[1])) {
				km.teams.affectPLayer(args[0], gt.getPlayer());
				
				sender.sendMessage("Player <" + args[1] + "> added to team <" + args[0] + ">.");
				
				return true;
			}
		}
		
		return true;
	}

}
