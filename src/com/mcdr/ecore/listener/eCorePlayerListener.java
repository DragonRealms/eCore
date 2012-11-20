package com.mcdr.ecore.listener;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.mcdr.ecore.eCore;
import com.mcdr.ecore.task.TaskManager;


public class eCorePlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.startFlameEffect();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.stopFlameEffect();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onTeleport(PlayerTeleportEvent e){
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			World world = player.getWorld();
			Location loc = player.getLocation();
			Random rand = new Random();
			for(int i = 0;i<100;i++)
				for(double j = 0.0; j <= 2.0; j+=0.25)
					world.playEffect(loc.clone().add(0.0, j, 0.0),Effect.SMOKE, i, 16);
		}
	}
}
