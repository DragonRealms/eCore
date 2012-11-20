package com.mcdr.ecore.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcdr.ecore.eCore;

public class eCoreButtonListener implements Listener {	
	
	public eCoreButtonListener(eCore plugin) {
			
	}
	
	@EventHandler
	public void onButtonHit(PlayerInteractEvent event){
		//if(event.getPlayer().getLocation().getWorld().getName().equals("Area51")){
			if(event.getClickedBlock().getType() == Material.STONE_BUTTON){
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Location loc = new Location(event.getClickedBlock().getLocation().getWorld(), 82D,63D,283D);
					
					eCore.logger.info(Integer.toString(loc.getBlock().getTypeId()));
					Sign s = (Sign) loc.getBlock().getState();
					
					s.setLine(4, "Granted" );
				}						
			}			
		}	
	//}

}
