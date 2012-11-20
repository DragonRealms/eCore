package com.mcdr.ecore.listener;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.mcdr.ecore.eCore;

public class eCoreRedstoneListener implements Listener {

	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent event){
		if(true){			
				if(event.getNewCurrent() > 0){
					if(isChangeableSign(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ())){
						int hoi = event.getBlock().getLocation().getBlockX();
						Location loc = event.getBlock().getLocation();
						loc.setX(hoi + 1);
						Sign s = (Sign) loc.getBlock().getState();
						s.setLine(4, "Granted");
				}
			}
		}
	}
	
	private boolean isChangeableSign(int x, int y, int z){
		return true;
	}

}
