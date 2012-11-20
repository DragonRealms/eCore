package com.mcdr.ecore.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mcdr.ecore.eCore;
import com.mcdr.ecore.task.TaskManager;


public class eCorePlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (player.getName() == eCore.name){
			TaskManager.startFlameEffect();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (player.getName() == eCore.name){
			TaskManager.stopFlameEffect();
		}
	}
}
