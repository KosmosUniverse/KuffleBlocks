package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class ManageTeams {
	private ArrayList<Team> teams = new ArrayList<Team>();

	public void createTeam(String name) {
		teams.add(new Team(name));
	}
	
	public void createTeam(String name, ChatColor color) {
		teams.add(new Team(name, color));
	}
	
	public boolean hasTeam(String team) {
		for (Team item : teams) {
			if (item.name.equals(team)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void deleteTeam(String name) {
		for (Team item : teams) {
			if (item.name.equals(name)) {
				item.players.clear();
				teams.remove(item);
				return ;
			}
		}
	}
	
	public void changeTeamColor(String name, ChatColor color) {
		for (Team item : teams) {
			if (item.name.equals(name)) {
				item.color = color;
				return ;
			}
		}
	}
	
	public void affectPlayer(String teamName, Player player) {
		for (Team item : teams) {
			if (item.name.equals(teamName)) {
				item.players.add(player);
				return ;
			}
		}
	}
	
	public void removePlayer(String teamName, Player player) {
		for (Team item : teams) {
			if (item.name.equals(teamName)) {
				item.players.remove(player);
				return ;
			}
		}
	}
	
	public boolean isInTeam(String player) {
		for (Team teamItem : teams) {
			if (teamItem.hasPlayer(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<String> getTeamColors() {
		ArrayList<String> teamColors = new ArrayList<String>();
		
		for (Team item : teams)  {
			teamColors.add(item.color.name());
		}
		
		return teamColors;
	}
	
	public int getMaxTeamSize() {
		int max = 0;
		
		for (Team teamItem : teams) {
			max = teamItem.players.size() < max ? max : teamItem.players.size();
		}
		
		return max;
	}
	
	public String findTeamByPlayer(String player) {
		for (Team teamItem : teams) {
			for (Player playerItem : teamItem.players) {
				if (playerItem.getDisplayName().equals(player)) {
					return teamItem.name;
				}
			}
		}
		
		return null;
	}
	
	public void resetAll() {
		for (Team item : teams) {
			item.players.clear();
		}
		
		teams.clear();
	}
	
	public String printTeam(String teamName) {
		for (Team item : teams) {
			if (item.name.equals(teamName)) {
				return item.toString();
			}
		}
		
		return null;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (teams.size() != 0) {
			for (int cnt = 0; cnt < teams.size(); cnt++) {
				sb.append(teams.get(cnt).toString());
				
				if (cnt < teams.size() - 1) {
					sb.append("\n");
				}
			}
		} else {
			sb.append("No Team, please add teams with kteam-create command before print.");
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String saveTeams() {
		JSONObject global = new JSONObject();
		
		for (Team item : teams) {
			JSONObject tmp = new JSONObject();
			JSONArray players = new JSONArray();
			
			tmp.put("color", item.color);
			
			for (Player p : item.players) {
				players.add(p.getDisplayName());
			}
			
			tmp.put("players", players);
			
			global.put(item.name, tmp);
		}
		
		return global.toString();
	}
	
	public void loadTeams(JSONObject global, ArrayList<GameTask> games) {
		for (Object key : global.keySet()) {
			String name = key.toString();
			JSONObject tmp = (JSONObject) global.get(key);
			ChatColor color = Utils.findChatColor((String) tmp.get("color"));
			JSONArray players = (JSONArray) tmp.get("players");
			
			createTeam(name, color);
			
			if (players != null) {
				for (Object obj : players) {
					Player p = Utils.getPlayerInList(games, (String) obj);
					affectPlayer(name, p);
				}
			}
		}
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public Team getTeam(String name) {
		for (Team item : teams) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		
		return null;
	}
}
