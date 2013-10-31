package com.mcdr.ecore.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import com.mcdr.ecore.GlobalDataManager;
import com.mcdr.ecore.eCore;

public class eCoreCheckpointListener implements Listener{
	private int[][] coords = {
				{   2, 60,  58}, //Armory:0
				{- 37, 60, -47}, //Headquarters:1
				{- 42, 28, -50}, //Cookie:2
				{   2, 28, -69}, //Darkness:3
				{- 29, 27, -96}, //Proud of subject:4
				{- 21, 27, -87}, //Survivor:5
				{- 41, 46, - 3}, //Lava Everywhere:6
				{- 95, 40,  41}, //Mobs Everywhere:7
				{- 82, 28,  62}, //Nuked:8
				{- 79, 46,  76}, //Google Glass:9
				{- 59, 20, 105}, //Chemicals:10
				{- 86, 21, 195}, //Spiraling:11
				{-110,  4, 148}, //Paradoxal Hallway:12
				{-149, 15, 120}, //Teleporter After Battle:13
				{-211, 21,  39}, //Cave Troll:14
				{-217, 27, -16}, //With the Fishes:15
				{-227, 28, - 1}, //Cooperation:16
				{-160, 26,  27}, //Humongous:17
				{-121, 29,  22}, //Librarian:18
				{-121, 28,  16}, //Choices 1:19
				{-116, 26,  19}, //Choices 2:20
				{- 77, 38,  29}, //Choices 3:21
				{-375, 63,  56}, //Dejavu:22
			};
	
	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent e){	
		if(e.getBlock().getWorld().getName().equalsIgnoreCase(eCore.worldName)){
			int cEntry = -1;
			switch(GlobalDataManager.state){
				case ARMORY:
					cEntry = 0;
				case HEADQUARTERS:
					cEntry = newCEntry(cEntry, 1);
				case COOKIE:
					cEntry = newCEntry(cEntry, 2);
				case DARKNESS:
					cEntry = newCEntry(cEntry, 3);
				case PROUDOFSUBJECT:
					cEntry = newCEntry(cEntry, 4);
				case SURVIVOR:
					cEntry = newCEntry(cEntry, 5);
				case LAVAEVERYWHERE:
					cEntry = newCEntry(cEntry, 6);
				case MOBSEVERYWHERE:
					cEntry = newCEntry(cEntry, 7);
				case NUKED:
					cEntry = newCEntry(cEntry, 8);
				case GOOGLEGLASS:
					cEntry = newCEntry(cEntry, 9);
				case CHEMICALS:
					cEntry = newCEntry(cEntry, 10);
				case SPIRALING:
					cEntry = newCEntry(cEntry, 11);
				case PARADOXALHALLWAY:
					cEntry = newCEntry(cEntry, 12);
				case TELEPORTER_AFTER:
					cEntry = newCEntry(cEntry, 13);
				case CAVETROLL:
					cEntry = newCEntry(cEntry, 14);
				case WITHTHEFISHES:
					cEntry = newCEntry(cEntry, 15);
				case COOPERATION:
					cEntry = newCEntry(cEntry, 16);
				case HUMONGOUS:
					cEntry = newCEntry(cEntry, 17);
				case LIBRARIAN:
					cEntry = newCEntry(cEntry, 18);
				case CHOICES1:
					cEntry = newCEntry(cEntry, 19);
				case CHOICES2:
					cEntry = newCEntry(cEntry, 20);
				case CHOICES3:
					cEntry = newCEntry(cEntry, 21);
				case DEJAVU:
					cEntry = newCEntry(cEntry, 22);
					if (isEqual(e.getBlock().getLocation(), cEntry) && e.getOldCurrent()==0 && e.getNewCurrent()>0){
						GlobalDataManager.advanceCheckpoint();
					}
			default:
				break;
			}
		}
	}
	
	private int newCEntry(int oldOne, int newOne){
		if(oldOne<0)
			oldOne = newOne;
		return oldOne;
	}
	
	private boolean isEqual(Location loc, int entry){
		if(entry<0)
			return false;
		int[] coords = this.coords[entry];
		return loc.getBlockX() == coords[0] && loc.getBlockY() == coords[1] && loc.getBlockZ() == coords[2];
	}
}
