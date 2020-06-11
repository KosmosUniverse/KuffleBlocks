package fr.kosmosuniverse.kuffle.crafts;

import java.util.ArrayList;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class ManageCrafts {
	private ArrayList<ACrafts> recipes = new ArrayList<ACrafts>();
	
	public ManageCrafts(KuffleMain _km) {
		if (!_km.getConfig().getBoolean("game_settings.custom_crafts")) {
			return;
		}
		
		recipes.add(new RedSand(_km));
		recipes.add(new MossyCobblestone(_km));
		recipes.add(new MossyStoneBrick(_km));
	}
	
	public ArrayList<ACrafts> getRecipeList() {
		return (recipes);
	}
}
