package com.mcdr.ecore.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.config.Config;

public class eCoreCreativeSignListener implements Listener {	
	
	private int x,y,z;

	public eCoreCreativeSignListener(eCore plugin) {
			x = Config.config.getInt("creativebutton.x", 0);
			y = Config.config.getInt("creativebutton.y", 0);
			z = Config.config.getInt("creativebutton.z", 0);
	}
	
	@EventHandler
	public void onCreativeButtonHit(PlayerInteractEvent event){
		//if(event.getPlayer().getLocation().getWorld().getName().equals("Area51")){
			if(event.getPlayer().getName().equals("BozeVogel") || event.getPlayer().getName().equals("WeirdWater")){			
				if(event.getClickedBlock().getType() == Material.STONE_BUTTON){
					if(isCreativeButton(event.getClickedBlock().getLocation())){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
							event.setCancelled(true);
							event.getPlayer().setGameMode(GameMode.SURVIVAL);
						}
						if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
							event.setCancelled(true);
							event.getPlayer().setGameMode(GameMode.CREATIVE);
						}
					}			
				}
			}
		//}
	}
	
	private boolean isCreativeButton(Location loc){
		if(loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z){
			return true;
		}
		return false;
	}

}
