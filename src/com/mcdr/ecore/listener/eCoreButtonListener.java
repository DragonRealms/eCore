package com.mcdr.ecore.listener;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcdr.ecore.eCore;

public class eCoreButtonListener implements Listener {	
	
	@EventHandler
	public void onButtonHit(PlayerInteractEvent event){
		if(event.getPlayer().getWorld().getName().equalsIgnoreCase("Area51")){
			if(event.hasBlock() && event.getClickedBlock().getType() == Material.STONE_BUTTON){
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					ArrayList<Location> locations = new ArrayList<Location>();
					locations.add(new Location(event.getClickedBlock().getLocation().getWorld(), -41D, 27D, -21D));
					locations.add(new Location(event.getClickedBlock().getLocation().getWorld(), -39D, 27D, -21D));
					
					for(Location loc: locations){
						Block b = loc.getBlock();
						try{
							Sign s = (Sign) b.getState();
							if(s.getLine(3).equalsIgnoreCase(ChatColor.DARK_RED+" Denied!")){
								s.setLine(3, ChatColor.DARK_GREEN+" Granted!");
							} else {
								s.setLine(3, ChatColor.DARK_RED+" Denied!");
							}
							s.update();
						} catch (ClassCastException e){
							eCore.logger.info("[eCore] Targeted block ("+b.getType()+") is not a sign.");
						}
					}
				}
			}			
		}
	}

}
