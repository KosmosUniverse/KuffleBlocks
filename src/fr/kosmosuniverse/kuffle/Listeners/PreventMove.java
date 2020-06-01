package fr.kosmosuniverse.kuffle.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class PreventMove implements Listener {
	private KuffleMain km;
	
	public PreventMove(KuffleMain _km) {
		km = _km;
	}
	
	@EventHandler
	public void onPauseEvent(PlayerMoveEvent event) {
		if (km.paused) {
//			event.setTo(event.getFrom());
			event.setCancelled(true);
		}
	}
}
