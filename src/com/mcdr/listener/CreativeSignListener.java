package com.mcdr.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;

import com.mcdr.eCore;

public class CreativeSignListener implements Listener {

	public CreativeSignListener(eCore plugin) {
		
	}
	
	@EventHandler
	public void onCreativeSignHit(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {			
			if(event.getClickedBlock().getState() instanceof Button){
				event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "You hit a button");
			}
		}
	}

}
