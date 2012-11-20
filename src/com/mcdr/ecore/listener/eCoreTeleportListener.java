package com.mcdr.ecore.listener;

import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.mcdr.ecore.eCore;

public class eCoreTeleportListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent e){
		Player player = e.getPlayer();
		if(player.getName()==eCore.name){
			World world = player.getWorld();
			world.playEffect(player.getLocation(), Effect.SMOKE, 0);
		}
	}
}
