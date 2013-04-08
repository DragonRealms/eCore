package com.mcdr.ecore.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.mcdr.ecore.eCore;

public class eCorePlayer {
	private OfflinePlayer player;
	private PlayerInventory eCoreInv, extInv;
	private boolean eventInv;

	public Player getPlayer() {
		return player.getPlayer();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void updateInv(){
		Player plyr;
		if((plyr = player.getPlayer())!=null){
			PlayerDataManager.saveInventory(plyr, eventInv);
			if(plyr.getLocation().getWorld().getName().equalsIgnoreCase(eCore.worldname) && !eventInv){
				eCoreInv = PlayerDataManager.loadInventory(plyr, true);
				plyr.getInventory().setContents(eCoreInv.getContents());
			} else if(eventInv) {
				extInv = PlayerDataManager.loadInventory(plyr, false);
				plyr.getInventory().setContents(extInv.getContents());
			}
		}
	}
	
	public void save(){
		if(player.getPlayer()!=null)
			PlayerDataManager.savePlayerData(this);
	}
}
