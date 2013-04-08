package com.mcdr.ecore.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.mcdr.corruption.CorruptionAPI;
import com.mcdr.ecore.CheckPointManager;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eCoreStage;

public class eCoreRedstoneListener implements Listener {
	private int[] rsLoc = {-37, 36, 35};
	private ArrayList<Location> signLocations = new ArrayList<Location>();
	private int[] giantRsLoc = {-217, 27, -19};
	private Location giantSpawnLoc;
	private boolean giantSpawned = false;
	
	public eCoreRedstoneListener(){
		signLocations.add(new Location(Bukkit.getWorld("Area51"), -36D, 40D, 34D));
		signLocations.add(new Location(Bukkit.getWorld("Area51"), -36D, 40D, 36D));
		signLocations.add(new Location(Bukkit.getWorld("Area51"), -38D, 40D, 36D));
		giantSpawnLoc = new Location(Bukkit.getWorld("Area51"), -211.0, 27.0, -27.0);
	}
	
	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent e){	
		if(e.getBlock().getWorld().getName().equalsIgnoreCase("Area51")){
			Location uLoc = e.getBlock().getLocation();
			if(uLoc.getBlockX() == rsLoc[0] && uLoc.getBlockY() == rsLoc[1] && uLoc.getBlockZ() == rsLoc[2]){
				if (e.getOldCurrent()==0 && e.getNewCurrent()>0){
					for(Location loc: signLocations){
						loc.setWorld(e.getBlock().getWorld());
						Block b = loc.getBlock();
						try{
							Sign s = (Sign) b.getState();
							s.setLine(3, ChatColor.DARK_RED+" Denied!");
							s.update();
						} catch (ClassCastException err){
							eCore.logger.info("[eCore] Targeted block ("+b.getType()+") is not a sign.");
						}
					}
				} else if (e.getOldCurrent()>0 && e.getNewCurrent()==0){
					for(Location loc: signLocations){
						loc.setWorld(e.getBlock().getWorld());
						Block b = loc.getBlock();
						try{
							Sign s = (Sign) b.getState();
							s.setLine(3, ChatColor.DARK_GREEN+" Granted!");
							s.update();
						} catch (ClassCastException err){
							eCore.logger.info("[eCore] Targeted block ("+b.getType()+") is not a sign.");
						}
					}
				}
			} else if(uLoc.getBlockX() == giantRsLoc[0] && uLoc.getBlockY() == giantRsLoc[1] && uLoc.getBlockZ() == giantRsLoc[2]){
				giantSpawnLoc.setWorld(e.getBlock().getWorld());
				if (e.getOldCurrent()>0 && e.getNewCurrent()==0 && CheckPointManager.state == eCoreStage.COOPERATION  && !giantSpawned){
					System.out.println("Ok");
					giantSpawnLoc.setWorld(e.getBlock().getWorld());
					if(eCore.corruptionEnabled){
						List<String> giants = CorruptionAPI.getBossNames().get(EntityType.GIANT);
						if(giants.size()>0){
							giantSpawned = CorruptionAPI.spawnBoss(giantSpawnLoc.getBlock().getLocation(), giants.get(0))!=null;
						}
					}
					if(!giantSpawned){
						giantSpawnLoc.getWorld().spawnEntity(giantSpawnLoc, EntityType.GIANT);
						giantSpawned = true;
					}
				}
			}
		}
	}
}
